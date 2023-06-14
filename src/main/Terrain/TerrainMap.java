package Terrain;

import Graphics.Chunk;
import Graphics.ShaderProgram;
import Terrain.Generation.NoiseMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Graphics.Chunk.CHUNK_HEIGHT;
import static Graphics.Chunk.CHUNK_SIZE;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TerrainMap {
    private final int MAP_SIZE = 20;
    private final List<Chunk> chunkList;
    private final String textureName;
    private ShaderProgram shaderP;
    private long seeds[];


    public TerrainMap( String texture) throws Exception {
        this.seeds = new long[3];

        initShaderP();
        this.textureName = texture;
        chunkList = new ArrayList<>();
        long seed = new Random().nextLong();
        seeds[0] = seed;
        double[] map1 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 4, 0.95, 0.004,seed);
        seed = new Random().nextLong();
        seeds[1] = seed;
        double[] map2 = NoiseMap.GenerateMap(MAP_SIZE*CHUNK_SIZE,MAP_SIZE*CHUNK_SIZE,3,0.5,0.004,seed);
        seed = new Random().nextLong();
        seeds[2] = seed;
        double[] map3 = NoiseMap.GenerateMap(MAP_SIZE * CHUNK_SIZE, MAP_SIZE * CHUNK_SIZE, 3, 0.9, 0.0005,seed);
        double[] combineMap = NoiseMap.combineMap(map1,map2,map3);
        int[] heightMap = NoiseMap.mapToInt(combineMap,-2,1,CHUNK_HEIGHT,1);
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
                chunkList.add(new Chunk( j, 0, i, newHeightMap, this));
            }
        }
        chunkList.forEach(Chunk::initializeBuffers);
        Thread thread = new Thread(() -> {
            try {
                NoiseMap.createColoredMap(heightMap,MAP_SIZE* CHUNK_SIZE,seeds);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();


    }

    private void initShaderP() throws Exception {
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/terrain.vert",GL_VERTEX_SHADER));
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/terrain.frag",GL_FRAGMENT_SHADER));
        this.shaderP = new ShaderProgram(shaderDataList);
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
        return 3.0f;
    }

    public String getTextureName(){
        return  textureName;
    }

    public ShaderProgram getShaderP(){
        return shaderP;
    }
    public long[] getSeeds() {
        return seeds;
    }
}
