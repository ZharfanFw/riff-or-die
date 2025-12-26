package riffOrDie.presenter.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import riffOrDie.presenter.IGamePresenter;

/**
 * InputHandler - handle keyboard input
 * Communicate dengan GamePresenter, NOT directly dengan model
 * Utility class untuk input handling
 */
public class InputHandler implements KeyListener {
    private IGamePresenter presenter;
    private int directionX = 0;
    private int directionY = 0;

    public InputHandler(IGamePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Horizontal Movement
        if (keyCode == KeyEvent.VK_LEFT) {
            directionX = -1;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            directionX = 1;
        }

        // Vertical Movement
        if (keyCode == KeyEvent.VK_UP) {
            directionY = -1;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            directionY = 1;
        }

        // Send movement command to presenter
        if (directionX != 0 || directionY != 0) {
            presenter.playerMove(directionX, directionY);
        }

        // Shoot (Z key)
        if (keyCode == KeyEvent.VK_Z) {
            presenter.playerShoot();
        }

        // Return to menu (SPACE key)
        if (keyCode == KeyEvent.VK_SPACE) {
            presenter.returnToMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            directionX = 0;
        }

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
            directionY = 0;
        }

        // Send updated movement to presenter
        presenter.playerMove(directionX, directionY);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
