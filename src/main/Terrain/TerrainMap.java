package Terrain;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import Graphics.Chunk;
import Terrain.Generation.NoiseMap;
import static Graphics.Chunk.CHUNK_SIZE;
import static Graphics.Chunk.CHUNK_HEIGHT;

public class TerrainMap {
    private final int MAP_SIZE = 6;
    private List<Chunk> chunkList;
    private int[] heightMap;

    public TerrainMap(GL3 gl) {
        chunkList = new ArrayList<>();
        heightMap = NoiseMap.mapToInt(
                NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 4, 0.95, 0.004),
                -2, 1,
                0, CHUNK_HEIGHT - 3);
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                int[] newHeightMap = new int[CHUNK_SIZE * CHUNK_SIZE];
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        int offset = CHUNK_SIZE * MAP_SIZE;
                        newHeightMap[x + (z * CHUNK_SIZE)] = heightMap[(x + (j * CHUNK_SIZE)) + ((z *
                                offset) + (i * (offset * CHUNK_SIZE)))];
                    }
                }
                chunkList.add(new Chunk(gl, j, 0, i, newHeightMap));
            }
        }
    }

    public List<Chunk> getMap() {
        return chunkList;
    }
}
