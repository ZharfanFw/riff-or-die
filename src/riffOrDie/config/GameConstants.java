package riffOrDie.config;

public class GameConstants {
    // Screen Setting
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int FPS = 60;
    public static final long FRAME_TIME = 1000 / FPS; // ~16ms per frame

    // Player Setting
    public static final int PLAYER_WIDTH = 58;
    public static final int PLAYER_HEIGHT = 64;
    public static final int PLAYER_HEALTH = 5;
    public static final double PLAYER_SPEED = 230.0; // px per second
    public static final double PLAYER_BULLET_SPEED = 320.0; // px per second (increased for faster travel)

    // Monster Setting
    public static final int MONSTER_WIDTH = 58;
    public static final int MONSTER_HEIGHT = 56;
    public static final double MONSTER_BULLET_SPEED = 200.0; // px per second (increased from 170)
    public static final long MONSTER_SHOOT_COOLDOWN = 1800; // 1.8 seconds (decreased from 2500 for balance)

    // Amplifier (Obstacle) Setting
    public static final int AMPLIFIER_WIDTH = 78;
    public static final int AMPLIFIER_HEIGHT = 60;
    public static final int AMPLIFIER_HEALTH = 3; // health points per amplifier
    public static final long AMPLIFIER_REGENERATE_INTERVAL = 8000; // 8 seconds in milliseconds
    public static final int AMPLIFIER_COUNT_MIN = 3;
    public static final int AMPLIFIER_COUNT_MAX = 5;
    public static final int AMPLIFIER_SPAWN_Y_MIN = 200; // amplifier spawn Y range min
    public static final int AMPLIFIER_SPAWN_Y_MAX = 400; // amplifier spawn Y range max

    // Monster Spawn Setting
    public static final int MONSTER_SPAWN_Y_MIN = 300; // monster spawn Y range min
    public static final int MONSTER_SPAWN_Y_MAX = 544; // monster spawn Y range max (600 - 56 height)

    // Spawning Setting
    public static final long BASE_SPAWN_RATE = 5000; // 5 seconds (old default)

    // Wave System Setting - Score Thresholds
    public static final int WAVE_0_MAX_SCORE = 2500;
    public static final int WAVE_1_MAX_SCORE = 5500;
    public static final int WAVE_2_MAX_SCORE = 8000;
    // Wave 3 starts at 8000+

    // Wave System Setting - Spawn Rates
    public static final long WAVE_0_SPAWN_RATE = 6000; // 6 seconds
    public static final long WAVE_1_SPAWN_RATE = 5000; // 5 seconds
    public static final long WAVE_2_SPAWN_RATE = 4000; // 4 seconds
    public static final long WAVE_3_SPAWN_RATE = 3000; // 3 seconds

    // HUD Setting
    public static final int WAVE_HUD_X = 700;
    public static final int WAVE_HUD_Y = 30;

    // Database Setting
    public static final String DB_URL = "jdbc:mysql://localhost:3306/riffordie";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "1234";
    public static final String DB_TABLE = "tbenefit";
}
