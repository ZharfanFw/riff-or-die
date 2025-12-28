package riffOrDie.view;

/**
 * GAMERENDERER - Static drawing utilities
 * 
 * Fungsi:
 * - drawPlayer() - Draw player PNG
 * - drawMonsters() - Draw monster PNG + health bar
 * - drawBullets() - Draw bullet PNG + rotation
 * - drawAmplifiers() - Draw amplifier PNG + health
 * - drawAmmo() - Draw ammo HUD text
 * 
 * Pattern: Static methods - reusable, no state!
 * 
 * Fallback: Colored rectangles jika PNG load fail
 */
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.List;

import riffOrDie.model.Amplifier;
import riffOrDie.model.Bullet;
import riffOrDie.model.GameEngine;
import riffOrDie.model.Monster;
import riffOrDie.model.MonsterType;
import riffOrDie.model.Player;
import riffOrDie.presenter.ImageLoader;
import riffOrDie.config.GameConstants;

public class GameRenderer {
    /** Image sprite player */
    public static Image playerImage;

    /** Image sprite monster easy */
    public static Image monsterEasyImage;

    /** Image sprite monster hard */
    public static Image monsterHardImage;

    /** Image sprite amplifier */
    public static Image amplifierImage;

    /** Image sprite bullet player */
    public static Image playerBulletImage;

    /** Image sprite bullet monster */
    public static Image monsterBulletImage;

    /**
     * Static block - Load semua image sprites saat class dimuat
     */
    static {
        playerImage = ImageLoader.loadImage("src/assets/player.png");
        monsterEasyImage = ImageLoader.loadImage("src/assets/monster-easy.png");
        monsterHardImage = ImageLoader.loadImage("src/assets/monster-hard.png");
        amplifierImage = ImageLoader.loadImage("src/assets/amplifier.png");
        playerBulletImage = ImageLoader.loadImage("src/assets/bullet-player.png");
        monsterBulletImage = ImageLoader.loadImage("src/assets/bullet-monster.png");
    }

    /**
     * Render semua elements ke screen
     * Helper method untuk render full game state
     * 
     * @param g2d        Graphics2D context
     * @param gameEngine GameEngine yang hold semua state
     */
    public static void render(Graphics2D g2d, GameEngine gameEngine) {
        drawAmplifiers(g2d, gameEngine.getAmplifiers());
        drawMonsters(g2d, gameEngine.getMonsters());
        drawBullets(g2d, gameEngine.getBullets());
        drawPlayer(g2d, gameEngine.getPlayer());
        drawUI(g2d, gameEngine);
    }

    // ===================== PUBLIC STATIC METHODS =====================

    /**
     * Draw amplifiers (static wrapper untuk GamePanel)
     * 
     * @param g2d        Graphics2D context
     * @param amplifiers List amplifier yang akan digambar
     */
    public static void drawAmplifiersStatic(Graphics2D g2d, List<Amplifier> amplifiers) {
        drawAmplifiers(g2d, amplifiers);
    }

    /**
     * Draw monsters (static wrapper untuk GamePanel)
     * 
     * @param g2d      Graphics2D context
     * @param monsters List monster yang akan digambar
     */
    public static void drawMonstersStatic(Graphics2D g2d, List<Monster> monsters) {
        drawMonsters(g2d, monsters);
    }

    /**
     * Draw bullets (static wrapper untuk GamePanel)
     * 
     * @param g2d     Graphics2D context
     * @param bullets List bullet yang akan digambar
     */
    public static void drawBulletsStatic(Graphics2D g2d, List<Bullet> bullets) {
        drawBullets(g2d, bullets);
    }

    /**
     * Draw player (static wrapper untuk GamePanel)
     * 
     * @param g2d    Graphics2D context
     * @param player Player entity
     */
    public static void drawPlayerStatic(Graphics2D g2d, Player player) {
        drawPlayer(g2d, player);
    }

    /**
     * Draw ammo HUD text
     * 
     * @param g             Graphics context
     * @param ammo          Jumlah ammo saat ini
     * @param bulletsMissed Jumlah bullet meleset
     * @param x             Posisi X
     * @param y             Posisi Y
     */
    public static void drawAmmo(Graphics2D g, int ammo, int bulletsMissed, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Ammo: " + ammo + " (Missed: " + bulletsMissed + ")", x, y);
    }

    // ===================== PRIVATE DRAWING METHODS =====================

