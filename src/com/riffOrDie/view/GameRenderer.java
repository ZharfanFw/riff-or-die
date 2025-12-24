package com.riffOrDie.view;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.List;

import com.riffOrDie.model.Amplifier;
import com.riffOrDie.model.Bullet;
import com.riffOrDie.model.GameEngine;
import com.riffOrDie.model.Monster;
import com.riffOrDie.model.Player;
import com.riffOrDie.util.ImageLoader;
import com.riffOrDie.util.GameConstants;

public class GameRenderer {
    public static Image playerImage;
    static {
        playerImage = ImageLoader.loadImage("resources/images/player.png");
    }

    public static void render(Graphics2D g2d, GameEngine gameEngine) {
        drawAmplifiers(g2d, gameEngine.getAmplifiers());
        drawMonsters(g2d, gameEngine.getMonsters());
        drawBullets(g2d, gameEngine.getBullets());
        drawPlayer(g2d, gameEngine.getPlayer());
        drawUI(g2d, gameEngine);
    }

    // Public static methods for GamePanel rendering
    public static void drawAmplifiersStatic(Graphics2D g2d, List<Amplifier> amplifiers) {
        drawAmplifiers(g2d, amplifiers);
    }

    public static void drawMonstersStatic(Graphics2D g2d, List<Monster> monsters) {
        drawMonsters(g2d, monsters);
    }

    public static void drawBulletsStatic(Graphics2D g2d, List<Bullet> bullets) {
        drawBullets(g2d, bullets);
    }

    public static void drawPlayerStatic(Graphics2D g2d, Player player) {
        drawPlayer(g2d, player);
    }

    private static void drawAmplifiers(Graphics2D g2d, List<Amplifier> amplifiers) {
        for (Amplifier amp : amplifiers) {
            g2d.setColor(new Color(80, 80, 80));
            g2d.fillRect(amp.getX(), amp.getY(), amp.getWidth(), amp.getHeight());

            g2d.setColor(new Color(50, 50, 50));
            int gridSize = 8;
            for (int i = amp.getX(); i < amp.getX() + amp.getWidth(); i += gridSize) {
                for (int j = amp.getY(); j < amp.getY() + amp.getHeight(); j += gridSize) {
                    g2d.fillRect(i, j, gridSize - 2, gridSize - 2);
                }
            }

            g2d.setColor(new Color(150, 150, 150));
            g2d.setStroke(new java.awt.BasicStroke(2));
            g2d.drawRect(amp.getX(), amp.getY(), amp.getWidth(), amp.getHeight());
        }
    }

    private static void drawMonsters(Graphics2D g2d, List<Monster> monsters) {
        for (Monster monster : monsters) {
            // Draw monster body - dark red circle
            g2d.setColor(new Color(139, 0, 0));
            g2d.fillOval(
                    monster.getX(),
                    monster.getY(),
                    monster.getWidth(),
                    monster.getHeight());

            // Draw eyes
            g2d.setColor(Color.WHITE);
            int eyeRadius = 3;
            int centerX = monster.getX() + monster.getWidth() / 2;
            int centerY = monster.getY() + monster.getHeight() / 2;

            // Left eye
            g2d.fillOval(centerX - 8 - eyeRadius, centerY - eyeRadius, eyeRadius * 2, eyeRadius * 2);
            // Right eye
            g2d.fillOval(centerX + 8 - eyeRadius, centerY - eyeRadius, eyeRadius * 2, eyeRadius * 2);

            // Draw health bar
            drawHealthBar(g2d, monster.getX(), monster.getY() - 8, monster.getWidth(), monster.getHealth(), monster.getType().getHealth());
        }
    }

    private static void drawBullets(Graphics2D g2d, List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            if (bullet.isPlayerBullet()) {
                // Player bullet - blue sound wave
                g2d.setColor(new Color(0, 100, 255));
            } else {
                // Monster bullet - orange sound wave
                g2d.setColor(new Color(255, 165, 0));
            }

            drawSoundWave(g2d, bullet.getX(), bullet.getY(), bullet.isPlayerBullet());
        }
    }

    private static void drawPlayer(Graphics2D g2d, Player player) {
        if (playerImage != null) {
            g2d.drawImage(playerImage, player.getX(), player.getY(), player.getWidth(), player.getHeight(), null);
        } else {
            // Fallback: draw as green rectangle
            g2d.setColor(new Color(0, 200, 0));
            g2d.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());

            // Draw guitar string indicator
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new java.awt.BasicStroke(2));
            g2d.drawLine(
                    player.getX() + player.getWidth() / 2,
                    player.getY() + player.getHeight() / 2,
                    player.getX() + player.getWidth() / 2,
                    player.getY() - 10);
        }
    }

    private static void drawUI(Graphics2D g2d, GameEngine gameEngine) {
        // Set font for UI text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        // Draw score
        g2d.drawString("Score: " + gameEngine.getScore(), 10, 20);

        // Draw health
        g2d.drawString("Health: " + gameEngine.getPlayer().getHealth(), 10, 40);

        // Draw bullets fired and missed info
        g2d.drawString("Bullets Fired: " + gameEngine.getBulletsFired(), 10, 60);
        g2d.drawString("Bullets Missed: " + gameEngine.getBulletsMissed(), 10, 80);

        // Draw monsters count
        g2d.drawString("Enemies: " + gameEngine.getMonsters().size(), GameConstants.SCREEN_WIDTH - 150, 20);
    }

    private static void drawHealthBar(Graphics2D g2d, int x, int y, int width, int currentHealth, int maxHealth) {
        // Background (red)
        g2d.setColor(new Color(200, 0, 0));
        g2d.fillRect(x, y, width, 4);

        // Health (green)
        int healthWidth = (int) ((currentHealth / (float) maxHealth) * width);
        g2d.setColor(new Color(0, 200, 0));
        g2d.fillRect(x, y, healthWidth, 4);

        // Border
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new java.awt.BasicStroke(1));
        g2d.drawRect(x, y, width, 4);
    }

    private static void drawSoundWave(Graphics2D g2d, int x, int y, boolean isPlayerBullet) {
        // Draw concentric circles to represent sound wave
        int[] radiuses = {3, 6, 9};
        for (int radius : radiuses) {
            g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
        }

        // Draw direction indicator line
        if (isPlayerBullet) {
            g2d.drawLine(x, y, x, y - 5);
        } else {
            g2d.drawLine(x, y, x, y + 5);
        }
    }
}
