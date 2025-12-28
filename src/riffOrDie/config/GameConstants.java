package riffOrDie.config;

/**
 * GAMECONSTANTS - Pusat semua konstanta game
 * 
 * Fungsi:
 * - Menyimpan semua nilai konfigurasi: speed, ukuran, volume, timings
 * - Single source of truth untuk game balance
 * - Tidak ada magic numbers di code lain
 * 
 * Benefit:
 * - Ubah satu nilai di sini, semua otomatis update
 * - Mudah tuning game balance
 * - Code lebih maintainable
 */
public class GameConstants {

    // ===================== SCREEN SETTING =====================

    /** Lebar window game (pixel) */
    public static final int SCREEN_WIDTH = 800;

    /** Tinggi window game (pixel) */
    public static final int SCREEN_HEIGHT = 600;

    /** Target frame rate per detik */
    public static final int FPS = 60;

    /** Durasi per frame dalam milidetik (~16ms untuk 60 FPS) */
    public static final long FRAME_TIME = 1000 / FPS;

    // ===================== PLAYER SETTING =====================

    /** Lebar hitbox player (pixel) */
    public static final int PLAYER_WIDTH = 58;

    /** Tinggi hitbox player (pixel) */
    public static final int PLAYER_HEIGHT = 64;

    /** HP awal player */
    public static final int PLAYER_HEALTH = 5;

    /** Kecepatan gerak player (pixel per detik) */
    public static final double PLAYER_SPEED = 260.0;

    /** Kecepatan bullet player (pixel per detik) */
    public static final double PLAYER_BULLET_SPEED = 320.0;

    /** Offset X spawn bullet player (agar spawn di tengah badan) */
    public static final int PLAYER_BULLET_SPAWN_OFFSET_X = -18;

    // ===================== MONSTER SETTING =====================

    /** Lebar hitbox monster (pixel) */
    public static final int MONSTER_WIDTH = 58;

    /** Tinggi hitbox monster (pixel) */
    public static final int MONSTER_HEIGHT = 56;

    /** Kecepatan bullet monster (pixel per detik) */
    public static final double MONSTER_BULLET_SPEED = 220.0;

    /** Cooldown waktu antar tembakan monster (milidetik) */
    public static final long MONSTER_SHOOT_COOLDOWN = 2000;

    // ===================== AMPLIFIER SETTING =====================

    /** Lebar hitbox amplifier (pixel) */
    public static final int AMPLIFIER_WIDTH = 78;

    /** Tinggi hitbox amplifier (pixel) */
    public static final int AMPLIFIER_HEIGHT = 60;

    /** HP setiap amplifier */
    public static final int AMPLIFIER_HEALTH = 3;

    /** Interval waktu untuk regenerate amplifier (milidetik) */
    public static final long AMPLIFIER_REGENERATE_INTERVAL = 8000;

    /** Jumlah minimum amplifier yang spawn */
    public static final int AMPLIFIER_COUNT_MIN = 3;

    /** Jumlah maksimum amplifier yang spawn */
    public static final int AMPLIFIER_COUNT_MAX = 5;

    /** Posisi Y minimum untuk spawn amplifier (pixel) */
    public static final int AMPLIFIER_SPAWN_Y_MIN = 200;

    /** Posisi Y maksimum untuk spawn amplifier (pixel) */
    public static final int AMPLIFIER_SPAWN_Y_MAX = 400;

    // ===================== BULLET SETTING =====================

    /** Lebar hitbox bullet (pixel) */
    public static final int BULLET_WIDTH = 5;

    /** Tinggi hitbox bullet (pixel) */
    public static final int BULLET_HEIGHT = 10;

    // ===================== MONSTER EASY SETTING =====================

    /** Lebar monster easy (pixel) */
    public static final int MONSTER_EASY_WIDTH = 36;

    /** Tinggi monster easy (pixel) */
    public static final int MONSTER_EASY_HEIGHT = 56;

    /** HP monster easy */
    public static final int MONSTER_EASY_HEALTH = 1;

    /** Score didapat saat kill monster easy */
    public static final int MONSTER_EASY_SCORE = 100;

    // ===================== MONSTER HARD SETTING =====================

    /** Lebar monster hard (pixel) */
    public static final int MONSTER_HARD_WIDTH = 58;

