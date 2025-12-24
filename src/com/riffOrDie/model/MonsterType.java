package com.riffOrDie.model;

import com.riffOrDie.util.GameConstants;

public enum MonsterType {
    EASY(GameConstants.MONSTER_WIDTH, GameConstants.MONSTER_HEIGHT, 2, 1, 100),
    HARD(GameConstants.MONSTER_WIDTH, GameConstants.MONSTER_HEIGHT, 5, 2, 200);

    private int width;
    private int height;
    private int speed;
    private int health;
    private int score;

    MonsterType(int width, int height, int speed, int health, int score) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = health;
        this.score = score;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public int getScore() {
        return score;
    }
}
