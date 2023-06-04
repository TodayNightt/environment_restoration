package Terrain;

import Graphics.Chunk;
import Terrain.Generation.NoiseMap;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;
import java.util.List;

import static Graphics.Chunk.CHUNK_HEIGHT;
import static Graphics.Chunk.CHUNK_SIZE;

public class TerrainMap {
    private final int MAP_SIZE = 10;
    private final List<Chunk> chunkList;
    private final Texture blockAtlas;

    public Texture getTexture() {
        return blockAtlas;
    }


    public TerrainMap(GL3 gl, Texture blockAtlas) {
        this.blockAtlas = blockAtlas;
        chunkList = new ArrayList<>();
        int[] heightMap = NoiseMap.mapToInt(
                NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 4, 0.95, 0.004),
                -2, 1,
                0, CHUNK_HEIGHT);
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
                chunkList.add(new Chunk(gl, j, 0, i, newHeightMap, this));
            }
        }
        chunkList.forEach(Chunk::initializeBuffers);
        int total = chunkList.stream().mapToInt(chunk -> chunk.getMesh().getNumVertices()).sum();
        System.out.println(total);

    }


    // https://stackoverflow.com/questions/22131437/return-objects-from-arraylist
    public Chunk getChunk(int x, int y, int z) {
        return chunkList.stream().filter(chunk -> chunk.isChunk(x, y, z)).findFirst().get();
    }


    public int getSize() {
        return MAP_SIZE;
    }

    public List<Chunk> getMap() {
        return chunkList;
    }

    public float getTextureRow() {
        return 2.0f;
    }
}
