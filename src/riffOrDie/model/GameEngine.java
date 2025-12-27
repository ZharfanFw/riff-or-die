package riffOrDie.model;

/**
 * GAMEENGINE - Core game logic
 * 
 * Fungsi:
 * - Game loop dengan delta time (frame-rate independent)
 * - Spawn monsters sesuai wave
 * - Collision detection semua entities
 * - Update score, wave progression, ammo tracking
 * - Call AudioManager untuk SFX
 * 
 * Pattern: Pure logic - tidak ada UI code di sini
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import riffOrDie.presenter.util.AudioManager;
import riffOrDie.config.GameConstants;

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
    private long lastUpdateTime;
    private long lastAmplifierRegenerateTime;
    private Random random;
    private int currentWave;

    private long lastWaveNotificationTime = 0;
    private int lastNotifiedWave = -1;

    public GameEngine(String username, int startingSisaPeluru) {
        this.currentUsername = username;

        // Initialize player di posisi center screen
        int playerStartX = GameConstants.SCREEN_WIDTH / 2 - GameConstants.PLAYER_WIDTH / 2;
        int playerStartY = GameConstants.SCREEN_HEIGHT / 2 - GameConstants.PLAYER_HEIGHT / 2;
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
        this.lastUpdateTime = System.currentTimeMillis();
        this.spawnInterval = GameConstants.BASE_SPAWN_RATE;
        this.random = new Random();
        this.currentWave = 1;
        this.lastAmplifierRegenerateTime = System.currentTimeMillis();

        initializeAmplifiers();
    }

    private void initializeAmplifiers() {
        int count = GameConstants.AMPLIFIER_COUNT_MIN
                + random.nextInt(GameConstants.AMPLIFIER_COUNT_MAX - GameConstants.AMPLIFIER_COUNT_MIN + 1);

        for (int i = 0; i < count; i++) {
            int x = -1;
            int y = -1;
            boolean validPosition = false;
            int retries = 0;
            int maxRetries = 10;

            // Find safe spawn position (not colliding with other amplifiers)
            while (!validPosition && retries < maxRetries) {
                x = random.nextInt(GameConstants.SCREEN_WIDTH - GameConstants.AMPLIFIER_WIDTH);
                y = GameConstants.AMPLIFIER_SPAWN_Y_MIN
                        + random.nextInt(GameConstants.AMPLIFIER_SPAWN_Y_MAX - GameConstants.AMPLIFIER_SPAWN_Y_MIN);

                validPosition = !checkAmplifierSpawnCollision(x, y) &&
                        !checkAmplifierPlayerCollision(x, y) &&
                        !checkAmplifierMonsterCollision(x, y);
                retries++;
            }

            // Only spawn if valid position found
            if (validPosition) {
                amplifiers.add(new Amplifier(x, y));
            }
        }

    }

    /**
     * Regenerate amplifiers every 7 seconds
     * Clears all amplifiers and spawns new ones to maintain 2-5 count
     */
    private void regenerateAmplifiers() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAmplifierRegenerateTime >= GameConstants.AMPLIFIER_REGENERATE_INTERVAL) {
            lastAmplifierRegenerateTime = currentTime;

            // Clear all amplifiers
            amplifiers.clear();

            // Spawn new batch
            int count = GameConstants.AMPLIFIER_COUNT_MIN
                    + random.nextInt(GameConstants.AMPLIFIER_COUNT_MAX - GameConstants.AMPLIFIER_COUNT_MIN + 1);

            for (int i = 0; i < count; i++) {
                int x = -1;
                int y = -1;
                boolean validPosition = false;
                int retries = 0;
                int maxRetries = 10;

                // Find safe spawn position (not colliding with other amplifiers)
                while (!validPosition && retries < maxRetries) {
                    x = random.nextInt(GameConstants.SCREEN_WIDTH - GameConstants.AMPLIFIER_WIDTH);
                    y = GameConstants.AMPLIFIER_SPAWN_Y_MIN
                            + random.nextInt(GameConstants.AMPLIFIER_SPAWN_Y_MAX - GameConstants.AMPLIFIER_SPAWN_Y_MIN);

                    validPosition = !checkAmplifierSpawnCollision(x, y) &&
                            !checkAmplifierPlayerCollision(x, y) &&
                            !checkAmplifierMonsterCollision(x, y);
                    retries++;
                }

                // Only spawn if valid position found
                if (validPosition) {
                    amplifiers.add(new Amplifier(x, y));
                }
            }
        }
    }

    public void update(double deltaTime) {
        updateWaveBasedOnScore();
        // Update player with collision detection against amplifiers
        updatePlayerWithCollisions(deltaTime);

        for (Monster monster : monsters) {
            monster.update(deltaTime);
        }

        for (Bullet bullet : bullets) {
            bullet.update(deltaTime);
        }

        spawnMonsters();

        regenerateAmplifiers();

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
                // Monster bullet went off-screen - add ammo to player
                if (!b.isPlayerBullet()) {
                    player.addAmmo(1);
                }
                inactiveBullets.add(b);
            }
        }
        bullets.removeAll(inactiveBullets);
    }

    /**
     * Update wave berdasarkan score saat ini
     * Score 0-2499: Wave 0 (spawn 6s)
     * Score 2500-5499: Wave 1 (spawn 5s)
     * Score 5500-7999: Wave 2 (spawn 4s)
     * Score 8000+: Wave 3 (spawn 3s)
     */
    private void updateWaveBasedOnScore() {
        if (score < GameConstants.WAVE_0_MAX_SCORE) {
            currentWave = 0;
            spawnInterval = GameConstants.WAVE_0_SPAWN_RATE;
        } else if (score < GameConstants.WAVE_1_MAX_SCORE) {
            currentWave = 1;
            spawnInterval = GameConstants.WAVE_1_SPAWN_RATE;
        } else if (score < GameConstants.WAVE_2_MAX_SCORE) {
            currentWave = 2;
            spawnInterval = GameConstants.WAVE_2_SPAWN_RATE;
        } else {
            currentWave = 3;
            spawnInterval = GameConstants.WAVE_3_SPAWN_RATE;
        }
    }

    private void spawnMonsters() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastSpawnTime >= spawnInterval) {
            lastSpawnTime = currentTime;

            int spawnCount = 1 + random.nextInt(2);
            for (int i = 0; i < spawnCount; i++) {
                int x = -1;
                int y = -1;
                boolean validPosition = false;
                int retries = 0;
                int maxRetries = 10;

                // Find safe spawn position (not colliding with amplifiers or other monsters)
                while (!validPosition && retries < maxRetries) {
                    x = random.nextInt(GameConstants.SCREEN_WIDTH - GameConstants.MONSTER_WIDTH);
                    y = GameConstants.MONSTER_SPAWN_Y_MIN
                            + random.nextInt(GameConstants.MONSTER_SPAWN_Y_MAX - GameConstants.MONSTER_SPAWN_Y_MIN);

                    // Check if this position collides with any amplifier or other monster
                    validPosition = true;
                    for (Amplifier amp : amplifiers) {
                        if (checkMonsterSpawnCollision(x, y, amp)) {
                            validPosition = false;
                            break;
                        }
                    }
                    if (validPosition && checkMonsterMonsterCollision(x, y)) {
                        validPosition = false;
                    }

                    retries++;
                }

                // Only spawn if valid position found
                if (validPosition) {
                    MonsterType type = random.nextDouble() < 0.7 ? MonsterType.EASY : MonsterType.HARD;
                    monsters.add(new Monster(x, y, type));
                }
            }

        }
    }

    /**
     * Check if amplifier spawn position collides with player
     */
    private boolean checkAmplifierPlayerCollision(int ampX, int ampY) {
        Player p = player;
        return ampX < p.getX() + p.getWidth() &&
                ampX + GameConstants.AMPLIFIER_WIDTH > p.getX() &&
                ampY < p.getY() + p.getHeight() &&
                ampY + GameConstants.AMPLIFIER_HEIGHT > p.getY();
    }

    /**
     * Check if amplifier spawn position collides with any monster
     */
    private boolean checkAmplifierMonsterCollision(int ampX, int ampY) {
        for (Monster m : monsters) {
            if (ampX < m.getX() + m.getWidth() &&
                    ampX + GameConstants.AMPLIFIER_WIDTH > m.getX() &&
                    ampY < m.getY() + m.getHeight() &&
                    ampY + GameConstants.AMPLIFIER_HEIGHT > m.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if spawn position (amplifier at x, y) collides with existing amplifiers
     * Uses AABB collision detection
     */
    private boolean checkAmplifierSpawnCollision(int ampX, int ampY) {
        for (Amplifier amp : amplifiers) {
            if (ampX < amp.getX() + amp.getWidth() &&
                    ampX + GameConstants.AMPLIFIER_WIDTH > amp.getX() &&
                    ampY < amp.getY() + amp.getHeight() &&
                    ampY + GameConstants.AMPLIFIER_HEIGHT > amp.getY()) {
                return true; // Collision detected
            }
        }
        return false; // No collision
    }

    /**
     * Check if spawn position (monster at x, y) collides with amplifier
     * Uses AABB collision detection
     */
    private boolean checkMonsterSpawnCollision(int monsterX, int monsterY, Amplifier amplifier) {
        return monsterX < amplifier.getX() + amplifier.getWidth() &&
                monsterX + GameConstants.MONSTER_WIDTH > amplifier.getX() &&
                monsterY < amplifier.getY() + amplifier.getHeight() &&
                monsterY + GameConstants.MONSTER_HEIGHT > amplifier.getY();
    }

    /**
     * Check if spawn position (monster at x, y) collides with any existing monster
     * Uses AABB collision detection
     */
    private boolean checkMonsterMonsterCollision(int monsterX, int monsterY) {
        for (Monster m : monsters) {
            if (monsterX < m.getX() + m.getWidth() &&
                    monsterX + GameConstants.MONSTER_WIDTH > m.getX() &&
                    monsterY < m.getY() + m.getHeight() &&
                    monsterY + GameConstants.MONSTER_HEIGHT > m.getY()) {
                return true; // Collision detected
            }
        }
        return false; // No collision
    }

    /**
     * Update player position with collision detection against amplifiers
     * Implements smart sliding: allows movement in one axis even if blocked in
     * another
     */
    private void updatePlayerWithCollisions(double deltaTime) {
        int currentX = player.getX();
        int currentY = player.getY();

        // Calculate new position with delta time
        int newX = currentX + (int) (player.getVelocityX() * GameConstants.PLAYER_SPEED * deltaTime);
        int newY = currentY + (int) (player.getVelocityY() * GameConstants.PLAYER_SPEED * deltaTime);

        // Check screen boundaries
        if (newX < 0)
            newX = 0;
        if (newX + player.getWidth() > GameConstants.SCREEN_WIDTH) {
            newX = GameConstants.SCREEN_WIDTH - player.getWidth();
        }
        if (newY < 0)
            newY = 0;
        if (newY + player.getHeight() > GameConstants.SCREEN_HEIGHT) {
            newY = GameConstants.SCREEN_HEIGHT - player.getHeight();
        }

        // Smart collision sliding: check X and Y movements independently
        boolean canMoveX = true;
        boolean canMoveY = true;

        // Check collision for X movement
        for (Amplifier amp : amplifiers) {
            if (newX < amp.getX() + amp.getWidth() &&
                    newX + player.getWidth() > amp.getX() &&
                    currentY < amp.getY() + amp.getHeight() &&
                    currentY + player.getHeight() > amp.getY()) {
                canMoveX = false;
                break;
            }
        }

        // Check collision for Y movement
        for (Amplifier amp : amplifiers) {
            if (currentX < amp.getX() + amp.getWidth() &&
                    currentX + player.getWidth() > amp.getX() &&
                    newY < amp.getY() + amp.getHeight() &&
                    newY + player.getHeight() > amp.getY()) {
                canMoveY = false;
                break;
            }
        }

        // Update position based on what's allowed
        if (canMoveX) {
            player.setX(newX);
        }
        if (canMoveY) {
            player.setY(newY);
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
                        AudioManager.playMonsterHit();
                        if (!monster.isAlive()) {
                            score += monster.getType().getScore();
                        }

                        bulletsToRemove.add(bullet);
                    }
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        // Check Player Bullet vs Amplifier (damage amplifier)
        bulletsToRemove.clear();

        for (Bullet bullet : bullets) {
            if (bullet.isPlayerBullet()) {
                for (Amplifier amplifier : amplifiers) {
                    if (amplifier.collidesWith(bullet)) {
                        amplifier.takeDamage(1);
                        AudioManager.playAmplifierHit();
                        bulletsMissed++;
                        bulletsToRemove.add(bullet);
                        break;
                    }
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        // Check Monster Bullet vs Amplifier (damage amplifier + add ammo)
        bulletsToRemove.clear();

        for (Bullet bullet : bullets) {
            if (!bullet.isPlayerBullet()) {
                for (Amplifier amplifier : amplifiers) {
                    if (amplifier.collidesWith(bullet)) {
                        amplifier.takeDamage(1);
                        // Monster bullet destroyed amplifier - add ammo to player
                        player.addAmmo(1);
                        bulletsMissed++;
                        bulletsToRemove.add(bullet);
                        break;
                    }
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

        // Remove dead amplifiers (health <= 0)
        List<Amplifier> deadAmplifiers = new ArrayList<>();
        for (Amplifier amp : amplifiers) {
            if (!amp.isAlive()) {
                deadAmplifiers.add(amp);
            }
        }
        amplifiers.removeAll(deadAmplifiers);
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
        int centerX = player.getCenterX() + GameConstants.PLAYER_BULLET_SPAWN_OFFSET_X;
        int centerY = player.getCenterY();
        bullets.add(new Bullet(centerX, centerY, true));
        bulletsFired++;
    }

    public void updateMonsterShooting() {
        for (Monster monster : monsters) {
            if (monster.canShoot()) {
                int centerX = monster.getCenterX();
                int centerY = monster.getCenterY();

                // Calculate direction from monster to player
                int playerCenterX = player.getCenterX();
                int playerCenterY = player.getCenterY();

                double dirX = playerCenterX - centerX;
                double dirY = playerCenterY - centerY;
                double distance = Math.sqrt(dirX * dirX + dirY * dirY);

                // Normalize direction (smooth lerp)
                double velocityX = 0;
                double velocityY = 0;
                if (distance > 0) {
                    velocityX = dirX / distance;
                    velocityY = dirY / distance;
                }

                // Create bullet and set velocity
                Bullet bullet = new Bullet(centerX, centerY, false);
                bullet.setVelocity(velocityX, velocityY);
                bullets.add(bullet);

                // Play monster shoot sound with frequency control (30% chance)
                if (random.nextInt(100) < GameConstants.AUDIO_MONSTER_SHOOT_FREQUENCY) {
                    AudioManager.playMonsterShoot();
                }

                monster.resetShootCooldown();
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

    public int getCurrentWave() {
        return currentWave;
    }

    /**
     * Check apakah harus tampilkan wave notification
     * Hanya trigger sekali per wave change, dan hanya selama 2 detik
     */
    public boolean shouldShowWaveNotification() {
        long currentTime = System.currentTimeMillis();

        // Jika wave belum berubah dari notifikasi terakhir
        if (currentWave == lastNotifiedWave) {
            // Tapi masih dalam durasi notification (< 2 detik)
            if (currentTime - lastWaveNotificationTime < GameConstants.WAVE_NOTIFICATION_DURATION) {
                return true; // Masih tampilkan
            } else {
                return false; // Sudah 2 detik berlalu
            }
        }

        // Wave berubah! Trigger notification
        lastWaveNotificationTime = currentTime;
        lastNotifiedWave = currentWave;
        return true;
    }
}
