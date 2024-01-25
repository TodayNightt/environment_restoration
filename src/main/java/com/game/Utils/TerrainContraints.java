package com.game.Utils;

public class TerrainContraints {
    public static final int MAP_SIZE = 20;
    public static final int CHUNK_SIZE = 15;
    /* NOTE: The max CHUNK_HEIGHT can only be 63 since the data sent to the GPU
             for y-axis are a 6-bit number
    * */
    public static final int CHUNK_HEIGHT = 63;
    public static final int SEA_LEVEL = 10;
    public static final int MOUNTAIN_LEVEL = CHUNK_HEIGHT - 6;
    public static final int CHUNK_SQR = CHUNK_SIZE * CHUNK_SIZE;
    public static final int CHUNK_CUBE = CHUNK_SQR * CHUNK_SIZE;

}
