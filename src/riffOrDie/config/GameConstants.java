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
    public static final int SCREEN_WIDTH = 800;  // Lebar window (px)
    public static final int SCREEN_HEIGHT = 600;  // Tinggi window (px)
    public static final int FPS = 60;            // Target frame rate
    public static final long FRAME_TIME = 1000 / FPS; // ~16ms per frame

    // ===================== PLAYER SETTING =====================
    public static final int PLAYER_WIDTH = 58;  // Lebar hitbox player (px)
    public static final int PLAYER_HEIGHT = 64; // Tinggi hitbox player (px)
    public static final int PLAYER_HEALTH = 5;  // HP awal player
    public static final double PLAYER_SPEED = 260.0; // Kecepatan gerak (px per detik)
    public static final double PLAYER_BULLET_SPEED = 320.0; // Kecepatan bullet (px per detik)
    public static final int PLAYER_BULLET_SPAWN_OFFSET_X = -18; // Offset bullet spawn (center body)

    // ===================== MONSTER SETTING =====================
    public static final int MONSTER_WIDTH = 58;  // Lebar hitbox monster (px)
    public static final int MONSTER_HEIGHT = 56; // Tinggi hitbox monster (px)
    public static final double MONSTER_BULLET_SPEED = 220.0; // Kecepatan bullet monster (px per detik)
    public static final long MONSTER_SHOOT_COOLDOWN = 2000; // Cooldown tembak (ms)

    // ===================== AMPLIFIER SETTING =====================
    public static final int AMPLIFIER_WIDTH = 78;  // Lebar hitbox (px)
    public static final int AMPLIFIER_HEIGHT = 60; // Tinggi hitbox (px)
    public static final int AMPLIFIER_HEALTH = 3;   // HP setiap amplifier
    public static final long AMPLIFIER_REGENERATE_INTERVAL = 8000; // Interval regen (ms)
    public static final int AMPLIFIER_COUNT_MIN = 3; // Min jumlah amplifier
    public static final int AMPLIFIER_COUNT_MAX = 5; // Max jumlah amplifier
    public static final int AMPLIFIER_SPAWN_Y_MIN = 200; // Range spawn Y min
    public static final int AMPLIFIER_SPAWN_Y_MAX = 400; // Range spawn Y max

    // ===================== BULLET SETTING =====================
    public static final int BULLET_WIDTH = 5;   // Lebar hitbox bullet (px)
    public static final int BULLET_HEIGHT = 10;  // Tinggi hitbox bullet (px)

    // ===================== MONSTER EASY SETTING =====================
    public static final int MONSTER_EASY_WIDTH = 36;  // Lebar monster easy (px)
    public static final int MONSTER_EASY_HEIGHT = 56; // Tinggi monster easy (px)
    public static final int MONSTER_EASY_HEALTH = 1;   // HP monster easy
    public static final int MONSTER_EASY_SCORE = 100;  // Score per kill

    // ===================== MONSTER HARD SETTING =====================
    public static final int MONSTER_HARD_WIDTH = 58;  // Lebar monster hard (px)
    public static final int MONSTER_HARD_HEIGHT = 56; // Tinggi monster hard (px)
    public static final int MONSTER_HARD_HEALTH = 2;   // HP monster hard
    public static final int MONSTER_HARD_SCORE = 200;  // Score per kill

    // ===================== MONSTER SPAWN SETTING =====================
    public static final int MONSTER_SPAWN_Y_MIN = 300;  // Range spawn Y min (px)
    public static final int MONSTER_SPAWN_Y_MAX = 544;  // Range spawn Y max (px)

    // ===================== WAVE SYSTEM SETTING =====================
    public static final int WAVE_0_MAX_SCORE = 2500;  // Max score wave 0
    public static final int WAVE_1_MAX_SCORE = 5500;  // Max score wave 1
    public static final int WAVE_2_MAX_SCORE = 8000;  // Max score wave 2
    public static final long WAVE_0_SPAWN_RATE = 6000; // Spawn rate wave 0 (ms)
    public static final long WAVE_1_SPAWN_RATE = 5000; // Spawn rate wave 1 (ms)
    public static final long BASE_SPAWN_RATE = 5000;   // Default spawn rate (ms)
    public static final long WAVE_2_SPAWN_RATE = 4000; // Spawn rate wave 2 (ms)
    public static final long WAVE_3_SPAWN_RATE = 3000; // Spawn rate wave 3 (ms)

    // ===================== WAVE NOTIFICATION SETTING =====================
    public static final long WAVE_NOTIFICATION_DURATION = 2000; // Durasi notifikasi (ms)
    public static final int WAVE_NOTIFICATION_FONT_SIZE = 72;   // Ukuran font notifikasi

    // ===================== HUD SETTING =====================
    public static final int WAVE_HUD_X = 700; // Posisi X wave info HUD
    public static final int WAVE_HUD_Y = 40;  // Posisi Y wave info HUD

    // ===================== AUDIO SETTING =====================
    public static final String AUDIO_PLAYER_SHOOT = "src/assets/bullet-player-sound.wav";
    public static final String AUDIO_MONSTER_SHOOT = "src/assets/bullet-monster-sound.wav";
    public static final String AUDIO_AMPLIFIER_HIT = "src/assets/amplifier-hit-sound.wav";
    public static final String AUDIO_BACKGROUND_MUSIC = "src/assets/backsound.wav";
    public static final float AUDIO_PLAYER_SHOOT_VOLUME = 1.0f;
    public static final float AUDIO_MONSTER_SHOOT_VOLUME = 0.5f;
    public static final float AUDIO_AMPLIFIER_HIT_VOLUME = 0.8f;
    public static final float AUDIO_BACKGROUND_MUSIC_VOLUME = 0.4f;
    public static final int AUDIO_MONSTER_SHOOT_FREQUENCY = 30; // 30% chance play sound

    // ===================== DATABASE SETTING =====================
    public static final String DB_URL = "jdbc:mysql://localhost:3306/riffordie";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "1234";
    public static final String DB_TABLE = "tbenefit";
}
