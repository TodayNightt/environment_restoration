//package Terrain;

public class NoiseMap {
    public static double[][] GenerateMap(int width, int height) {
        double[][] map = new double[height][width];
        OpenSimplexNoise noise = new OpenSimplexNoise();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                map[y][x] = noise.eval(x, y, 0.0);

            }
        }
        return map;
    }

    // private static double map(double value, double start1, double end1, double
    // start2, double end2) {
    // return 0.4;

    // }

    public static void main(String[] args) {
        double[][] Hm = NoiseMap.GenerateMap(10, 10);
        for (int i = 0; i < Hm.length; i++) {
            for (int j = 0; j < Hm[i].length; j++) {
                System.out.println(Hm[i][j]);
            }
        }
    }
}
