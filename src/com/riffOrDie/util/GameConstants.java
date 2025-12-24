package com.riffOrDie.util;

public class GameConstants {
    // Screen Setting
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int FPS = 60;
    public static final long FRAME_TIME = 1000 / FPS; // ~16ms per frame

    // Player Setting
    public static final int PLAYER_WIDTH = 58;
    public static final int PLAYER_HEIGHT = 68;
    public static final int PLAYER_HEALTH = 5;
    public static final double PLAYER_SPEED = 180.0; // px per second
    public static final double PLAYER_BULLET_SPEED = 240.0; // px per second

    // Monster Setting
    public static final int MONSTER_WIDTH = 50;
    public static final int MONSTER_HEIGHT = 50;
    public static final double MONSTER_BULLET_SPEED = 150.0; // px per second
    public static final long MONSTER_SHOOT_COOLDOWN = 2500; // 2.5 seconds // milisecond

    // Amplifier (Obstacle) Setting
    public static final int AMPLIFIER_WIDTH = 40;
    public static final int AMPLIFIER_HEIGHT = 40;
    public static final int AMPLIFIER_COUNT_MIN = 2;
    public static final int AMPLIFIER_COUNT_MAX = 5;

    // Spawning Setting
    public static final long BASE_SPAWN_RATE = 5000; // 4 seconds // milisecond

    // Database Setting
    public static final String DB_URL = "jdbc:mysql://localhost:3306/riffordie";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "1234";
    public static final String DB_TABLE = "tbenefit";
}
