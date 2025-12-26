package riffOrDie.view.renderer;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.List;

import riffOrDie.model.Amplifier;
import riffOrDie.model.Bullet;
import riffOrDie.model.GameEngine;
import riffOrDie.model.Monster;
import riffOrDie.model.MonsterType;
import riffOrDie.model.Player;
import riffOrDie.presenter.util.ImageLoader;
import riffOrDie.config.GameConstants;

public class GameRenderer {
    public static Image playerImage;
    public static Image monsterEasyImage;
    public static Image monsterHardImage;
    public static Image amplifierImage;
    
    static {
        playerImage = ImageLoader.loadImage("src/assets/player.png");
        monsterEasyImage = ImageLoader.loadImage("src/assets/monster-easy.png");
        monsterHardImage = ImageLoader.loadImage("src/assets/monster-hard.png");
        amplifierImage = ImageLoader.loadImage("src/assets/amplifier.png");
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

    public static void drawAmmo(Graphics2D g, int ammo, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Ammo: " + ammo, x, y);
    }

    private static void drawAmplifiers(Graphics2D g2d, List<Amplifier> amplifiers) {
        for (Amplifier amp : amplifiers) {
            if (amplifierImage != null) {
                // Draw amplifier image
                g2d.drawImage(
                    amplifierImage,
                    amp.getX(),
                    amp.getY(),
                    amp.getWidth(),
                    amp.getHeight(),
                    null
                );
            } else {
                // Fallback: black rectangle
                g2d.setColor(Color.BLACK);
                g2d.fillRect(
                    amp.getX(),
                    amp.getY(),
                    amp.getWidth(),
                    amp.getHeight()
                );
            }
            
            // Draw health display
            drawAmplifierHealth(g2d, amp);
        }
    }

    private static void drawMonsters(Graphics2D g2d, List<Monster> monsters) {
        for (Monster monster : monsters) {
            Image image = monster.getType() == MonsterType.EASY ? 
                          monsterEasyImage : monsterHardImage;
            
            if (image != null) {
                // Draw monster image
                g2d.drawImage(
                    image,
                    monster.getX(),
                    monster.getY(),
                    monster.getWidth(),
                    monster.getHeight(),
                    null
                );
            } else {
                // Fallback: black rectangle
                g2d.setColor(Color.BLACK);
                g2d.fillRect(
                    monster.getX(),
                    monster.getY(),
                    monster.getWidth(),
                    monster.getHeight()
                );
            }
            
            // Draw health bar only when damaged
            if (monster.getHealth() < monster.getType().getHealth()) {
                drawHealthBar(
                    g2d,
                    monster.getX(),
                    monster.getY() - 8,
                    monster.getWidth(),
                    monster.getHealth(),
                    monster.getType().getHealth()
                );
            }
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


    /**
     * Draw amplifier health number in center
     */
    private static void drawAmplifierHealth(Graphics2D g2d, Amplifier amplifier) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        
        String healthText = String.valueOf(amplifier.getHealth());
        int textX = amplifier.getX() + (amplifier.getWidth() / 2) - 4;
        int textY = amplifier.getY() + (amplifier.getHeight() / 2) + 5;
        
        g2d.drawString(healthText, textX, textY);
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
