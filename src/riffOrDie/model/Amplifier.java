package riffOrDie.model;

import riffOrDie.config.GameConstants;

public class Amplifier {
    private int x;
    private int y;
    private int width;
    private int height;
    private int health;

    // Constructors
    public Amplifier(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = GameConstants.AMPLIFIER_WIDTH;
        this.height = GameConstants.AMPLIFIER_HEIGHT;
        this.health = GameConstants.AMPLIFIER_HEALTH;
    }

    public boolean collidesWith(Bullet bullet) {
        boolean collidingX = x < bullet.getX() + bullet.getWidth() && x + width > bullet.getX();
        boolean collidingY = y < bullet.getY() + bullet.getHeight() && y + height > bullet.getY();

        return collidingX && collidingY;
    }

    /**
     * Take damage to this amplifier
     * @param damage Amount of damage to take
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    /**
     * Check if amplifier is still alive
     * @return true if health > 0, false otherwise
     */
    public boolean isAlive() {
        return this.health > 0;
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
}
