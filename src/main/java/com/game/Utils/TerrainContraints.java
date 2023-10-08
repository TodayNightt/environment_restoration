package com.game.Utils;

import com.game.Terrain.Chunk;

public class TerrainContraints {
    public static final int MAP_SIZE = 20;
    public static final int CHUNK_SIZE = 15;
    public static final int CHUNK_HEIGHT = 40;
    public static final int SEA_LEVEL = 10;
    public static final int MOUNTAIN_LEVEL = CHUNK_HEIGHT - 6;
    public static final int CHUNK_SQR = CHUNK_SIZE * CHUNK_SIZE;
    public static final int CHUNK_CUBE = CHUNK_SQR * CHUNK_SIZE;

}
