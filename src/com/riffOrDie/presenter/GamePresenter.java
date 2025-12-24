package com.riffOrDie.presenter;

import com.riffOrDie.db.DatabaseManager;
import com.riffOrDie.model.GameEngine;
import com.riffOrDie.model.GameScore;

/**
 * GamePresenter - orchestrate game logic
 * Implementasi dari IGamePresenter
 * Handle semua business logic untuk game screen
 * 
 * Tanggung jawab:
 * 1. Mengelola GameEngine state
 * 2. Handle input dari InputHandler
 * 3. Update view setiap frame
 * 4. Save score ke database saat game over
 */
public class GamePresenter implements IGamePresenter {
    private IGameView view;
    private GameEngine gameEngine;
    private String username;
    private int startingSisaPeluru;
    private boolean gameOver;

    public GamePresenter(IGameView view) {
        this.view = view;
        this.gameEngine = null;
        this.gameOver = false;
        this.view.setPresenter(this);
    }

    /**
     * Start game dengan username dan sisa peluru dari game sebelumnya
     */
    @Override
    public void startGame(String username, int sisaPeluru) {
        this.username = username;
        this.startingSisaPeluru = sisaPeluru;
        
        // Create new GameEngine instance
        this.gameEngine = new GameEngine(username, sisaPeluru);
        this.gameOver = false;
        
        // Notify view bahwa game sudah dimulai
        updateView();
    }

    /**
     * Handle player movement
     * Direction values: -1 (left/up), 0 (no movement), 1 (right/down)
     */
    @Override
    public void playerMove(int directionX, int directionY) {
        if (gameEngine == null || gameOver) {
            return;
        }
        
        // Set player velocity based on direction
        int speed = 5; // pixels per frame
        gameEngine.getPlayer().setVelocity(directionX * speed, directionY * speed);
    }

    /**
     * Handle player shoot
     */
    @Override
    public void playerShoot() {
        if (gameEngine == null || gameOver) {
            return;
        }
        
        gameEngine.playerShoot();
    }

    /**
     * Update game state setiap frame
     * Called oleh GamePanel di game loop
     * Ini adalah method paling sering dipanggil
     */
    @Override
    public void update() {
        if (gameEngine == null || gameOver) {
            return;
        }
        
        // Update game engine (semua entities, collisions, scoring)
        gameEngine.update();
        
        // Update monster shooting logic
        gameEngine.updateMonsterShooting();
        
        // Check if game over
        if (gameEngine.isGameOver()) {
            gameOver = true;
            endGame();
            return;
        }
        
        // Update view dengan state terbaru
        updateView();
    }

    /**
     * Update view dengan state terbaru dari game engine
     * Method ini dipanggil setiap frame
     */
    private void updateView() {
        // Update score, health, ammo
        view.updateScore(gameEngine.getScore());
        view.updateHealth(gameEngine.getPlayer().getHealth());
        
        // Update entities
        view.updateMonsters(gameEngine.getMonsters());
        view.updateBullets(gameEngine.getBullets());
        view.updateAmplifiers(gameEngine.getAmplifiers());
        view.updatePlayer(gameEngine.getPlayer());
    }

    /**
     * End game dan save score ke database
     */
    @Override
    public void endGame() {
        if (gameEngine == null) {
            return;
        }
        
        gameOver = true;
        
        // Create GameScore object
        GameScore gameScore = new GameScore();
        gameScore.setUsername(username);
        gameScore.setSkor(gameEngine.getScore());
        gameScore.setPeluruMeleset(gameEngine.getBulletsMissed());
        gameScore.setSisaPeluru(startingSisaPeluru);
        
        // Save to database
        try {
            DatabaseManager.saveScore(gameScore);
        } catch (Exception e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
        
        view.showGameOver(gameEngine.getScore());
    }

    /**
     * Check apakah game sudah over
     */
    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Get current game score
     */
    @Override
    public int getScore() {
        return gameEngine != null ? gameEngine.getScore() : 0;
    }

    /**
     * Get game engine (untuk testing/debugging)
     */
    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
