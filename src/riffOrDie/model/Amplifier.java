package riffOrDie.model;

/**
 * AMPLIFIER - Entity obstacle
 * 
 * Fungsi:
 * - Posisi, health (3 HP)
 * - Take damage method
 * - Check alive status
 * 
 * Regen: Auto-regenerate setiap 8 detik (di GameEngine)
 */
import riffOrDie.config.GameConstants;

public class Amplifier {
    /** Posisi X amplifier */
    private int x;
    
    /** Posisi Y amplifier */
    private int y;
    
    /** Lebar hitbox amplifier */
    private int width;
    
    /** Tinggi hitbox amplifier */
    private int height;
    
    /** Health amplifier (default 3 HP) */
    private int health;

    /**
     * Constructor - Buat amplifier baru di posisi spesifik
     * 
     * @param x Posisi X spawn
     * @param y Posisi Y spawn
     */
    public Amplifier(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = GameConstants.AMPLIFIER_WIDTH;
        this.height = GameConstants.AMPLIFIER_HEIGHT;
        this.health = GameConstants.AMPLIFIER_HEALTH;
    }

    /**
     * Cek collision antara amplifier dan bullet
     * 
     * @param bullet Bullet yang dicek
     * @return true jika collision, false jika tidak
     */
    public boolean collidesWith(Bullet bullet) {
        boolean collidingX = x < bullet.getX() + bullet.getWidth() && x + width > bullet.getX();
        boolean collidingY = y < bullet.getY() + bullet.getHeight() && y + height > bullet.getY();

        return collidingX && collidingY;
    }

    /**
     * Ambil damage untuk amplifier ini
     * Health akan dikurangi, minimum 0
     * 
     * @param damage Jumlah damage yang diterima
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Cek apakah amplifier masih hidup
     * 
     * @return true jika health > 0, false jika health <= 0
     */
    public boolean isAlive() {
        return this.health > 0;
    }

    /**
     * Ambil posisi X amplifier
     * 
     * @return Posisi X dalam pixel
     */
    public int getX() {
        return x;
    }

    /**
     * Ambil posisi Y amplifier
     * 
     * @return Posisi Y dalam pixel
     */
    public int getY() {
        return y;
    }

    /**
     * Ambil lebar hitbox amplifier
     * 
     * @return Lebar dalam pixel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Ambil tinggi hitbox amplifier
     * 
     * @return Tinggi dalam pixel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Ambil health amplifier saat ini
     * 
     * @return Health amplifier (0-3)
     */
    public int getHealth() {
        return health;
    }
}
