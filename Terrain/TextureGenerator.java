import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class TextureGenerator {
    private static final Color[] dirt = { new Color(182, 159, 102), new Color(118, 85, 43), new Color(64, 41, 5) };
    private static final Color[] grass = { new Color(95, 195, 20), new Color(121, 208, 33), new Color(161, 223, 80),
            new Color(85, 194, 51), new Color(55, 174, 15), Color.decode(null) };

    public static void GenerateDirtMap() {
        // double[][] noiseMap = NoiseMap.GenerateMap(20, 20);

        // https://stackoverflow.com/questions/23234306/how-to-edit-the-pixels-in-a-bufferedimage
        // https://stackoverflow.com/questions/20425507/how-to-create-image-in-java
        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, dirt[new Random().nextInt(dirt.length)].getRGB());
            }
        }
        try {
            ImageIO.write(resizeImage(image, 100, 100), "png", new File("Dirt_Texture.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GenerateGrassMap() {
        // double[][] noiseMap = NoiseMap.GenerateMap(10, 10);
        // https://stackoverflow.com/questions/23234306/how-to-edit-the-pixels-in-a-bufferedimage
        // https://stackoverflow.com/questions/20425507/how-to-create-image-in-java
        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                image.setRGB(x, y, grass[new Random().nextInt(grass.length)].getRGB());
            }
        }
        try {
            ImageIO.write(resizeImage(image, 100, 100), "png", new File("Grass_Texture.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void SaveTexture(double[][] map) {

    }

    // https://stackoverflow.com/questions/12620158/save-resized-image-java
    private static BufferedImage resizeImage(BufferedImage originalImage, int IMG_WIDTH, int IMG_HEIGHT) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }
}
