package com.riffOrDie.model;

import com.riffOrDie.util.GameConstants;

public class Monster {
    private int x;
    private int y;
    private int width;
    private int height;

    private MonsterType type;
    private int health;
    private int maxHealth;

    private long shootCooldown;
    private long lastShootTime;

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

    public void update() {
        y -= type.getSpeed();
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public boolean canShoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShootTime >= shootCooldown) {
            return true;
        }
        return false;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isOutOfBounds() {
        return y < -height;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public MonsterType getType() {
        return type;
    }

    // Get center x position
    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y;
    }
}
