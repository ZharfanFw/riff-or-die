package riffOrDie.model;

/**
 * MONSTERTYPE - Enum tipe monster
 * 
 * Fungsi:
 * - Define EASY dan HARD monster
 * - Simpan stats: health, score, width, height
 * - Getter methods
 * 
 * Pattern: Enum dengan data untuk cleaner code
 */
import riffOrDie.config.GameConstants;

public enum MonsterType {
    /** Monster easy - HP 1, Score 100 */
    EASY(GameConstants.MONSTER_WIDTH, GameConstants.MONSTER_HEIGHT, 2, 1, 100),
    
    /** Monster hard - HP 2, Score 200 */
    HARD(GameConstants.MONSTER_WIDTH, GameConstants.MONSTER_HEIGHT, 5, 2, 200);

    /** Lebar monster */
    private int width;
    
    /** Tinggi monster */
    private int height;
    
    /** Kecepatan monster (tidak digunakan saat ini) */
    private int speed;
    
    /** Health monster */
    private int health;
    
    /** Score didapat saat kill monster */
    private int score;

    /**
     * Constructor enum - Simpan data untuk setiap tipe monster
     * 
     * @param width Lebar monster
     * @param height Tinggi monster
     * @param speed Kecepatan monster
     * @param health Health monster
     * @param score Score saat kill
     */
    MonsterType(int width, int height, int speed, int health, int score) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = health;
        this.score = score;
    }

    /**
     * Ambil lebar monster
     * 
     * @return Lebar dalam pixel
     */
    public int getWidth() {
        return width;
    }

    /**
     * Ambil tinggi monster
     * 
     * @return Tinggi dalam pixel
     */
    public int getHeight() {
        return height;
    }

    /**
     * Ambil kecepatan monster
     * 
     * @return Kecepatan
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Ambil health monster
     * 
     * @return Health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Ambil score monster
     * 
     * @return Score didapat saat kill
     */
    public int getScore() {
        return score;
    }
}
