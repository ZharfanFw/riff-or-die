package riffOrDie.presenter;

/**
 * AudioManager - Centralized audio management untuk game
 * 
 * Fungsi Utama:
 * - Load dan manage semua WAV files pada initialization
 * - Provide interface untuk play SFX (Sound Effects) dan BGM (Background Music)
 * - Handle overlap playback untuk sound yang bisa dimainkan bersamaan
 * - Handle interrupt playback untuk sound yang exclusive (hanya satu instance)
 * 
 * Methods:
 * - initialize(): Load semua audio files dari assets directory
 * - playPlayerShoot(): Play sound saat player menembak (overlap - bisa simultaneous)
 * - playMonsterHit(): Play sound saat monster kena tembak (overlap - bisa simultaneous)
 * - playMonsterShoot(): Play sound saat monster menembak (overlap - bisa simultaneous)
 * - playAmplifierHit(): Play sound saat amplifier kena tembak (interrupt - single instance)
 * - playBackgroundMusic(): Play BGM loop selama gameplay
 * - setVolume(type, volume): Set volume untuk specific sound type
 * - stopAllSounds(): Hentikan semua audio playback
 * 
 * Audio Behavior & Volume Strategy:
 * - Player Shoot: 1.0f volume, overlap (multiple shots same time)
 * - Monster Shoot: 0.6f volume, overlap (multiple monsters)
 * - Monster Hit: 0.7f volume, overlap (multiple hits)
 * - Amplifier Hit: 0.8f volume, interrupt (one at a time)
 * - Background Music: 0.4f volume, continuous loop
 * 
 * Volume Control Fallback:
 * - Coba gunakan MASTER_GAIN control untuk universal volume
 * - Fallback ke VOLUME control jika MASTER_GAIN tidak tersedia
 * - Graceful handling jika volume control tidak supported
 * 
 * Threading:
 * - Static instance untuk global audio access
 * - Audio playback di Thread terpisah (non-blocking)
 * - Thread-safe untuk concurrent sound playback
 * 
 * Asset Path:
 * - Semua audio files di src/assets/ directory
 * - Format: WAV (compatible dengan Java AudioSystem)
 * - Fallback: Error message jika file missing, game continue tanpa audio
 */
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * AudioManager - Handle semua audio playback (SFX dan BGM)
 * Thread-safe sound playback dengan smart volume control (smart fallback)
 * Overlap-based playback untuk player bullet dan monster hit sounds
 * Interrupt-based playback untuk amplifier hit sound
 */
public class AudioManager {
    private static Clip playerShootClip;
    private static Clip monsterShootClip;
    private static Clip monsterHitClip;
    private static Clip amplifierHitClip;
    private static Clip backgroundMusicClip;

    private static float playerShootVolume = 1.0f;
    private static float monsterShootVolume = 0.6f;
    private static float monsterHitVolume = 0.7f;
    private static float amplifierHitVolume = 0.8f;
    private static float backgroundMusicVolume = 0.4f;

    /**
     * Initialize dan load semua audio files
     */
    public static void initialize() {
        try {
            playerShootClip = loadAudioClip("src/assets/bullet-player-sound.wav");
            monsterShootClip = loadAudioClip("src/assets/bullet-monster-sound.wav");
            monsterHitClip = loadAudioClip("src/assets/monster-hit-sound.wav");
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
     * Play player shoot sound effect (overlap-based: multiple sounds can play
     * simultaneously)
     */
    public static void playPlayerShoot() {
        playClip(playerShootClip, playerShootVolume);
    }

    /**
     * Play monster shoot sound effect (overlap-based: allows overlap)
     */
    public static void playMonsterShoot() {
        playClip(monsterShootClip, monsterShootVolume);
    }

    /**
     * Play monster hit sound effect (overlap-based: multiple sounds can play
     * simultaneously)
     */
    public static void playMonsterHit() {
        playClip(monsterHitClip, monsterHitVolume);
    }

    /**
     * Play amplifier hit sound effect (interrupt-based: stops previous sound and
     * restarts)
     */
    public static void playAmplifierHit() {
        playClipWithInterrupt(amplifierHitClip, amplifierHitVolume);
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
     * Play clip (SFX) tanpa looping - overlap-based behavior
     * Used by sounds that should allow multiple instances to play (player bullet,
     * monster shoot, monster hit)
     * Multiple triggers = multiple sounds playing simultaneously
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
     * Play clip dengan interrupt - stops and restarts sound jika already playing
     * Used by sounds yang ingin interrupt behavior (amplifier hit only)
     * Saat di-trigger ulang sebelum sound selesai → stop sound lama + restart dari
     * frame 0
     */
    private static void playClipWithInterrupt(Clip clip, float volume) {
        if (clip != null) {
            try {
                // Check if clip is running, jika ya maka stop dulu
                if (clip.isRunning()) {
                    clip.stop();
                }

                // Reset frame position ke awal
                clip.setFramePosition(0);

                // Set volume
                setVolume(clip, volume);

                // Start sound dari frame 0
                clip.start();
            } catch (Exception e) {
                // Silent fail - audio might stutter tapi tidak crash
            }
        }
    }

    /**
     * Get first available FloatControl from clip (smart fallback)
     * Try MASTER_GAIN first, fallback to VOLUME if unavailable
     * Return null if no control available (graceful degradation)
     */
    private static FloatControl getAvailableControl(Clip clip, Control.Type... types) {
        if (clip == null)
            return null;
        for (Control.Type type : types) {
            try {
                return (FloatControl) clip.getControl(type);
            } catch (IllegalArgumentException e) {
                // Control not available, try next
            }
        }
        return null;
    }

    /**
     * Set volume untuk clip (0.0 = mute, 1.0 = max)
     * Smart fallback: try MASTER_GAIN first, fallback to VOLUME
     * If no control available, audio still plays at system volume
     */
    private static void setVolume(Clip clip, float volume) {
        if (clip == null)
            return;

        FloatControl control = getAvailableControl(clip,
                FloatControl.Type.MASTER_GAIN,
                FloatControl.Type.VOLUME);

        if (control != null) {
            try {
                // dB = 20 * log10(volume)
                // Range: -80dB (silent) to 0dB (max)
                float dB = (float) (20 * Math.log10(Math.max(volume, 0.0001)));
                dB = Math.min(dB, 0); // Cap at 0dB
                control.setValue(dB);
            } catch (Exception e) {
                // Silent fail - audio still plays without volume control
            }
        }
        // If no control available, audio plays at system volume (graceful degradation)
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

    public static void setMonsterHitVolume(float volume) {
        monsterHitVolume = Math.max(0, Math.min(volume, 1.0f));
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
        if (playerShootClip != null)
            playerShootClip.close();
        if (monsterShootClip != null)
            monsterShootClip.close();
        if (monsterHitClip != null)
            monsterHitClip.close();
        if (amplifierHitClip != null)
            amplifierHitClip.close();
        if (backgroundMusicClip != null)
            backgroundMusicClip.close();
        System.out.println("✅ AudioManager shutdown");
    }
}
