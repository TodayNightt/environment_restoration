package com.game.Graphics.Gui;

import com.game.templates.Mesh;

import java.nio.IntBuffer;

import static com.game.Graphics.MeshFactory.createMesh;
import static com.game.templates.Mesh.MeshType.QUAD;

public class MiniMap {
    private Mesh mesh;

    public MiniMap(int posX, int posY, int size) {
        int[] positions = new int[]{
                posX << 7 | posY << 2,
                (posX + size) << 7 | posY << 2 | 1,
                posX << 7 | (posY + size) << 2 | 2,
                (posX + size) << 7 | (posY + size) << 2 | 3
        };
        IntBuffer vertexBuffer = IntBuffer.wrap(positions);
        mesh = createMesh(QUAD,vertexBuffer,null);
    }


    public Mesh getMesh() {
        return mesh;
    }


}