package riffOrDie.view;

/**
 * GAMEFRAME - Main window container
 * 
 * Fungsi:
 * - JFrame setup (800x600, not resizable)
 * - CardLayout untuk panel switching (menu <-> game)
 * - switchToGamePanel() - Menu ke Game
 * - switchToMenuPanel() - Game ke Menu
 * 
 * Pattern: JFrame + CardLayout = clean panel switching
 */
import javax.swing.JFrame;
import riffOrDie.presenter.MenuPresenter;
import riffOrDie.config.GameConstants;

public class GameFrame extends JFrame {
    /** Menu panel reference */
    private MenuPanel menuPanel;
    
    /** Game panel reference */
    private GamePanel gamePanel;
    
    /** Menu presenter reference */
    private MenuPresenter menuPresenter;

    /**
     * Constructor - Inisialisasi main window
     * Setup JFrame properties dan tampilkan menu panel
     */
    public GameFrame() {
        setTitle("Riff or Die");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        setSize(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

        // Tampilkan menu panel saat startup
        showMenuPanel();
    }

    /**
     * Tampilkan menu panel (main menu)
     * Hapus game panel jika ada, create menu panel baru
     */
    public void showMenuPanel() {
        if (gamePanel != null) {
            remove(gamePanel);
        }

        menuPanel = new MenuPanel(this);
        menuPresenter = new MenuPresenter(menuPanel, menuPanel);
        add(menuPanel);

        revalidate();
        repaint();
    }

    /**
     * Tampilkan game panel dengan username dan ammo awal
     * Hapus menu panel jika ada, create game panel baru
     * 
     * @param username Username player
     * @param sisaPeluru Ammo awal (dari previous session)
     */
    public void showGamePanel(String username, int sisaPeluru) {
        if (menuPanel != null) {
            remove(menuPanel);
        }

        gamePanel = new GamePanel(this, username, sisaPeluru);
        add(gamePanel);

        // Request focus untuk keyboard input
        gamePanel.requestFocusInWindow();

        revalidate();
        repaint();
    }

    /**
     * Kembali ke menu panel
     * Stop game loop, hapus game panel, tampilkan menu lagi
     */
    public void backToMenu() {
        if (gamePanel != null) {
            gamePanel.stopGame();
            remove(gamePanel);
        }
        showMenuPanel();
    }
}
