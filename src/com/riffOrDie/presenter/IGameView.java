package com.riffOrDie.presenter;

import java.util.List;
import com.riffOrDie.model.Player;
import com.riffOrDie.model.Monster;
import com.riffOrDie.model.Bullet;
import com.riffOrDie.model.Amplifier;

/**
 * Interface untuk Game View (MVP Pattern)
 * View HANYA bertanggung jawab untuk display
 * Presenter akan tell View apa yang harus di-display
 */
public interface IGameView {
    /**
     * Update score display
     */
    void updateScore(int score);

    /**
     * Update player health display
     */
    void updateHealth(int health);

    /**
     * Update bullets fired count
     */
    void updateBulletsFired(int bulletsFired);

    void updateAmmo(int ammoCount);

    /**
     * Update monsters list untuk rendering
     */
    void updateMonsters(List<Monster> monsters);

    /**
     * Update bullets list untuk rendering
     */
    void updateBullets(List<Bullet> bullets);

    /**
     * Update player position untuk rendering
     */
    void updatePlayer(Player player);

    /**
     * Update amplifiers list untuk rendering
     */
    void updateAmplifiers(List<Amplifier> amplifiers);

    /**
     * Show game over dialog (blocking)
     * Display score and wait for user to click OK
     * After OK, return to menu
     */
    void showGameOverDialog(int finalScore);

    /**
     * Repaint game canvas
     */
    void repaintGame();

    /**
     * Set presenter reference
     */
    void setPresenter(IGamePresenter presenter);
}
