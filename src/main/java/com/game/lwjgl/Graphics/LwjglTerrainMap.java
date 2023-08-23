package com.game.lwjgl.Graphics;

import com.game.common.Scene;
import com.game.common.Terrain.Chunk;
import com.game.common.Terrain.TerrainMap;

public class LwjglTerrainMap extends TerrainMap {

    public LwjglTerrainMap(String texture, Scene scene) {
        init(texture, scene);
    }

    @Override
    public Chunk createChunk(int x, int y, int z, int[] heightMap, TerrainMap parent) {
        return new LwjglChunk(x, y, z, heightMap, parent);
    }
}
