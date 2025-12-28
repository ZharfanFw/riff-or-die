package riffOrDie.presenter;

/**
 * InputHandler - Keyboard input handler untuk game screen
 * 
 * Fungsi Utama:
 * - Implement KeyListener untuk menangkap keyboard input
 * - Route input ke GamePresenter (MVP pattern: input → presenter → model)
 * - Track direction state (directionX, directionY) untuk continuous movement
 * - Support shooting dan menu navigation
 * 
 * Key Bindings:
 * - LEFT / RIGHT arrows: Horizontal movement (directionX: -1, 0, 1)
 * - UP / DOWN arrows: Vertical movement (directionY: -1, 0, 1)
 * - Z key: Shoot (playerShoot())
 * - SPACE key: Return to menu (returnToMenu())
 * 
 * Movement Handling:
 * - keyPressed(): Set direction saat key ditekan
 * - keyReleased(): Clear direction saat key dilepas
 * - Continuous: presenter.playerMove() dipanggil every game loop frame
 * - Smooth: player keep moving sampai key dilepas
 * 
 * MVP Pattern:
 * - InputHandler: Detect keyboard input (peripheral data)
 * - GamePresenter: Decide action based on input (orchestration)
 * - GameEngine: Execute action dan update model state
 * - NOT directly modify model/engine state dari InputHandler
 * 
 * Thread Safety:
 * - Single-threaded: Swing EDT (Event Dispatch Thread) menjalankan KeyListener
 * - Presenter dan GameEngine update di game loop thread
 * - No race condition: presenter.playerMove() synchronized via Swing EDT
 * 
 * Direction State Machine:
 * - Initial: directionX = 0, directionY = 0 (no movement)
 * - LEFT pressed: directionX = -1
 * - LEFT released: directionX = 0
 * - Simultaneous: Support diagonal movement (LEFT + UP = (-1, -1))
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import riffOrDie.presenter.IGamePresenter;

public class InputHandler implements KeyListener {
    /** Reference ke GamePresenter */
    private IGamePresenter presenter;
    
    /** Direction horizontal (-1, 0, 1) */
    private int directionX = 0;
    
    /** Direction vertical (-1, 0, 1) */
    private int directionY = 0;

    /**
     * Constructor - Inisialisasi input handler dengan presenter
     * 
     * @param presenter Reference ke IGamePresenter
     */
    public InputHandler(IGamePresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Dipanggil saat tombol keyboard ditekan
     * 
     * @param e KeyEvent yang berisi informasi tombol
     */
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

    /**
     * Dipanggil saat tombol keyboard dilepas
     * 
     * @param e KeyEvent yang berisi informasi tombol
     */
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

    /**
     * Tidak digunakan
     * Dipanggil saat character diketik (untuk text input)
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
