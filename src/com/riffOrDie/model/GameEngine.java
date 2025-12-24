package com.riffOrDie.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.riffOrDie.util.GameConstants;

public class GameEngine {
    private Player player;
    private List<Monster> monsters;
    private List<Bullet> bullets;
    private List<Amplifier> amplifiers;

    private int score;
    private int bulletsFired;
    private int bulletsMissed;
    private String currentUsername;

    private long lastSpawnTime;
    private long spawnInterval;
    private Random random;

    public GameEngine(String username, int startingSisaPeluru) {
        this.currentUsername = username;

        // Initialize player di posisi tengah atas
        int playerStartX = GameConstants.SCREEN_WIDTH / 2 - GameConstants.PLAYER_WIDTH / 2;
        int playerStartY = GameConstants.SCREEN_HEIGHT - 100;
        this.player = new Player(playerStartX, playerStartY);

        // Initialize collections
        this.monsters = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.amplifiers = new ArrayList<>();

        // Initialize game state
        this.score = 0;
        this.bulletsFired = 0;
        this.bulletsMissed = 0;
        this.lastSpawnTime = System.currentTimeMillis();
        this.spawnInterval = GameConstants.BASE_SPAWN_RATE;
        this.random = new Random();

        initializeAmplifiers();
    }

    private void initializeAmplifiers() {
        int count = GameConstants.AMPLIFIER_COUNT_MIN
                + random.nextInt(GameConstants.AMPLIFIER_COUNT_MAX - GameConstants.AMPLIFIER_COUNT_MIN + 1);

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(GameConstants.SCREEN_WIDTH - GameConstants.AMPLIFIER_WIDTH);
            int y = 100 + random.nextInt(GameConstants.SCREEN_HEIGHT / 2);
            amplifiers.add(new Amplifier(x, y));
        }

    }

    public void update() {
        player.update();

        for (Monster monster : monsters) {
            monster.update();
        }

        for (Bullet bullet : bullets) {
            bullet.update();
        }

        spawnMonsters();

        checkCollisons();

        List<Monster> deadMonsters = new ArrayList<>();
        for (Monster m : monsters) {
            if (!m.isAlive() || m.isOutOfBounds()) {
                deadMonsters.add(m);
            }
        }
        monsters.removeAll(deadMonsters);

        List<Bullet> inactiveBullets = new ArrayList<>();
        for (Bullet b : bullets) {
            if (!b.isActive()) {
                inactiveBullets.add(b);
            }
        }
        bullets.removeAll(inactiveBullets);
    }

    private void spawnMonsters() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastSpawnTime >= spawnInterval) {
            lastSpawnTime = currentTime;

            int spawnCount = 1 + random.nextInt(2);
            for (int i = 0; i < spawnCount; i++) {
                int x = random.nextInt(GameConstants.SCREEN_WIDTH - GameConstants.MONSTER_WIDTH);
                int y = GameConstants.SCREEN_HEIGHT + GameConstants.MONSTER_HEIGHT;

                MonsterType type = random.nextDouble() < 0.7 ? MonsterType.EASY : MonsterType.HARD;

                monsters.add(new Monster(x, y, type));
            }

            spawnInterval = Math.max(500, GameConstants.BASE_SPAWN_RATE - (score / 1000) * 100);
        }
    }

    private void checkCollisons() {
        // Check Player Bullet vs Monster
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.isPlayerBullet()) {
                for (Monster monster : monsters) {
                    if (checkRectangleCollision(bullet, monster)) {
                        monster.takeDamage(1);
                        if (!monster.isAlive()) {
                            score += monster.getType().getScore();
                        }

                        bulletsToRemove.add(bullet);
                    }
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        // Check Bullet vs Amplifier
        bulletsToRemove.clear();

        for (Bullet bullet : bullets) {
            for (Amplifier amplifier : amplifiers) {
                if (amplifier.collidesWith(bullet)) {
                    bulletsMissed++;
                    bulletsToRemove.add(bullet);
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        // Check Monster vs Player
        for (Monster monster : monsters) {
            if (checkRectangleCollision(monster, player)) {
                player.takeDamage(1);
                monster.takeDamage(monster.getHealth());
            }
        }

        // Check Monster Bullet vs Player
        bulletsToRemove.clear();

        for (Bullet bullet : bullets) {
            if (!bullet.isPlayerBullet()) {
                if (checkRectangleCollision(bullet, player)) {
                    player.takeDamage(1);
                    bulletsToRemove.add(bullet);
                }
            }
        }
        bullets.removeAll(bulletsToRemove);
    }

    private boolean checkRectangleCollision(Object obj1, Object obj2) {
        int x1, y1, w1, h1;
        int x2, y2, w2, h2;

        if (obj1 instanceof Bullet) {
            Bullet b = (Bullet) obj1;
            x1 = b.getX();
            y1 = b.getY();
            w1 = b.getWidth();
            h1 = b.getHeight();
        } else if (obj1 instanceof Monster) {
            Monster m = (Monster) obj1;
            x1 = m.getX();
            y1 = m.getY();
            w1 = m.getWidth();
            h1 = m.getHeight();
        } else {
            Player p = (Player) obj1;
            x1 = p.getX();
            y1 = p.getY();
            w1 = p.getWidth();
            h1 = p.getHeight();
        }

        if (obj2 instanceof Bullet) {
            Bullet b = (Bullet) obj2;
            x2 = b.getX();
            y2 = b.getY();
            w2 = b.getWidth();
            h2 = b.getHeight();
        } else if (obj2 instanceof Monster) {
            Monster m = (Monster) obj2;
            x2 = m.getX();
            y2 = m.getY();
            w2 = m.getWidth();
            h2 = m.getHeight();
        } else {
            Player p = (Player) obj2;
            x2 = p.getX();
            y2 = p.getY();
            w2 = p.getWidth();
            h2 = p.getHeight();
        }

        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    public void playerShoot() {
        int centerX = player.getCenterX();
        int centerY = player.getCenterY();
        bullets.add(new Bullet(centerX, centerY, true));
        bulletsFired++;
    }

    public void updateMonsterShooting() {
        for (Monster monster : monsters) {
            if (monster.canShoot()) {
                int centerX = monster.getCenterX();
                int centerY = monster.getCenterY();
                bullets.add(new Bullet(centerX, centerY, false));
            }
        }
    }

    public boolean isGameOver() {
        return !player.isAlive();
    }

    // Getters
    public Player getPlayer() {
        return player;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Amplifier> getAmplifiers() {
        return amplifiers;
    }

    public int getScore() {
        return score;
    }

    public int getBulletsFired() {
        return bulletsFired;
    }

    public int getBulletsMissed() {
        return bulletsMissed;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }
}
