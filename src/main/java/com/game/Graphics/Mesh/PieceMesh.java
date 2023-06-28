package com.game.Graphics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

@JsonDeserialize(builder = PieceMesh.Builder.class)
public class PieceMesh implements Mesh {
    private final int numVertices;
    private final int vao;
    private final Vector3f size;

    public PieceMesh(float[] positions, int[] indices, Vector3f size) {
        this.numVertices = indices.length;
        this.size = size;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            // Create vbo buffer
            int vbo = glGenBuffers();

            // Add vbo[0] for vertices
            FloatBuffer vertexDataBuffer = stack.callocFloat(positions.length);
            vertexDataBuffer.put(0, positions);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            // the * 4 is used to calculate the size of the buffer as float or int is 4 bytes
            glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer, GL_STATIC_DRAW);
            // Enable the vertex data attribute
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 5, 0);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 5, Float.BYTES * 3);

            // Crete vbo for color
            vbo = glGenBuffers();
            IntBuffer indicesBuffer = stack.mallocInt(indices.length);
            indicesBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Clear the Buffer
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    @Override
    public int getNumVertices() {
        return numVertices;
    }

    @Override
    public int getVao() {
        return vao;
    }

    public Vector3f getSize() {
        return size;
    }

    @Override
    public void cleanup() {
        glDeleteVertexArrays(vao);
    }

    @JsonPOJOBuilder(buildMethodName = "create", withPrefix = "set")
    static class Builder {
        float[] vertexData;
        int[] indices;
        Vector3f size;

        Builder setVertexData(float[] vertexData) {
            this.vertexData = vertexData;
            return this;
        }

        Builder setIndices(int[] indices) {
            this.indices = indices;
            return this;
        }

        Builder setSize(Map<String, Integer> size) {
            this.size = new Vector3f(size.get("width"), size.get("height"), size.get("depth"));
            return this;
        }

        public PieceMesh create() {
            return new PieceMesh(vertexData, indices, size);
        }
    }
}
