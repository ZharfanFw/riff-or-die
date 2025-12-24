package com.riffOrDie.presenter;

/**
 * Interface untuk Menu Presenter (MVP Pattern)
 * Presenter handle semua business logic untuk menu
 */
public interface IMenuPresenter {
    /**
     * Load scoreboard data dari database
     */
    void loadScoreboard();

    /**
     * Handle play button click
     * @param username - username yang diinput user
     */
    void onPlayClicked(String username);

    /**
     * Handle quit button click
     */
    void onQuitClicked();
}
