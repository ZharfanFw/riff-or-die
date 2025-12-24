package com.riffOrDie.util;

public class GameConstants {
    // Screen Setting
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int FPS = 60;
    public static final long FRAME_TIME = 1000 / FPS; // ~16ms per frame

    // Player Setting
    public static final int PLAYER_WIDTH = 29;
    public static final int PLAYER_HEIGHT = 34;
    public static final int PLAYER_HEALTH = 5;
    public static final int PLAYER_SPEED = 5; // px per frame
    public static final int PLAYER_BULLET_SPEED = 8;

    // Monster Setting
    public static final int MONSTER_WIDTH = 50;
    public static final int MONSTER_HEIGHT = 50;
    public static final int MONSTER_BULLET_SPEED = 4;
    public static final long MONSTER_SHOOT_COOLDOWN = 1000; // milisecond

    // Amplifier (Obstacle) Setting
    public static final int AMPLIFIER_WIDTH = 40;
    public static final int AMPLIFIER_HEIGHT = 40;
    public static final int AMPLIFIER_COUNT_MIN = 2;
    public static final int AMPLIFIER_COUNT_MAX = 5;

    // Spawning Setting
    public static final long BASE_SPAWN_RATE = 2000; // milisecond

    // Database Setting
    public static final String DB_URL = "jdbc:mysql://localhost:3306/riffordie";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "1234";
    public static final String DB_TABLE = "tbenefit";
}
