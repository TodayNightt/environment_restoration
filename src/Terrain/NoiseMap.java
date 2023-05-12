package Terrain;

import OpenSimplexNoise.OpenSimplexNoise;

import processing.core.PApplet;

public class NoiseMap {
    public static double[][] GenerateMap(int width, int height, long seed) {
        double[][] map = new double[height][width];
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                map[y][x] = noise.eval(x, y, 0.0);
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

    public static int[][] mapToInt(double[][] map, int start, int end, int start1, int end1, int width,
            int height) {
        int[][] newMap = new int[height][width];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                newMap[i][j] = (int) PApplet.map((float) map[i][j], start, end, start1, end1);
            }
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
