package riffOrDie.presenter;

/**
 * MenuPresenter - Orchestrator untuk menu screen (main menu)
 * 
 * Fungsi Utama:
 * - Load leaderboard dari database dan display ke user
 * - Validate user input (username)
 * - Initiate game start dengan player name
 * - Handle quit/exit aplikasi
 * 
 * Key Methods:
 * - loadScoreboard(): Query database untuk top scores, update MenuPanel
 * - onPlayClicked(username): Validate input, start game session
 * - onQuitClicked(): Exit aplikasi
 * 
 * MVP Pattern:
 * - MenuPresenter: Business logic untuk menu (orchestration)
 * - MenuPanel: UI rendering (view)
 * - DatabaseManager: Data access (model)
 * - IMenuView/IMenuPresenter: Loose coupling via interfaces
 * 
 * Flow:
 * 1. Application startup → create MenuPresenter
 * 2. Constructor call loadScoreboard()
 * 3. User input username → onPlayClicked()
 * 4. MenuPresenter validate + query existing score
 * 5. MenuPanel.navigateToGame() switch ke GamePanel
 * 
 * Data Flow:
 * - Database → DatabaseManager.getAllScores() → MenuPresenter
 * - MenuPresenter → view.updateScoreboard(List<GameScore>) → MenuPanel render
 * 
 * Validation:
 * - Username tidak boleh kosong (trim whitespace)
 * - Check database apakah player sudah main sebelumnya
 * - Return sisaPeluru dari previous session untuk game start
 * 
 * Error Handling:
 * - Database error: Show error dialog ke user
 * - Input validation: Show error message jika username empty
 * - Graceful: Continue menggunakan empty data jika database fail
 * 
 * State Management:
 * - view: Reference ke IMenuView (MenuPanel)
 * - menuPanel: Reference to MenuPanel (untuk navigateToGame call)
 * - No game state: Presenter stateless (hanya bridge)
 */
import java.util.List;

import riffOrDie.model.DatabaseManager;
import riffOrDie.model.GameScore;
import riffOrDie.view.MenuPanel;

public class MenuPresenter implements IMenuPresenter {
    /** Reference ke IMenuView (MenuPanel) */
    private IMenuView view;
    
    /** Reference ke MenuPanel untuk navigasi */
    private MenuPanel menuPanel;

    /**
     * Constructor - Inisialisasi presenter dengan view
     * 
     * @param view Reference ke IMenuView
     * @param menuPanel Reference ke MenuPanel untuk navigateToGame
     */
    public MenuPresenter(IMenuView view, MenuPanel menuPanel) {
        this.view = view;
        this.menuPanel = menuPanel;
        this.view.setPresenter(this);
        loadScoreboard();
    }

    /**
     * Load scoreboard dari database dan update view
     * Query top scores dari DatabaseManager dan pass ke MenuPanel
     * Error handling: Show error message jika database fail
     */
    @Override
    public void loadScoreboard() {
        try {
            List<GameScore> scores = DatabaseManager.getAllScores();
            view.updateScoreboard(scores);
        } catch (Exception e) {
            view.showError("Failed to load scoreboard: " + e.getMessage());
        }
    }

    /**
     * Handle play button click dengan username input
     * Validation: Check username tidak empty
     * Look up: Query database untuk existing score + sisa peluru
     * Navigate: Start game dengan player name
     * 
     * @param username Username yang diinput user
     */
    @Override
    public void onPlayClicked(String username) {
        String trimmedUsername = username.trim();

        // Validate username
        if (trimmedUsername.isEmpty()) {
            view.showError("Username cannot be empty!");
            return;
        }

        // Get sisa peluru dari database (untuk continue saved session)
        GameScore existingScore = DatabaseManager.getPlayerScore(trimmedUsername);
        int sisaPeluru = (existingScore != null) ? existingScore.getSisaPeluru() : 0;

        // Navigate ke game screen dengan username dan available ammo
        menuPanel.navigateToGame(trimmedUsername, sisaPeluru);
    }

    /**
     * Handle quit button click
     * Exit aplikasi dengan code 0 (normal exit)
     */
    @Override
    public void onQuitClicked() {
        System.exit(0);
    }
}
