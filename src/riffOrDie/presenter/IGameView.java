package riffOrDie.presenter;

/**
 * IGAMEVIEW - Interface kontrak GamePanel
 * 
 * Fungsi:
 * - Define contract untuk GamePanel
 * - Methods: updateScore, updateHealth, updateMonsters, showGameOverDialog
 */
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
     * 
     * @param score Score terbaru
     */
    void updateScore(int score);

    /**
     * Update player health display
     * 
     * @param health Health player saat ini
     */
    void updateHealth(int health);

    /**
     * Update bullets fired count
     * 
     * @param bulletsFired Jumlah bullet yang sudah ditembak
     */
    void updateBulletsFired(int bulletsFired);

    /**
     * Update ammo display
     * 
     * @param ammoCount Jumlah ammo saat ini
     * @param bulletsMissed Jumlah bullet yang meleset
     */
    void updateAmmo(int ammoCount, int bulletsMissed);

    /**
     * Update monsters list untuk rendering
     * 
     * @param monsters List monster aktif
     */
    void updateMonsters(List<Monster> monsters);

    /**
     * Update bullets list untuk rendering
     * 
     * @param bullets List bullet aktif
     */
    void updateBullets(List<Bullet> bullets);

    /**
     * Update player position untuk rendering
     * 
     * @param player Player entity
     */
    void updatePlayer(Player player);

    /**
     * Update amplifiers list untuk rendering
     * 
     * @param amplifiers List amplifier aktif
     */
    void updateAmplifiers(List<Amplifier> amplifiers);

    /**
     * Update wave counter display
     * 
     * @param waveNumber Wave number saat ini (0-3)
     */
    void updateWave(int waveNumber);

    /**
     * Tampilkan wave transition notification di tengah layar
     * 
     * @param waveNumber Wave number yang akan muncul
     */
    void showWaveNotification(int waveNumber);

    /**
     * Show game over dialog (blocking)
     * Display score and wait for user to click OK
     * After OK, return to menu
     * 
     * @param finalScore Score akhir game
     */
    void showGameOverDialog(int finalScore);

    /**
     * Repaint game canvas
     */
    void repaintGame();

    /**
     * Set presenter reference
     * 
     * @param presenter IGamePresenter instance
     */
    void setPresenter(IGamePresenter presenter);
}
