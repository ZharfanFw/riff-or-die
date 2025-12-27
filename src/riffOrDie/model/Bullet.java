package riffOrDie.model;

/**
 * BULLET - Entity proyektil
 * 
 * Fungsi:
 * - Posisi, velocity (velocityX, velocityY)
 * - isPlayerBullet() check
 * - getAngle() untuk visual rotation
 * - Collision detection (AABB)
 * 
 * Key: Velocity-based movement untuk smooth tracking
 */
import riffOrDie.config.GameConstants;

public class Bullet {
    private int x;
    private int y;
    private int width;
    private int height;

    private double speed;
    private double velocityX;
    private double velocityY;

    private boolean isPlayerBullet;

    public Bullet(int startX, int startY, boolean isPlayerBullet) {
        this.x = startX;
        this.y = startY;
        this.width = 5;
        this.height = 10;
        this.isPlayerBullet = isPlayerBullet;
        this.velocityX = 0;
        this.velocityY = 0;

        if (isPlayerBullet) {
            this.speed = GameConstants.PLAYER_BULLET_SPEED;
        } else {
            this.speed = GameConstants.MONSTER_BULLET_SPEED;
        }
    }

    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public void update(double deltaTime) {
        if (isPlayerBullet) {
            y += (int) (speed * deltaTime);
        } else {
            // Monster bullet moves with velocity (smooth lerp towards player)
            x += (int) (velocityX * speed * deltaTime);
            y += (int) (velocityY * speed * deltaTime);
        }
    }

    public boolean isActive() {
        return y > -height && y < GameConstants.SCREEN_HEIGHT + height &&
                x > -width && x < GameConstants.SCREEN_WIDTH + width;
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

    public double getSpeed() {
        return speed;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Hitung angle rotasi bullet berdasarkan arah pergerakan
     * Returns angle dalam radians untuk AffineTransform rotation
     */
    public double getAngle() {
        return Math.atan2(velocityY, velocityX);
    }
}
