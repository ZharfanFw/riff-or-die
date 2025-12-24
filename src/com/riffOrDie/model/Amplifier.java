package com.riffOrDie.model;

import com.riffOrDie.util.GameConstants;

public class Amplifier {
    private int x;
    private int y;
    private int width;
    private int height;

    // Constructors
    public Amplifier(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = GameConstants.AMPLIFIER_WIDTH;
        this.height = GameConstants.AMPLIFIER_HEIGHT;
    }

    public boolean collidesWith(Bullet bullet) {
        boolean collidingX = x < bullet.getX() + bullet.getWidth() && x + width > bullet.getX();
        boolean collidingY = y < bullet.getY() + bullet.getHeight() && y + height > bullet.getY();

        return collidingX && collidingY;
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
}
