package riffOrDie.presenter.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * AudioManager - Handle semua audio playback (SFX dan BGM)
 * Thread-safe sound playback dengan volume control
 */
public class AudioManager {
    private static Clip playerShootClip;
    private static Clip monsterShootClip;
    private static Clip amplifierHitClip;
    private static Clip backgroundMusicClip;

    private static float playerShootVolume = 1.0f;
    private static float monsterShootVolume = 0.6f;
    private static float amplifierHitVolume = 0.8f;
    private static float backgroundMusicVolume = 0.4f;

    /**
     * Initialize dan load semua audio files
     */
    public static void initialize() {
        try {
            playerShootClip = loadAudioClip("src/assets/bullet-player-sound.wav");
            monsterShootClip = loadAudioClip("src/assets/bullet-monster-sound.wav");
            amplifierHitClip = loadAudioClip("src/assets/amplifier-hit-sound.wav");
            backgroundMusicClip = loadAudioClip("src/assets/backsound.wav");
            
            System.out.println("✅ AudioManager initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Error initializing AudioManager: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load audio file sebagai Clip
     */
    private static Clip loadAudioClip(String filePath) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    }

    /**
     * Play player shoot sound effect
     */
    public static void playPlayerShoot() {
        playClip(playerShootClip, playerShootVolume);
    }

    /**
     * Play monster shoot sound effect
     */
    public static void playMonsterShoot() {
        playClip(monsterShootClip, monsterShootVolume);
    }

    /**
     * Play amplifier hit sound effect
     */
    public static void playAmplifierHit() {
        playClip(amplifierHitClip, amplifierHitVolume);
    }

    /**
     * Play background music dengan looping
     */
    public static void playBackgroundMusic() {
        if (backgroundMusicClip != null) {
            setVolume(backgroundMusicClip, backgroundMusicVolume);
            backgroundMusicClip.setFramePosition(0);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stop background music
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
    }

    /**
     * Play clip (SFX) tanpa looping
     */
    private static void playClip(Clip clip, float volume) {
        if (clip != null) {
            try {
                setVolume(clip, volume);
                clip.setFramePosition(0);
                clip.start();
            } catch (Exception e) {
                System.err.println("Error playing clip: " + e.getMessage());
            }
        }
    }

    /**
     * Set volume untuk clip (0.0 = mute, 1.0 = max)
     */
    private static void setVolume(Clip clip, float volume) {
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // dB = 20 * log10(volume)
                // Range: -80dB (silent) to 0dB (max)
                float dB = (float) (20 * Math.log10(Math.max(volume, 0.0001)));
                dB = Math.min(dB, 0); // Cap at 0dB
                gainControl.setValue(dB);
            } catch (Exception e) {
                System.err.println("Error setting volume: " + e.getMessage());
            }
        }
    }

    /**
     * Set volume untuk sound effects
     */
    public static void setPlayerShootVolume(float volume) {
        playerShootVolume = Math.max(0, Math.min(volume, 1.0f));
    }

    public static void setMonsterShootVolume(float volume) {
        monsterShootVolume = Math.max(0, Math.min(volume, 1.0f));
    }

    public static void setAmplifierHitVolume(float volume) {
        amplifierHitVolume = Math.max(0, Math.min(volume, 1.0f));
    }

    public static void setBackgroundMusicVolume(float volume) {
        backgroundMusicVolume = Math.max(0, Math.min(volume, 1.0f));
    }

    /**
     * Cleanup resources
     */
    public static void shutdown() {
        stopBackgroundMusic();
        if (playerShootClip != null) playerShootClip.close();
        if (monsterShootClip != null) monsterShootClip.close();
        if (amplifierHitClip != null) amplifierHitClip.close();
        if (backgroundMusicClip != null) backgroundMusicClip.close();
        System.out.println("✅ AudioManager shutdown");
    }
}
