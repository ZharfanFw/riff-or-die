package com.riffOrDie.view;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.util.List;

import com.riffOrDie.util.InputHandler;
import com.riffOrDie.model.Amplifier;
import com.riffOrDie.model.Bullet;
import com.riffOrDie.model.Monster;
import com.riffOrDie.model.Player;
import com.riffOrDie.presenter.GamePresenter;
import com.riffOrDie.presenter.IGamePresenter;
import com.riffOrDie.presenter.IGameView;
import com.riffOrDie.util.GameConstants;

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

        // Start game loop
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long lastFrameTime = System.currentTimeMillis();

        while (isRunning && !presenter.isGameOver()) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastFrameTime;

            // Update game state melalui presenter
            presenter.update();

            repaint();

            try {
                long sleepTime = GameConstants.FRAME_TIME - deltaTime;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            lastFrameTime = currentTime;
        }

        // Game over
        if (isRunning) {
            onGameOver();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Render semua entities yang telah di-update oleh presenter
        if (player != null && monsters != null && bullets != null && amplifiers != null) {
            // Create temporary GameEngine-like object for rendering
            // Or render directly using stored state
            g2d.setColor(java.awt.Color.WHITE);
            g2d.drawString("Riff or Die", 10, 20);
        }

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

        // Draw enemies count
        if (monsters != null) {
            g2d.drawString("Enemies: " + monsters.size(), GameConstants.SCREEN_WIDTH - 150, 20);
        }
    }

    private void onGameOver() {
        isRunning = false;

        JOptionPane.showMessageDialog(
                this,
                "Game Over!\nScore: " + score,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);

        gameFrame.backToMenu();
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
    public void showGameOver(int finalScore) {
        // Handled by onGameOver()
    }

    @Override
    public void repaintGame() {
        repaint();
    }
}
