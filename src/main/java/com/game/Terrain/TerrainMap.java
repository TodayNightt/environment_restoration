package com.game.Terrain;

import com.game.Graphics.Scene;
import com.game.Terrain.Generation.NoiseMap;
import com.game.Terrain.Generation.TextureGenerator;
import com.game.WorkerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.game.Utils.TerrainContraints.*;

public class TerrainMap {

    private List<Chunk> chunkList;
    private List<Thread> threads;
    private String textureName;
    private int[] heightMap;
    private final  Scene scene;



    public TerrainMap(String texture, Scene scene) {
        this.textureName = texture;
        this.scene = scene;
        this.chunkList = new ArrayList<>();
        this.threads = new ArrayList<>();
        init();
    }

    public void init() {
        long seed = new Random().nextLong();
        chunkList.clear();
        double[] map1 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 4, 0.95, 0.004, seed);
        double[] map2 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.5, 0.004, seed);
        double[] map3 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.9, 0.0005, seed);
        double[] combineMap = NoiseMap.combineMap(map1, map2, map3);
        heightMap = NoiseMap.mapToInt(combineMap, -2, 1, CHUNK_HEIGHT, 1);
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
                chunkList.add(new Chunk(j, 0, i, newHeightMap, this));
            }
        }

        WorkerManager.getInstance().pass(chunkList).start();

        scene.createTexture("minimap", TextureGenerator.createColoredMap(heightMap, MAP_SIZE * CHUNK_SIZE));
    }



    // https://stackoverflow.com/questions/22131437/return-objects-from-arraylist
    public Chunk getChunk(int x, int y, int z) {
        for(Chunk chunk : chunkList){
            if(chunk.isChunk(x,y,z)) return chunk;
        }
        return null;
    }

    public void cleanup() {
        chunkList.forEach(Chunk::cleanup);
    }

    public int getSize() {
        return MAP_SIZE;
    }

    public List<Chunk> getMap() {
        return chunkList;
    }

    public static float getTextureRow() {
        return 3.0f;
    }

    public String getTextureName() {
        return textureName;
    }

    public int[] getHeightMap() {
        return heightMap;
    }
}
