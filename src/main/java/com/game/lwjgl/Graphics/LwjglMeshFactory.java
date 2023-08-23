package com.game.lwjgl.Graphics;

import com.game.common.Mesh;
import com.game.common.MeshType;
import com.game.lwjgl.Graphics.Mesh.LwjglPieceMesh;
import com.game.lwjgl.Graphics.Mesh.LwjglQuadMesh;
import com.game.lwjgl.Graphics.Mesh.LwjglTerrainMesh;

import java.nio.IntBuffer;

public class LwjglMeshFactory {

    public static Mesh createMesh(MeshType type, IntBuffer positionBuffer, IntBuffer indexBuffer) {
        return switch (type) {
            case TERRAIN -> new LwjglTerrainMesh(positionBuffer, indexBuffer);
            case QUAD -> new LwjglQuadMesh(positionBuffer);
            case PIECE -> new LwjglPieceMesh(positionBuffer, indexBuffer);
        };
    }
}
