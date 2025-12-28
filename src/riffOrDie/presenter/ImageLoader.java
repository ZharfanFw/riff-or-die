package riffOrDie.presenter;

/**
 * ImageLoader - Utility class untuk load PNG sprite assets
 * 
 * Fungsi Utama:
 * - loadImage(path): Load image file dari path yang diberikan
 * - Return BufferedImage jika sukses, null jika gagal
 * - Graceful error handling: log error tapi tidak crash game
 * - Single static method: simple stateless image loading
 * 
 * Asset Files yang Di-load:
 * - player.png: Player character sprite
 * - monster-easy.png: Easy monster sprite
 * - monster-hard.png: Hard monster sprite (boss)
 * - amplifier.png: Amplifier power-up sprite
 * - bullet-player.png: Player bullet sprite
 * - bullet-monster.png: Monster bullet sprite
 * 
 * Error Handling Strategy:
 * - Check file exists sebelum load (prevent FileNotFoundException)
 * - Check buffered image tidak null setelah read (catch corrupt files)
 * - Print error ke System.err (developer visibility)
 * - Return null pada error (graceful fallback)
 * - Game continue dengan null images (GameRenderer draw fallback shapes)
 * 
 * Flow:
 * 1. Game startup: ImageLoader.loadImage(path) dipanggil untuk setiap sprite
 * 2. Return BufferedImage atau null
 * 3. GameRenderer check null → draw fallback rectangle/circle
 * 4. Game continue to play tanpa visual assets
 * 
 * Asset Path Convention:
 * - src/assets/[filename].png
 * - Build path: assets/[filename].png (saat jar dibuat)
 */
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    /**
     * Load image file dari path
     * Static method, tidak perlu instance
     * 
     * @param path Path ke file image (PNG format)
     * @return BufferedImage jika sukses, null jika gagal/file tidak ada
     */
    public static Image loadImage(String path) {
        try {
            File imageFile = new File(path);

            // Cek apakah file ada
            if (!imageFile.exists()) {
                System.err.println("Image file tidak ditemukan: " + path);
                return null;
            }

            // Baca file image
            BufferedImage bufferedImage = ImageIO.read(imageFile);

            // Cek apakah image berhasil dibaca
            if (bufferedImage == null) {
                System.err.println("Gagal membaca image file: " + path);
                return null;
            }

            return bufferedImage;
        } catch (IOException e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return null;
        }
    }
}
