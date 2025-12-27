package riffOrDie;

import riffOrDie.view.GameFrame;

/**
 * MAIN - Entry point aplikasi Riff Or Die
 * 
 * Fungsi:
 * - Membuat JFrame utama (GameFrame)
 * - Menampilkan window game
 * - Starting point untuk seluruh aplikasi
 * 
 * Flow: Main → GameFrame → MenuPanel / GamePanel
 */
public class Main {
    public static void main(String[] args) {
        // Buat JFrame container utama
        GameFrame gameFrame = new GameFrame();
        
        // Tampilkan window ke layar
        gameFrame.setVisible(true);
    }
}
