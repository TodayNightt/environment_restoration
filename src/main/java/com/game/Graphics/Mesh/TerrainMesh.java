package com.game.Graphics.Mesh;

import com.game.templates.Mesh;
import org.joml.Vector3f;

import java.nio.IntBuffer;


public class TerrainMesh extends Mesh {


    public TerrainMesh(IntBuffer positions, IntBuffer indices) {
        this.numVertices = indices.capacity();
        bindBuffer(positions, indices);
    }

    @Override
    public Vector3f getSize() {
        return null;
    }


}