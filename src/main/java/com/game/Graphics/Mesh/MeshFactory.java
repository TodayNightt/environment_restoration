package com.game.Graphics.Mesh;

import com.game.templates.Mesh;
import com.game.templates.Mesh.MeshType;

import java.nio.IntBuffer;

import static com.game.templates.Mesh.MeshType.QUAD;

public class MeshFactory {

    public static Mesh createMesh(MeshType type, IntBuffer positionBuffer, IntBuffer indexBuffer) {
        return switch (type) {
            case TERRAIN -> new TerrainMesh(positionBuffer, indexBuffer);
            case QUAD -> new QuadMesh(positionBuffer);
            case PIECE -> new PieceMesh(positionBuffer, indexBuffer);
        };
    }

    public static Mesh createQuadMesh(int posX, int posY, int size) {
        int[] positions = new int[] {
                posX << 7 | posY << 2,
                (posX + size) << 7 | posY << 2 | 1,
                posX << 7 | (posY + size) << 2 | 2,
                (posX + size) << 7 | (posY + size) << 2 | 3
        };
        IntBuffer vertexBuffer = IntBuffer.wrap(positions);
        return createMesh(QUAD, vertexBuffer, null);
    }
}
