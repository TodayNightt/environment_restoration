package com.game.Graphics;

import com.game.Graphics.Mesh.TerrainMesh;
import com.game.Graphics.Mesh.PieceMesh;
import com.game.templates.Mesh;
import com.game.templates.Mesh.MeshType;
import com.game.Graphics.Mesh.QuadMesh;

import java.nio.IntBuffer;

public class MeshFactory {

    public static Mesh createMesh(MeshType type, IntBuffer positionBuffer, IntBuffer indexBuffer) {
        return switch (type) {
            case TERRAIN -> new TerrainMesh(positionBuffer, indexBuffer);
            case QUAD -> new QuadMesh(positionBuffer);
            case PIECE -> new PieceMesh(positionBuffer, indexBuffer);
        };
    }
}
