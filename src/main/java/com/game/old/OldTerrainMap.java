//package com.game.lwjgl.Terrain;
//
//import com.game.lwjgl.Graphics.OldChunk;
//import com.game.common.Scene;
//import com.game.lwjgl.Graphics.TextureList;
//import com.game.lwjgl.Terrain.Generation.NoiseMap;
//import com.game.lwjgl.Terrain.Generation.TextureGenerator;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class OldTerrainMap {
//    private final int MAP_SIZE = 20;
//    private final List<OldChunk> chunkList;
//    private final String textureName;
//    private final long seed;
//
//
//    public OldTerrainMap(Scene scene, String texture) {
//        this.seed = new Random().nextLong();
//        this.textureName = texture;
//        chunkList = new ArrayList<>();
//        double[] map1 = NoiseMap.GenerateMap(MAP_SIZE * OldChunk.CHUNK_SIZE, MAP_SIZE * OldChunk.CHUNK_SIZE, 4, 0.95, 0.004, seed);
//        double[] map2 = NoiseMap.GenerateMap(MAP_SIZE * OldChunk.CHUNK_SIZE, MAP_SIZE * OldChunk.CHUNK_SIZE, 3, 0.5, 0.004, seed);
//        double[] map3 = NoiseMap.GenerateMap(MAP_SIZE * OldChunk.CHUNK_SIZE, MAP_SIZE * OldChunk.CHUNK_SIZE, 3, 0.9, 0.0005, seed);
//        double[] combineMap = NoiseMap.combineMap(map1, map2, map3);
//        int[] heightMap = NoiseMap.mapToInt(combineMap, -2, 1, OldChunk.CHUNK_HEIGHT, 1);
//        for (int i = 0; i < MAP_SIZE; i++) {
//            for (int j = 0; j < MAP_SIZE; j++) {
//                int[] newHeightMap = new int[OldChunk.CHUNK_SIZE * OldChunk.CHUNK_SIZE];
//                for (int z = 0; z < OldChunk.CHUNK_SIZE; z++) {
//                    for (int x = 0; x < OldChunk.CHUNK_SIZE; x++) {
//                        int offset = OldChunk.CHUNK_SIZE * MAP_SIZE;
//                        newHeightMap[x + (z * OldChunk.CHUNK_SIZE)] = heightMap[(x + (j * OldChunk.CHUNK_SIZE)) + ((z *
//                                offset) + (i * (offset * OldChunk.CHUNK_SIZE)))];
//                    }
//                }
//                chunkList.add(new OldChunk(j, 0, i, newHeightMap, this));
//            }
//        }
//        chunkList.forEach(OldChunk::initializeBuffers);
//        TextureList.createTexture("minimap", TextureGenerator.createColoredMap(heightMap, MAP_SIZE * OldChunk.CHUNK_SIZE));
//
//
//    }
//
//
//    // https://stackoverflow.com/questions/22131437/return-objects-from-arraylist
//    public OldChunk getChunk(int x, int y, int z) {
//        return chunkList.stream().filter(chunk -> chunk.isChunk(x, y, z)).findFirst().get();
//    }
//
//    public void cleanup() {
//        chunkList.forEach(OldChunk::cleanup);
//    }
//
//    public int getSize() {
//        return MAP_SIZE;
//    }
//
//    public List<OldChunk> getMap() {
//        return chunkList;
//    }
//
//    public float getTextureRow() {
//        return 3.0f;
//    }
//
//    public String getTextureName() {
//        return textureName;
//    }
//
//    public long getSeed() {
//        return seed;
//    }
//}
