package Terrain.Generation;

import java.util.Random;

import Terrain.OpenSimplexNoise.OpenSimplex2S;

import processing.core.PApplet;

public class NoiseMap {
    // https://cbrgm.net/post/2017-07-03-procedual-map-generation-part2/
    public static double[] GenerateMap(int height, int width, int octaves, double roughness, double scale) {
        long seed = new Random().nextLong();
        double[] map = new double[width * height];
        double layerFrequency = scale;
        double layerWeight = 1;
        // double weightSum = 0;

        for (int octave = 0; octave < octaves; octave++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // System.out.print(x + " " + y + " ");
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

    // public static void main(String[] args) {
    // double[][] noise = NoiseMap.GenerateMap(10, 10, 48394);
    // float[][] reMapped = NoiseMap.mapToFloat(noise, -1.0f, 1.0f, 0.0f, 1.0f, 10,
    // 10);
    // for (int i = 0; i < noise.length; i++) {
    // for (int j = 0; j < noise[i].length; j++) {
    // System.out.print(noise[i][j]);
    // System.out.println(" " + reMapped[i][j]);
    // }
    // }
    // }
}
