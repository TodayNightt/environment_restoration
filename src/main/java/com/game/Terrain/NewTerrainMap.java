package com.game.Terrain;

import com.game.Graphics.NewChunk;
import com.game.Terrain.Generation.NoiseMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.game.Graphics.Chunk.CHUNK_HEIGHT;
import static com.game.Graphics.Chunk.CHUNK_SIZE;

public class NewTerrainMap {
    public static void main(String[] args) throws Exception {
        new NewTerrainMap();
    }
    private final int MAP_SIZE = 1;
    private final List<NewChunk> chunkList;
//    private final String textureName;
    private final long seeds;


    public NewTerrainMap() throws Exception {
        this.seeds = new Random().nextLong();
//        this.textureName = texture;
        chunkList = new ArrayList<>();
        double[] map1 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 4, 0.95, 0.004, seeds);
        double[] map2 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.5, 0.004, seeds);
        double[] map3 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.9, 0.0005, seeds);
        double[] combineMap = NoiseMap.combineMap(map1, map2, map3);
        int[] heightMap = NoiseMap.mapToInt(combineMap, -2, 1, CHUNK_HEIGHT, 1);
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
                chunkList.add(new NewChunk(j,0,i,newHeightMap, this));
            }
        }
        chunkList.forEach(NewChunk::initializeBuffers);
//        scene.addMapTexture(TextureGenerator.createColoredMap(heightMap, MAP_SIZE * CHUNK_SIZE, seeds));


    }


    // https://stackoverflow.com/questions/22131437/return-objects-from-arraylist
    public NewChunk getChunk(int x, int y, int z) {
        return chunkList.stream().filter(chunk -> chunk.isChunk(x, y, z)).findFirst().get();
    }
    public void cleanup(){
        chunkList.forEach(NewChunk::cleanup);
    }

    public int getSize() {
        return MAP_SIZE;
    }

    public List<NewChunk> getMap() {
        return chunkList;
    }

    public float getTextureRow() {
        return 3.0f;
    }

//    public String getTextureName() {
//        return textureName;
//    }

    public long getSeeds() {
        return seeds;
    }
}