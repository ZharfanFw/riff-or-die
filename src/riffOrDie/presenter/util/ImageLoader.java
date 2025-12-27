package riffOrDie.presenter.util;

/**
 * IMAGELOADER - Load PNG assets
 * 
 * Fungsi:
 * - loadImage(path) - Load PNG file
 * - Return BufferedImage atau null jika fail
 * - Graceful fallback jika asset missing
 * 
 * Assets: player.png, monster-easy.png, monster-hard.png, amplifier.png,
 *         bullet-player.png, bullet-monster.png
 */
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    public static Image loadImage(String path) {
        try {
            File imageFile = new File(path);

            if (!imageFile.exists()) {
                System.err.println("Image file tidak ditemukan: " + path);
                return null;
            }

            BufferedImage bufferedImage = ImageIO.read(imageFile);

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
