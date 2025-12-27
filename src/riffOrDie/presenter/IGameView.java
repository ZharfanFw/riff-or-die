package riffOrDie.presenter;

import java.util.List;
import riffOrDie.model.Player;
import riffOrDie.model.Monster;
import riffOrDie.model.Bullet;
import riffOrDie.model.Amplifier;

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

    void updateAmmo(int ammoCount, int bulletsMissed);

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
     * Update wave counter display
     */
    void updateWave(int waveNumber);
    /**
     * Tampilkan wave transition notification di tengah layar
     */
    void showWaveNotification(int waveNumber);

    /**

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