    /**
     * Draw semua amplifiers
     * 
     * @param g2d        Graphics2D context
     * @param amplifiers List amplifier aktif
     */
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
                        null);
            } else {
                // Fallback: black rectangle
                g2d.setColor(Color.BLACK);
                g2d.fillRect(
                        amp.getX(),
                        amp.getY(),
                        amp.getWidth(),
                        amp.getHeight());
            }

            // Draw health display
            drawAmplifierHealth(g2d, amp);
        }
    }

    /**
     * Draw semua monsters
     * 
     * @param g2d      Graphics2D context
     * @param monsters List monster aktif
     */
    private static void drawMonsters(Graphics2D g2d, List<Monster> monsters) {
        for (Monster monster : monsters) {
            Image image = monster.getType() == MonsterType.EASY ? monsterEasyImage : monsterHardImage;

            if (image != null) {
                // Draw monster image
                g2d.drawImage(
                        image,
                        monster.getX(),
                        monster.getY(),
                        monster.getWidth(),
                        monster.getHeight(),
                        null);
            } else {
                // Fallback: black rectangle
                g2d.setColor(Color.BLACK);
                g2d.fillRect(
                        monster.getX(),
                        monster.getY(),
                        monster.getWidth(),
                        monster.getHeight());
            }

            // Draw health bar only when damaged
            if (monster.getHealth() < monster.getType().getHealth()) {
                drawHealthBar(
                        g2d,
                        monster.getX(),
                        monster.getY() - 8,
                        monster.getWidth(),
                        monster.getHealth(),
                        monster.getType().getHealth());
            }
        }
    }

    /**
     * Draw semua bullets
     * 
     * @param g2d     Graphics2D context
     * @param bullets List bullet aktif
     */
    private static void drawBullets(Graphics2D g2d, List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            double angle = bullet.getAngle(); // angle dalam radians

            // Tentukan ukuran dan image berdasarkan tipe bullet
            int bulletWidth;
            int bulletHeight;
            Image bulletImage;
            Color fallbackColor;

            if (bullet.isPlayerBullet()) {
                bulletWidth = 36; // Player bullet 2x scaled
                bulletHeight = 12;
                bulletImage = playerBulletImage;
                fallbackColor = new Color(0, 100, 255); // Blue fallback
            } else {
                bulletWidth = 22; // Monster bullet 2x scaled
                bulletHeight = 16;
                bulletImage = monsterBulletImage;
                fallbackColor = new Color(255, 165, 0); // Orange fallback
            }

            // Save current transform
            AffineTransform oldTransform = g2d.getTransform();

            // Calculate center point untuk rotation
            int centerX = bullet.getX() + (bulletWidth / 2);
            int centerY = bullet.getY() + (bulletHeight / 2);

            // Apply transformation: translate -> rotate -> translate back
            g2d.translate(centerX, centerY);
            g2d.rotate(angle);
            g2d.translate(-centerX, -centerY);

            // Draw bullet image atau fallback
            if (bulletImage != null) {
                g2d.drawImage(bulletImage, bullet.getX(), bullet.getY(), bulletWidth, bulletHeight, null);
            } else {
                // Fallback: colored rectangle
                g2d.setColor(fallbackColor);
                g2d.fillRect(bullet.getX(), bullet.getY(), bulletWidth, bulletHeight);
            }

            // Restore original transform
            g2d.setTransform(oldTransform);
        }
    }

    /**
     * Draw player
     * 
     * @param g2d    Graphics2D context
     * @param player Player entity
     */
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

    /**
     * Draw UI elements (score, health, bullets, enemies)
     * 
     * @param g2d        Graphics2D context
     * @param gameEngine GameEngine untuk ambil data
     */
    private static void drawUI(Graphics2D g2d, GameEngine gameEngine) {
        // Set font untuk UI text
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
     * Draw amplifier health number di tengah
     * 
     * @param g2d       Graphics2D context
     * @param amplifier Amplifier entity
     */
    private static void drawAmplifierHealth(Graphics2D g2d, Amplifier amplifier) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        String healthText = String.valueOf(amplifier.getHealth());
        int textX = amplifier.getX() + (amplifier.getWidth() / 2) - 4;
        int textY = amplifier.getY() + (amplifier.getHeight() / 2) + 5;

        g2d.drawString(healthText, textX, textY);
    }

    /**
     * Draw health bar untuk monster yang terdamage
     * 
     * @param g2d           Graphics2D context
     * @param x             Posisi X
     * @param y             Posisi Y
     * @param width         Lebar health bar
     * @param currentHealth Health saat ini
     * @param maxHealth     Health maksimum
     */
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

    /**
     * Draw sound wave effect (tidak dipakai di game)
     * 
     * @param g2d            Graphics2D context
     * @param x              Posisi X
     * @param y              Posisi Y
     * @param isPlayerBullet Apakah bullet player
     */
    private static void drawSoundWave(Graphics2D g2d, int x, int y, boolean isPlayerBullet) {
        // Draw concentric circles to represent sound wave
        int[] radiuses = { 3, 6, 9 };
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
