package com.game.Graphics.Mesh;

import com.game.templates.Mesh;
import org.joml.Vector3f;

import java.nio.IntBuffer;

public class QuadMesh extends Mesh {

    public QuadMesh(IntBuffer positions) {
        IntBuffer indices = IntBuffer.wrap(new int[]{
                0, 1, 2, 1, 3, 2
        });
        this.numVertices = indices.capacity();
        bindBuffer(positions, indices);
    }


    @Override
    public Vector3f getSize() {
        return null;
    }
}
