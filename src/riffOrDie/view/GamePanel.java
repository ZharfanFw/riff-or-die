package riffOrDie.view;

import riffOrDie.presenter.AudioManager;

/**
 * GAMEPANEL - Game rendering & input
 * 
 * Fungsi:
 * - Game loop (run() method)
 * - Input handler (keyboard listener)
 * - paintComponent() - Render semua entities
 * - Update HUD (score, health, ammo, enemies, wave)
 * - Game over dialog
 * - Wave notification (fade in/out)
 * 
 * Key Methods: run(), paintComponent(), keyPressed()
 */

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.util.List;

import riffOrDie.presenter.InputHandler;
import riffOrDie.view.GameRenderer;
import riffOrDie.model.Amplifier;
import riffOrDie.model.Bullet;
import riffOrDie.model.Monster;
import riffOrDie.model.Player;
import riffOrDie.presenter.GamePresenter;
import riffOrDie.presenter.IGamePresenter;
import riffOrDie.presenter.IGameView;
import riffOrDie.config.GameConstants;

/**
 * GamePanel - Pure View, implements IGameView
 * Hanya bertanggung jawab untuk display dan input handling
 * Semua logic ada di GamePresenter
 */
public class GamePanel extends JPanel implements IGameView, Runnable {
    private GameFrame gameFrame;
    private IGamePresenter presenter;
    private InputHandler inputHandler;

    // Game state untuk rendering (diupdate oleh presenter)
    private Player player;
    private List<Monster> monsters;
    private List<Bullet> bullets;
    private List<Amplifier> amplifiers;
    private int score;
    private int health;
    private int currentAmmo = 0;
    private int bulletsMissed = 0;
    private int currentWave = 0;
    private int notificationWave = -1;
    private long notificationStartTime = 0;
    private boolean isNotificationActive = false;

    private Thread gameThread;
    private volatile boolean isRunning;

    public GamePanel(GameFrame gameFrame, String username, int sisaPeluru) {
        this.gameFrame = gameFrame;
        this.isRunning = true;

        setBackground(new java.awt.Color(30, 30, 30));
        setFocusable(true);

        // Create presenter
        this.presenter = new GamePresenter(this);

        // Setup input handler
        this.inputHandler = new InputHandler(presenter);
        addKeyListener(inputHandler);

        // Start game
        presenter.startGame(username, sisaPeluru);

        // Initialize audio system
        AudioManager.initialize();
        AudioManager.playBackgroundMusic();

        // Start game loop
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long lastFrameTime = System.currentTimeMillis();

        while (isRunning) {
            long currentTime = System.currentTimeMillis();
            long deltaTimeMs = currentTime - lastFrameTime;
            double deltaTime = deltaTimeMs / 1000.0;

            // Update game state melalui presenter
            presenter.update(deltaTime);

            repaint();

            try {
                long sleepTime = GameConstants.FRAME_TIME - deltaTimeMs;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            lastFrameTime = currentTime;
        }

        // Game loop thread ends here
        // Game over dialog already handled by showGameOverDialog() method
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Call GameRenderer dengan player, monsters, bullets, amplifiers
        // Copy lists to prevent ConcurrentModificationException
        if (amplifiers != null) {
            GameRenderer.drawAmplifiersStatic(g2d, new java.util.ArrayList<>(amplifiers));
        }
        if (monsters != null) {
            GameRenderer.drawMonstersStatic(g2d, new java.util.ArrayList<>(monsters));
        }
        if (bullets != null) {
            GameRenderer.drawBulletsStatic(g2d, new java.util.ArrayList<>(bullets));
        }
        if (player != null) {
            GameRenderer.drawPlayerStatic(g2d, player);
        }

        drawUI(g2d);
    }

    private void drawUI(Graphics2D g2d) {
        g2d.setColor(java.awt.Color.WHITE);
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));

        // Draw score
        g2d.drawString("Score: " + score, 10, 20);

        // Draw health
        g2d.drawString("Health: " + health, 10, 40);

        // Draw ammo (with missed count)
        GameRenderer.drawAmmo(g2d, currentAmmo, bulletsMissed, 10, 60);

        // Draw enemies count (kanan atas)
        if (monsters != null) {
            g2d.drawString("Enemies: " + monsters.size(), 700, 20);

            g2d.drawString("Wave: " + currentWave, GameConstants.WAVE_HUD_X, GameConstants.WAVE_HUD_Y);

            // Draw wave notification (fade in/out effect)
            if (isNotificationActive && notificationWave >= 0) {
                long elapsed = System.currentTimeMillis() - notificationStartTime;

                if (elapsed >= GameConstants.WAVE_NOTIFICATION_DURATION) {
                    isNotificationActive = false;
                } else {
                    // Calculate alpha untuk fade in/out
                    float alpha = 1.0f;
                    if (elapsed < 500) {
                        // Fade in (0 → 1)
                        alpha = (float) elapsed / 500f;
                    } else if (elapsed > 1500) {
                        // Fade out (1 → 0)
                        alpha = 1f - ((float) (elapsed - 1500) / 500f);
                    }
                    alpha = Math.max(0, Math.min(1, alpha));

                    // Draw notification text
                    Font oldFont = g2d.getFont();
                    g2d.setFont(new Font("Arial", Font.BOLD, GameConstants.WAVE_NOTIFICATION_FONT_SIZE));
                    g2d.setColor(new Color(255, 0, 0, (int) (255 * alpha))); // Red dengan alpha

                    String text = "WAVE " + notificationWave;
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (GameConstants.SCREEN_WIDTH - fm.stringWidth(text)) / 2;
                    int textY = (GameConstants.SCREEN_HEIGHT + fm.getAscent()) / 2;

                    g2d.drawString(text, textX, textY);
                    g2d.setFont(oldFont);
                }
            }
        }
    }

    public void stopGame() {
        isRunning = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * IGameView Implementation Methods
     */

    @Override
    public void setPresenter(IGamePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateScore(int score) {
        this.score = score;
    }

    @Override
    public void updateHealth(int health) {
        this.health = health;
    }

    @Override
    public void updateBulletsFired(int bulletsFired) {
        // Optional: display bullets fired info
    }

    @Override
    public void updateAmmo(int ammoCount, int bulletsMissed) {
        this.currentAmmo = ammoCount;
        this.bulletsMissed = bulletsMissed;
    }

    @Override
    public void updateWave(int waveNumber) {
        this.currentWave = waveNumber;
    }

    @Override
    public void showWaveNotification(int waveNumber) {
        this.notificationWave = waveNumber;
        this.notificationStartTime = System.currentTimeMillis();
        this.isNotificationActive = true;
    }

    public void updateMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    @Override
    public void updateBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    @Override
    public void updatePlayer(Player player) {
        this.player = player;
    }

    @Override
    public void updateAmplifiers(List<Amplifier> amplifiers) {
        this.amplifiers = amplifiers;
    }

    @Override
    public void showGameOverDialog(int finalScore) {
        // Stop game loop
        isRunning = false;

        // Stop background music
        AudioManager.stopBackgroundMusic();
        // Stop background music
        AudioManager.stopBackgroundMusic();

        // Show blocking JOptionPane dialog
        JOptionPane.showMessageDialog(
                this,
                "Game Over!\n\nScore: " + finalScore,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);

        // After user clicks OK, return to menu
        gameFrame.backToMenu();
    }

    @Override
    public void repaintGame() {
        repaint();
    }
}
