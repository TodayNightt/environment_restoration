package com.game.templates;


import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public abstract class Mesh {
    public enum MeshType {
        TERRAIN, QUAD, PIECE
    }

    protected int vao;
    protected int numVertices;


    public void bindBuffer(IntBuffer positions, IntBuffer indices) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Create vbo buffer
        IntBuffer vbos = IntBuffer.allocate(2);
        int vbo = glGenBuffers();
        vbos.put(vbo);
        vbo = glGenBuffers();
        vbos.put(vbo);

        // Add vbo[0] for vertices
        IntBuffer vertexDataBuffer = BufferUtils.createIntBuffer(positions.capacity());
        vertexDataBuffer.put(0, positions, 0, positions.capacity());
        glBindBuffer(GL_ARRAY_BUFFER, vbos.get(0));
        // the * 4 is used to calculate the size of the buffer as float or int is 4 bytes
        glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
        // Enable the vertex data attribute
        glEnableVertexAttribArray(0);
        glVertexAttribIPointer(0, 1, GL_UNSIGNED_INT, Integer.BYTES, 0);

        // Crete vbo for indices
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.capacity());
        indexBuffer.put(0, indices, 0, indices.capacity());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbos.get(1));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        // Clear the Buffer
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getVao() {
        return vao;
    }

    public String toString() {
        return String.format("vao: %s, numOfVertices: %d", vao, numVertices);
    }

    public void cleanup() {
        glDeleteBuffers(vao);
    }

    public abstract Vector3f getSize();

}
