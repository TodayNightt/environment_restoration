package com.game.Terrain.Generation;

import com.game.Graphics.Chunk;
import com.game.Terrain.OpenSimplexNoise.OpenSimplex2S;
import processing.core.PApplet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import static com.game.Graphics.Chunk.SEA_LEVEL;

public class NoiseMap {
    // https://cbrgm.net/post/2017-07-03-procedual-map-generation-part2/
    public static double[] GenerateMap(int height, int width, int octaves, double roughness, double scale, long seed) {

        double[] map = new double[width * height];
        double layerFrequency = scale;
        double layerWeight = 1;
        // double weightSum = 0;

        for (int octave = 0; octave < octaves; octave++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    map[x + (y * width)] += OpenSimplex2S.noise3_ImproveXZ(seed, x * layerFrequency,
                            y * layerFrequency, 0.0)
                            * layerWeight;
                }
            }
            layerFrequency *= 2;
            // weightSum += layerWeight;
            layerWeight *= roughness;
        }
        return map;
    }

    public static double[] GenerateMap(int height, int width) {
        long seed = new Random().nextLong();

        double[] map = new double[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[x + (y * height)] += OpenSimplex2S.noise3_ImproveXZ(seed, x, y, 0.0);
            }
        }
        return map;

    }

    public static float[][] mapToFloat(double[][] map, float start, float end, float start1, float end1, int width,
                                       int height) {
        float[][] newMap = new float[height][width];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                newMap[i][j] = PApplet.map((float) map[i][j], start, end, start1, end1);
            }
        }
        return newMap;
    }

    public static int[] mapToInt(double[] map, int start, int end, int start1, int end1) {
        int[] newMap = new int[map.length];
        for (int i = 0; i < map.length; i++) {
            newMap[i] = (int) PApplet.map((float) map[i], start, end, start1, end1);

        }
        return newMap;
    }

    public static double[] combineMap(double[] map1, double[] map2, double[] map3) throws Exception {
        if (map1.length != map2.length || map2.length != map3.length) {
            throw new Exception("All map must be the same length");
        }
        double[] newMap = new double[map1.length];
        for (int i = 0; i < map1.length; i++) {
            newMap[i] += map1[i] + map2[i] * map3[i];
        }
        return newMap;
    }



//    public static void main(String[] args) throws Exception {
//        int MAP_SIZE = 10;
////8,0.5,0.004
//        //8,0.9,0.0005
//        double[] map1 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 4, 0.95, 0.004);
//        double[] map2 = NoiseMap.GenerateMap(MAP_SIZE*CHUNK_SIZE,MAP_SIZE*CHUNK_SIZE,3,0.5,0.004);
//        double[] map3 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.9, 0.0005);
//        double[] combineMap = NoiseMap.combineMap(map1,map2,map3);
//        int[] heightMap = NoiseMap.mapToInt(combineMap,-2,1,0,CHUNK_HEIGHT);
//
//        BufferedImage image = new BufferedImage(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, BufferedImage.TYPE_INT_RGB);
//        for (int y = 0; y < MAP_SIZE * CHUNK_SIZE; y++) {
//            for (int x = 0; x < MAP_SIZE * CHUNK_SIZE; x++) {
//                int value = heightMap[x + (y * MAP_SIZE *CHUNK_SIZE)];
//                System.out.println(heightMap[x + (y * MAP_SIZE *CHUNK_SIZE)]);
//                int rgb = 0x010101 * (int) ((value + 1) * 127.5);
//                image.setRGB(x, y, rgb);
//            }
//        }
//        ImageIO.write(image, "png", new File("noise.png"));
//    }


}
