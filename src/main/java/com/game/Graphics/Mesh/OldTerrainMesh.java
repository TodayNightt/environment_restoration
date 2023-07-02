package com.game.Graphics.Mesh;

import com.game.Graphics.Mesh.Mesh;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;


public class OldTerrainMesh implements Mesh {
    private final int numVertices;
    private final int vao;

    public OldTerrainMesh(float[] positions, int[] indices) {

        this.numVertices = indices.length;
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Create vbo buffer
        IntBuffer vbos = IntBuffer.allocate(2);
        int vbo = glGenBuffers();
        vbos.put(vbo);
        vbo = glGenBuffers();
        vbos.put(vbo);

        // Add vbo[0] for vertices
        FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(positions.length);
        vertexDataBuffer.put(0, positions);
        glBindBuffer(GL_ARRAY_BUFFER, vbos.get(0));
        // the * 4 is used to calculate the size of the buffer as float or int is 4 bytes
        glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
        // Enable the vertex data attribute
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES*7, 0);
        // Enable uvCoordinates embedded in the buffer
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 7, Float.BYTES * 3);
        //Enable uvOffset embedded in the buffer
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, Float.BYTES * 7, Float.BYTES * 5);

        // Crete vbo for indices
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(0, indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbos.get(1));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        // Clear the Buffer
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }


    @Override
    public void cleanup() {
        glDeleteBuffers(vao);
    }

    @Override
    public int getNumVertices() {
        return numVertices;
    }

    @Override
    public int getVao() {
        return vao;
    }

    public String toString() {
        return String.format("vao: %s, numOfVertices: %d", vao, numVertices);
    }

}