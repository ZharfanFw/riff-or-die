package riffOrDie.presenter;

import java.util.List;
import riffOrDie.model.GameScore;

/**
 * Interface untuk Menu View (MVP Pattern)
 * View HANYA bertanggung jawab untuk display menu
 */
public interface IMenuView {
    /**
     * Update scoreboard dengan data terbaru dari database
     */
    void updateScoreboard(List<GameScore> scores);

    /**
     * Show error message ke user
     */
    void showError(String message);

    /**
     * Set presenter reference
     */
    void setPresenter(IMenuPresenter presenter);

    /**
     * Get username dari input field
     */
    String getUsername();

    /**
     * Disable/enable play button
     */
    void setPlayButtonEnabled(boolean enabled);
}
