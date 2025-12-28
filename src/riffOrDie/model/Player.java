package riffOrDie.model;

/**
 * PLAYER - Entity pemain
 * 
 * Fungsi:
 * - Simpan posisi, health, ammo
 * - Move method untuk update posisi
 * - Collision detection (AABB)
 * - Ammo management (use, add, get)
 */
import riffOrDie.config.GameConstants;

public class Player {
    /** Posisi X player */
    private int x;
    
    /** Posisi Y player */
    private int y;
    
    /** Lebar hitbox player */
    private int width;
    
    /** Tinggi hitbox player */
    private int height;

    /** Health player saat ini */
    private int health;
    
    /** Health maksimum player */
    private int maxHealth;

    /** Velocity arah X (-1, 0, 1) */
    private int velocityX;
    
    /** Velocity arah Y (-1, 0, 1) */
    private int velocityY;

    /** Jumlah ammo player */
    private int ammo = 0;

    /**
     * Constructor - Buat player baru di posisi spesifik
     * 
     * @param startX Posisi X awal
     * @param startY Posisi Y awal
     */
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = GameConstants.PLAYER_WIDTH;
        this.height = GameConstants.PLAYER_HEIGHT;
        this.health = GameConstants.PLAYER_HEALTH;
        this.maxHealth = GameConstants.PLAYER_HEALTH;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    /**
     * Update posisi player setiap frame
     * Menggunakan velocity untuk movement
     * Menangani boundary checking (batas layar)
     * 
     * @param deltaTime Delta time (detik) sejak frame terakhir
     */
    public void update(double deltaTime) {
        x += (int) (velocityX * GameConstants.PLAYER_SPEED * deltaTime);
        y += (int) (velocityY * GameConstants.PLAYER_SPEED * deltaTime);

        // Boundary checking X
        if (x < 0) {
            x = 0;
        }
        if (x + width > GameConstants.SCREEN_WIDTH) {
            x = GameConstants.SCREEN_WIDTH - width;
        }

        // Boundary checking Y
        if (y < 0) {
            y = 0;
        }
        if (y + height > GameConstants.SCREEN_HEIGHT) {
            y = GameConstants.SCREEN_HEIGHT - height;
        }
    }

    /**
     * Set velocity arah player
     * -1 (kiri/atas), 0 (tidak gerak), 1 (kanan/bawah)
     * 
     * @param vx Velocity X (-1, 0, 1)
     * @param vy Velocity Y (-1, 0, 1)
     */
    public void setVelocity(int vx, int vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    /**
     * Ambil damage untuk player ini
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
     * Cek apakah player masih hidup
     * 
     * @return true jika health > 0, false jika health <= 0
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Ambil posisi X player
     * 
     * @return Posisi X dalam pixel
     */
    public int getX() {
        return x;
    }

    /**
     * Ambil posisi Y player
     * 
     * @return Posisi Y dalam pixel
     */
    public int getY() {
        return y;
    }

    /**
     * Ambil lebar hitbox player
     * 
     * @return Lebar dalam pixel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Ambil tinggi hitbox player
     * 
     * @return Tinggi dalam pixel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Ambil health player saat ini
     * 
     * @return Health player
     */
    public int getHealth() {
        return health;
    }

    /**
     * Ambil health maksimum player
     * 
     * @return Health maksimum
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Ambil velocity arah X
     * 
     * @return Velocity X (-1, 0, 1)
     */
    public int getVelocityX() {
        return velocityX;
    }

    /**
     * Ambil velocity arah Y
     * 
     * @return Velocity Y (-1, 0, 1)
     */
    public int getVelocityY() {
        return velocityY;
    }

    /**
     * Ambil posisi X tengah player
     * Digunakan untuk spawn bullet
     * 
     * @return Posisi X tengah
     */
    public int getCenterX() {
        return x + width / 2;
    }

    /**
     * Ambil posisi Y tengah player
     * Digunakan untuk spawn bullet
     * 
     * @return Posisi Y tengah
     */
    public int getCenterY() {
        return y;
    }

    /**
     * Ambil jumlah ammo player
     * 
     * @return Jumlah ammo
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Set posisi X player
     * Digunakan oleh GameEngine untuk collision handling
     * 
     * @param x Posisi X baru
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set posisi Y player
     * Digunakan oleh GameEngine untuk collision handling
     * 
     * @param y Posisi Y baru
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Tambah ammo ke player
     * Dipanggil saat monster bullet mengenai amplifier atau keluar layar
     * 
     * @param amount Jumlah ammo yang ditambahkan
     */
    public void addAmmo(int amount) {
        ammo += amount;
    }

    /**
     * Gunakan 1 ammo untuk menembak
     * Hanya mengurangi jika ammo > 0
     */
    public void useAmmo() {
        if (ammo > 0) {
            ammo--;
        }
    }

    /**
     * Set jumlah ammo player
     * Digunakan saat load game dari database
     * 
     * @param amount Jumlah ammo baru
     */
    public void setAmmo(int amount) {
        ammo = amount;
    }
}