    /** Tinggi monster hard (pixel) */
    public static final int MONSTER_HARD_HEIGHT = 56;

    /** HP monster hard */
    public static final int MONSTER_HARD_HEALTH = 2;

    /** Score didapat saat kill monster hard */
    public static final int MONSTER_HARD_SCORE = 200;

    // ===================== MONSTER SPAWN SETTING =====================

    /** Posisi Y minimum untuk spawn monster (pixel) */
    public static final int MONSTER_SPAWN_Y_MIN = 300;

    /** Posisi Y maksimum untuk spawn monster (pixel) */
    public static final int MONSTER_SPAWN_Y_MAX = 544;

    // ===================== WAVE SYSTEM SETTING =====================

    /** Score maksimum untuk Wave 0 */
    public static final int WAVE_0_MAX_SCORE = 2500;

    /** Score maksimum untuk Wave 1 */
    public static final int WAVE_1_MAX_SCORE = 5500;

    /** Score maksimum untuk Wave 2 */
    public static final int WAVE_2_MAX_SCORE = 8000;

    /** Spawn rate untuk Wave 0 (milidetik) */
    public static final long WAVE_0_SPAWN_RATE = 6000;

    /** Spawn rate untuk Wave 1 (milidetik) */
    public static final long WAVE_1_SPAWN_RATE = 5000;

    /** Default spawn rate (milidetik) */
    public static final long BASE_SPAWN_RATE = 5000;

    /** Spawn rate untuk Wave 2 (milidetik) */
    public static final long WAVE_2_SPAWN_RATE = 4000;

    /** Spawn rate untuk Wave 3+ (milidetik) */
    public static final long WAVE_3_SPAWN_RATE = 3000;

    // ===================== WAVE NOTIFICATION SETTING =====================

    /** Durasi tampilan notifikasi wave (milidetik) */
    public static final long WAVE_NOTIFICATION_DURATION = 2000;

    /** Ukuran font untuk notifikasi wave */
    public static final int WAVE_NOTIFICATION_FONT_SIZE = 72;

    // ===================== HUD SETTING =====================

    /** Posisi X untuk display wave info di HUD (pixel) */
    public static final int WAVE_HUD_X = 700;

    /** Posisi Y untuk display wave info di HUD (pixel) */
    public static final int WAVE_HUD_Y = 40;

    // ===================== AUDIO SETTING =====================

    /** Path file audio untuk tembakan player */
    public static final String AUDIO_PLAYER_SHOOT = "src/assets/bullet-player-sound.wav";

    /** Path file audio untuk tembakan monster */
    public static final String AUDIO_MONSTER_SHOOT = "src/assets/bullet-monster-sound.wav";

    /** Path file audio untuk amplifier terkena tembakan */
    public static final String AUDIO_AMPLIFIER_HIT = "src/assets/amplifier-hit-sound.wav";

    /** Path file audio untuk background music */
    public static final String AUDIO_BACKGROUND_MUSIC = "src/assets/backsound.wav";

    /** Volume audio tembakan player (0.0 - 1.0) */
    public static final float AUDIO_PLAYER_SHOOT_VOLUME = 1.0f;

    /** Volume audio tembakan monster (0.0 - 1.0) */
    public static final float AUDIO_MONSTER_SHOOT_VOLUME = 0.5f;

    /** Volume audio amplifier hit (0.0 - 1.0) */
    public static final float AUDIO_AMPLIFIER_HIT_VOLUME = 0.8f;

    /** Volume background music (0.0 - 1.0) */
    public static final float AUDIO_BACKGROUND_MUSIC_VOLUME = 0.4f;

    /** Frekuensi suara tembakan monster (30% chance play) */
    public static final int AUDIO_MONSTER_SHOOT_FREQUENCY = 30;

    // ===================== DATABASE SETTING =====================

    /** URL koneksi ke database MySQL */
    public static final String DB_URL = "jdbc:mysql://localhost:3306/riffordie";

    /** Username database */
    public static final String DB_USER = "root";

    /** Password database */
    public static final String DB_PASSWORD = "1234";

    /** Nama tabel untuk menyimpan data skor */
    public static final String DB_TABLE = "tbenefit";
}
