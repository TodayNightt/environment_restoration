package com.game.lwjgl.Graphics;

import com.game.common.Mesh;
import com.game.common.Terrain.Chunk;
import com.game.common.Terrain.TerrainMap;

import java.nio.IntBuffer;

import static com.game.common.MeshType.TERRAIN;
import static com.game.lwjgl.Graphics.LwjglMeshFactory.createMesh;

public class LwjglChunk extends Chunk {
    public LwjglChunk(int x, int y, int z, int[] heightMap, TerrainMap parent) {
        init(x, y, z, heightMap, parent);
    }

    @Override
    public Mesh generateMesh(IntBuffer vertexBuffer, IntBuffer indicesBuffer) {
        return createMesh(TERRAIN, vertexBuffer, indicesBuffer);
    }
}
