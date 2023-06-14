//package Terrain.Generation;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class TextureGenerator {
//    private static final Color[] dirt = { new Color(182, 159, 102), new Color(118, 85, 43), new Color(64, 41, 5),
//            new Color(20, 47, 9), new Color(96, 87, 30) };
//    private static final Color[] grass = { new Color(95, 195, 20), new Color(121, 208, 33), new Color(161, 223, 80),
//            new Color(85, 194, 51), new Color(55, 174, 15) };
//    private static  final Color[]sand = { new Color(246,215,176), new Color(242,210,169),new Color(236,204,162),new Color(231,196,150),new Color(225,191,146)};
//private static  final Color[] water = {new Color(15,94,156),new Color(35,137,218),new Color(28,163,236),new Color(90,188,216),new Color(116,204,244)};
//
//private  static  final Color[] snow = {new Color(255,255,255), new Color(236,255,253),new Color(208,236,235),new Color(160,230,236),new Color(148,242,244)};
//    public static void GenerateAtlas() throws IOException {
//        int[] dirtTexture = NoiseMap.mapToInt(NoiseMap.GenerateMap(40, 40), -1, 1, 0, dirt.length);
//        int[] grassTexture = NoiseMap.mapToInt(NoiseMap.GenerateMap(40, 40), -2, 1, 0, grass.length);
//        int[] sandTexture  = NoiseMap.mapToInt(NoiseMap.GenerateMap(40,40),-2,1,0,sand.length);
//        int[] waterTexture = NoiseMap.mapToInt(NoiseMap.GenerateMap(40,40),-2,1,0,water.length);
//        int[] snowTexture = NoiseMap.mapToInt(NoiseMap.GenerateMap(40,40),-2,1,0,snow.length);
//
//        SaveTexture(dirtTexture,grassTexture,sandTexture,waterTexture,snowTexture, "block_atlas");
//    }
//
//    public static void SaveTexture(int[] dirtTexture,int[] grassTexture, int[] sandTexture,int[] waterTexture,int[] snowTexture,String type) throws IOException {
//        // https://stackoverflow.com/questions/23234306/how-to-edit-the-pixels-in-a-bufferedimage
//        // https://stackoverflow.com/questions/20425507/how-to-create-image-in-java
//        BufferedImage image = new BufferedImage(74, 74, BufferedImage.TYPE_INT_ARGB);
//        System.out.println(image.getWidth());
//        for (int x = 2; x < image.getWidth(); x++) {
//            for (int y = 2; y < image.getHeight(); y++) {
//                if(x <= 23 && y > 25 && y <= 47) {
//                    image.setRGB(x,y,water[waterTexture[x+(y*20)]].getRGB());
//                }else if (x > 25 && y > 25 && y <=47 && x <= 47){
//                    image.setRGB(x,y,snow[snowTexture[x+(y*20)]].getRGB());
//                } else if(x <= 23 && y > 49 && y < image.getHeight()-2){
//                    image.setRGB(x,y,dirt[dirtTexture[x+(y*20)]].getRGB());
//                } else if (x > 25 && y > 49 && y < image.getHeight()-2 && x <= 47) {
//                    image.setRGB(x,y,grass[grassTexture[x+(y*20)]].getRGB());
//                } else if (x >=50 && y > 49 && y < image.getHeight()-2 && x < image.getWidth()-2) {
//                image.setRGB(x, y, sand[sandTexture[x + (y * 20)]].getRGB());
//                }
//            }
//
//            }
//            ImageIO.write(image, "png",
//                    new File(String.format("src/main/resources/textures/%s_texture.png", type)));
//
//
//    }
//
//    // https://stackoverflow.com/questions/12620158/save-resized-image-java
//    private static BufferedImage resizeImage(BufferedImage originalImage, int IMG_WIDTH, int IMG_HEIGHT) {
//        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
//                BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
//        g.dispose();
//
//        return resizedImage;
//    }
//}
