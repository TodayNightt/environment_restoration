package com.game.Terrain.Generation;

import com.game.Terrain.OpenSimplexNoise.OpenSimplex2S;

import java.util.Random;

import static com.game.Utils.TerrainContraints.*;


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

    public static int[] mapToInt(double[] map, int start, int end, int start1, int end1) {

        int[] newMap = new int[map.length];
        for (int i = 0; i < map.length; i++) {
            newMap[i] = (int) map((float) map[i], start, end, start1, end1);

        }
        return newMap;
    }

    public static double[] combineMap(double[] map1, double[] map2, double[] map3) {
        if (map1.length != map2.length || map2.length != map3.length) {
            throw new RuntimeException("All map must be the same length");
        }
        double[] newMap = new double[map1.length];
        for (int i = 0; i < map1.length; i++) {
            newMap[i] += map1[i] + map2[i] * map3[i];
        }
        return newMap;
    }

    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        //https://stackoverflow.com/questions/3451553/value-remapping
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    public static int[] createHeightMap(){
        long seed = new Random().nextLong();
        double[] map1 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 4, 0.95, 0.004, seed);
        double[] map2 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.5, 0.004, seed);
        double[] map3 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.9, 0.0005, seed);
        double[] combineMap = NoiseMap.combineMap(map1, map2, map3);
        return NoiseMap.mapToInt(combineMap, -2, 1, CHUNK_HEIGHT, 1);
    }


}
