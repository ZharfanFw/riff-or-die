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
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private MenuPresenter menuPresenter;

    public GameFrame() {
        setTitle("Riff or Die");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        setSize(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);

        showMenuPanel();
    }

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

    public void showGamePanel(String username, int sisaPeluru) {
        if (menuPanel != null) {
            remove(menuPanel);
        }

        gamePanel = new GamePanel(this, username, sisaPeluru);
        add(gamePanel);

        gamePanel.requestFocusInWindow();

        revalidate();
        repaint();
    }

    public void backToMenu() {
        if (gamePanel != null) {
            gamePanel.stopGame();
            remove(gamePanel);
        }
        showMenuPanel();
    }
}
