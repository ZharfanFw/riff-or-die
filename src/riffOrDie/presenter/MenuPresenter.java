package riffOrDie.presenter;


/**
 * MENUPRESENTER - Bridge menu logic <-> MenuPanel
 * 
 * Fungsi:
 * - loadScoreboard() - Query database, populate table
 * - onPlayClicked() - Validate username + switch ke game
 * - onQuitClicked() - Exit aplikasi
 * 
 * Validation: Check username tidak kosong
 */
import java.util.List;

import riffOrDie.model.database.DatabaseManager;
import riffOrDie.model.GameScore;
import riffOrDie.view.MenuPanel;

/**
 * MenuPresenter - orchestrate menu logic
 * Implementasi dari IMenuPresenter
 * Handle semua business logic untuk menu screen
 */
public class MenuPresenter implements IMenuPresenter {
    private IMenuView view;
    private MenuPanel menuPanel;

    public MenuPresenter(IMenuView view, MenuPanel menuPanel) {
        this.view = view;
        this.menuPanel = menuPanel;
        this.view.setPresenter(this);
        loadScoreboard();
    }

    /**
     * Load scoreboard dari database dan update view
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
     * Handle play button click
     * Validate username dan start game
     */
    @Override
    public void onPlayClicked(String username) {
        String trimmedUsername = username.trim();

        // Validate username
        if (trimmedUsername.isEmpty()) {
            view.showError("Username cannot be empty!");
            return;
        }

        // Get sisa peluru dari database
        GameScore existingScore = DatabaseManager.getPlayerScore(trimmedUsername);
        int sisaPeluru = (existingScore != null) ? existingScore.getSisaPeluru() : 0;

        // Navigate ke game screen
        menuPanel.navigateToGame(trimmedUsername, sisaPeluru);
    }

    /**
     * Handle quit button click
     */
    @Override
    public void onQuitClicked() {
        System.exit(0);
    }
}
