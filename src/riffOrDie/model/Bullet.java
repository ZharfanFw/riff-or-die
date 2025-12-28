package riffOrDie.model;

/**
 * BULLET - Entity proyektil
 * 
 * Fungsi:
 * - Posisi, velocity (velocityX, velocityY)
 * - isPlayerBullet() check
 * - getAngle() untuk visual rotation
 * - Collision detection (AABB)
 * 
 * Key: Velocity-based movement untuk smooth tracking
 */
import riffOrDie.config.GameConstants;

public class Bullet {
    /** Posisi X bullet */
    private int x;
    
    /** Posisi Y bullet */
    private int y;
    
    /** Lebar hitbox bullet */
    private int width;
    
    /** Tinggi hitbox bullet */
    private int height;

    /** Kecepatan bullet (px/detik) */
    private double speed;
    
    /** Kecepatan arah X (normalized) */
    private double velocityX;
    
    /** Kecepatan arah Y (normalized) */
    private double velocityY;

    /** Flag apakah bullet ini milik player */
    private boolean isPlayerBullet;

    /**
     * Constructor - Buat bullet baru
     * 
     * @param startX Posisi X awal
     * @param startY Posisi Y awal
     * @param isPlayerBullet true jika bullet player, false jika bullet monster
     */
    public Bullet(int startX, int startY, boolean isPlayerBullet) {
        this.x = startX;
        this.y = startY;
        this.width = 5;
        this.height = 10;
        this.isPlayerBullet = isPlayerBullet;
        this.velocityX = 0;
        this.velocityY = 0;

        // Set speed berdasarkan tipe bullet
        if (isPlayerBullet) {
            this.speed = GameConstants.PLAYER_BULLET_SPEED;
        } else {
            this.speed = GameConstants.MONSTER_BULLET_SPEED;
        }
    }

    /**
     * Set velocity arah bullet
     * 
     * @param vx Velocity arah X (normalized: -1 s/d 1)
     * @param vy Velocity arah Y (normalized: -1 s/d 1)
     */
    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    /**
     * Update posisi bullet setiap frame
     * Player bullet: bergerak ke atas (Y berkurang)
     * Monster bullet: bergerak mengikuti arah velocity
     * 
     * @param deltaTime Delta time (detik) sejak frame terakhir
     */
    public void update(double deltaTime) {
        if (isPlayerBullet) {
            // Player bullet bergerak lurus ke atas
            y += (int) (speed * deltaTime);
        } else {
            // Monster bullet bergerak dengan velocity (smooth tracking ke player)
            x += (int) (velocityX * speed * deltaTime);
            y += (int) (velocityY * speed * deltaTime);
        }
    }

    /**
     * Cek apakah bullet masih dalam batas layar
     * Bullet dianggap tidak aktif jika keluar dari layar
     * 
     * @return true jika masih aktif (di layar), false jika sudah keluar
     */
    public boolean isActive() {
        return y > -height && y < GameConstants.SCREEN_HEIGHT + height &&
                x > -width && x < GameConstants.SCREEN_WIDTH + width;
    }

    /**
     * Cek apakah bullet ini milik player
     * 
     * @return true jika bullet player, false jika bullet monster
     */
    public boolean isPlayerBullet() {
        return this.isPlayerBullet;
    }

    /**
     * Ambil posisi X bullet
     * 
     * @return Posisi X dalam pixel
     */
    public int getX() {
        return x;
    }

    /**
     * Ambil posisi Y bullet
     * 
     * @return Posisi Y dalam pixel
     */
    public int getY() {
        return y;
    }

    /**
     * Ambil lebar hitbox bullet
     * 
     * @return Lebar dalam pixel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Ambil tinggi hitbox bullet
     * 
     * @return Tinggi dalam pixel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Ambil kecepatan bullet
     * 
     * @return Kecepatan dalam pixel/detik
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Ambil velocity arah X
     * 
     * @return Velocity X (normalized: -1 s/d 1)
     */
    public double getVelocityX() {
        return velocityX;
    }

    /**
     * Ambil velocity arah Y
     * 
     * @return Velocity Y (normalized: -1 s/d 1)
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Hitung angle rotasi bullet berdasarkan arah pergerakan
     * Returns angle dalam radians untuk AffineTransform rotation di GameRenderer
     * 
     * @return Angle rotasi dalam radians
     */
    public double getAngle() {
        return Math.atan2(velocityY, velocityX);
    }
}
