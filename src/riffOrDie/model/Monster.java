package riffOrDie.model;

/**
 * MONSTER - Entity musuh
 * 
 * Fungsi:
 * - Posisi, health, type (EASY/HARD)
 * - Shoot logic dengan cooldown
 * - Take damage method
 */
import riffOrDie.config.GameConstants;

public class Monster {
    /** Posisi X monster */
    private int x;
    
    /** Posisi Y monster */
    private int y;
    
    /** Lebar hitbox monster */
    private int width;
    
    /** Tinggi hitbox monster */
    private int height;

    /** Tipe monster (EASY/HARD) */
    private MonsterType type;
    
    /** Health monster saat ini */
    private int health;
    
    /** Health maksimum monster */
    private int maxHealth;

    /** Cooldown waktu antar tembakan (ms) */
    private long shootCooldown;
    
    /** Waktu tembakan terakhir (timestamp) */
    private long lastShootTime;

    /**
     * Constructor - Buat monster baru
     * 
     * @param startX Posisi X spawn
     * @param startY Posisi Y spawn
     * @param type Tipe monster (EASY atau HARD)
     */
    public Monster(int startX, int startY, MonsterType type) {
        this.x = startX;
        this.y = startY;
        this.type = type;
        this.width = type.getWidth();
        this.height = type.getHeight();
        this.health = type.getHealth();
        this.maxHealth = type.getHealth();
        this.shootCooldown = GameConstants.MONSTER_SHOOT_COOLDOWN;
        this.lastShootTime = System.currentTimeMillis();
    }

    /**
     * Update monster setiap frame
     * Monster tidak bergerak, posisi fixed di spawn
     * Hanya bullet dari monster yang bergerak ke arah player
     * 
     * @param deltaTime Delta time (detik)
     */
    public void update(double deltaTime) {
        // Monster position is fixed at spawn, does not move
        // Only bullets from monster move towards player
    }

    /**
     * Ambil damage untuk monster ini
     * Health akan dikurangi, minimum 0
     * 
     * @param damage Jumlah damage yang diterima
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Cek apakah monster sudah bisa menembak lagi
     * Berdasarkan cooldown time
     * 
     * @return true jika sudah bisa menembak, false jika masih cooldown
     */
    public boolean canShoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShootTime >= shootCooldown) {
            return true;
        }
        return false;
    }

    /**
     * Cek apakah monster masih hidup
     * 
     * @return true jika health > 0, false jika health <= 0
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Cek apakah monster sudah keluar dari layar
     * (meskipun monster tidak bergerak, method ini disediakan untuk consistency)
     * 
     * @return true jika Y position < -height
     */
    public boolean isOutOfBounds() {
        return y < -height;
    }

    /**
     * Ambil posisi X monster
     * 
     * @return Posisi X dalam pixel
     */
    public int getX() {
        return x;
    }

    /**
     * Ambil posisi Y monster
     * 
     * @return Posisi Y dalam pixel
     */
    public int getY() {
        return y;
    }

    /**
     * Ambil lebar hitbox monster
     * 
     * @return Lebar dalam pixel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Ambil tinggi hitbox monster
     * 
     * @return Tinggi dalam pixel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Ambil health monster saat ini
     * 
     * @return Health monster
     */
    public int getHealth() {
        return health;
    }

    /**
     * Ambil health maksimum monster
     * 
     * @return Health maksimum
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Ambil tipe monster
     * 
     * @return Tipe monster (EASY atau HARD)
     */
    public MonsterType getType() {
        return type;
    }

    /**
     * Ambil posisi X tengah monster
     * Digunakan untuk spawn bullet
     * 
     * @return Posisi X tengah
     */
    public int getCenterX() {
        return x + width / 2;
    }

    /**
     * Ambil posisi Y tengah monster
     * Digunakan untuk spawn bullet
     * 
     * @return Posisi Y tengah
     */
    public int getCenterY() {
        return y;
    }

    /**
     * Reset cooldown tembakan
     * Dipanggil setelah monster menembak
     */
    public void resetShootCooldown() {
        this.lastShootTime = System.currentTimeMillis();
    }
}
