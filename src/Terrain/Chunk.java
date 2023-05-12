package Terrain;

public class Chunk {
    protected final int CHUNK_SIZE = 10;
    protected final int BLOCK_SIZE = 100;

    private Block[][] map;
    public int[][] heightMap;

    public Chunk() {
        double[][] noise = NoiseMap.GenerateMap(CHUNK_SIZE, CHUNK_SIZE, 183792);
        heightMap = NoiseMap.mapToInt(noise, -1, 1, 0, 400, CHUNK_SIZE, CHUNK_SIZE);
        map = new Block[CHUNK_SIZE][CHUNK_SIZE];

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                int height = heightMap[y][x] / BLOCK_SIZE + 1;
                // int remain = heightMap[y][x] % BLOCK_SIZE;
                map[y][x] = new Block(x, y, height, 0);
                // for (int z = 0; z <= height; z++) {
                // map[y][x][z] = new Block(new PVector(x, y, z), 0);
                // }
                // if (height != 0)
                // map[y][x][height + 1] = new Block(new PVector(x, y, height + 1), 0);
            }
        }
    }

    public static void main(String[] args) {
        int MB = 1024 * 1024;
        Runtime runtime = Runtime.getRuntime();
        Chunk c = new Chunk();
        for (int y = 0; y < c.map.length; y++) {
            for (int x = 0; x < c.map[y].length; x++) {
                System.out.println(c.map[y][x]);

            }
        }

        // Print used memory
        System.out.println("Used Memory:"
                + (runtime.totalMemory() - runtime.freeMemory()) / MB);

        // Print free memory
        System.out.println("Free Memory:"
                + runtime.freeMemory() / MB);

        // Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / MB);

        // Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / MB);
    }
}

record Block(int x, int y, int height, int type) {
}
