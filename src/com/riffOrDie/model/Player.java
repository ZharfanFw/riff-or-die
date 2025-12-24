package com.riffOrDie.model;

import com.riffOrDie.util.GameConstants;

public class Player {
    private int x;
    private int y;
    private int width;
    private int height;

    private int health;
    private int maxHealth;

    private int velocityX;
    private int velocityY;

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

    public void update() {
        x += velocityX * GameConstants.PLAYER_SPEED;
        y += velocityY * GameConstants.PLAYER_SPEED;

        if (x < 0) {
            x = 0;
        }
        if (x + width > GameConstants.SCREEN_WIDTH) {
            x = GameConstants.SCREEN_WIDTH - width;
        }

        if (y < 0) {
            y = 0;
        }
        if (y + height > GameConstants.SCREEN_HEIGHT) {
            y = GameConstants.SCREEN_HEIGHT - height;
        }
    }

    public void setVelocity(int vx, int vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public boolean isAlive() {
        return health > 0;
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

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    // Get center x position
    public int getCenterX() {
        return x + width / 2;
    }

    public int getCenterY() {
        return y;
    }
}
