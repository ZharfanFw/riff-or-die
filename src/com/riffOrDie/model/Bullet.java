package com.riffOrDie.model;

import com.riffOrDie.util.GameConstants;

public class Bullet {
    private int x;
    private int y;
    private int width;
    private int height;

    private int speed;

    private boolean isPlayerBullet;

    public Bullet(int startX, int startY, boolean isPlayerBullet) {
        this.x = startX;
        this.y = startY;
        this.width = 5;
        this.height = 10;
        this.isPlayerBullet = isPlayerBullet;

        if (isPlayerBullet) {
            this.speed = GameConstants.PLAYER_BULLET_SPEED;
        } else {
            this.speed = GameConstants.MONSTER_BULLET_SPEED;
        }
    }

    public void update() {
        if (isPlayerBullet) {
            y += speed;
        } else {
            y -= speed;
        }
    }

    public boolean isActive() {
        return y > -height && y < GameConstants.SCREEN_HEIGHT + height;
    }

    public boolean isPlayerBullet() {
        return this.isPlayerBullet;
    }

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

    public int getSpeed() {
        return speed;
    }
}
