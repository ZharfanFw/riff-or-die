package com.riffOrDie.presenter;

/**
 * Interface untuk Game Presenter (MVP Pattern)
 * Presenter HANYA bertanggung jawab untuk orchestrate business logic
 * Presenter handle semua komunikasi antara Model dan View
 */
public interface IGamePresenter {
    /**
     * Start game dengan username
     * @param username - nama pemain
     * @param sisaPeluru - peluru sisa dari game sebelumnya
     */
    void startGame(String username, int sisaPeluru);

    /**
     * Handle player movement
     * @param directionX - horizontal direction (-1, 0, 1)
     * @param directionY - vertical direction (-1, 0, 1)
     */
    void playerMove(int directionX, int directionY);

    /**
     * Handle player shoot
     */
    void playerShoot();

    /**
     * Update game state (called every frame)
     */
    void update(double deltaTime);

    /**
     * End game dan save score
     */
    void endGame();

    /**
     * Check apakah game sudah over
     */
    boolean isGameOver();

    /**
     * Get current game score
     */
    int getScore();

    /**
     * Return to menu (called when SPACE pressed during game)
     * Show game over dialog with score and save to database
     */
    void returnToMenu();
}
