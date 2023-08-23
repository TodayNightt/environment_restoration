package com.game.lwjgl.Graphics.Mesh;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.game.common.Mesh;
import com.game.common.Utils.Vec3i;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Map;

import static com.game.common.MeshType.PIECE;
import static com.game.lwjgl.Graphics.LwjglMeshFactory.createMesh;
import static org.lwjgl.opengl.GL33.*;

@JsonDeserialize(builder = LwjglPieceMesh.Builder.class)
public class LwjglPieceMesh implements Mesh {
    protected int vao;
    protected int numVertices;
    private Vec3i size;
    private float[] schematics;

    public LwjglPieceMesh(IntBuffer positions, IntBuffer indices) {
        this.numVertices = indices.capacity();
//        bindBuffer(positions, indices);
    }

    public float[] getSchematics() {
        return schematics;
    }


    @Override
    public void bindBuffer(IntBuffer positions, IntBuffer indices) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            // Create vbo buffer
            int vbo = glGenBuffers();

            // Add vbo[0] for vertices
            IntBuffer vertexDataBuffer = stack.callocInt(positions.capacity());
            vertexDataBuffer.put(positions);
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
            IntBuffer indicesBuffer = stack.mallocInt(indices.capacity());
            indicesBuffer.put(indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Clear the Buffer
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    private void setSchematics(float[] schematics) {
        this.schematics = schematics;
    }

    private void setSize(Vec3i size) {
        this.size = size;
    }


    public int[] getSize() {
        return new int[]{size.x(), size.y(), size.z()};
    }

    @Override
    public void cleanup() {
        glDeleteVertexArrays(vao);
    }


    @Override
    public String toString() {
        return null;
    }


    @Override
    public int getNumVertices() {
        return numVertices;
    }

    @Override
    public int getVao() {
        return vao;
    }

    @JsonPOJOBuilder(buildMethodName = "create", withPrefix = "set")
    static class Builder {
        IntBuffer vertexData;
        IntBuffer indices;
        Vec3i size;
        float[] schematics;

        Builder setVertexData(int[] vertexData) {
            this.vertexData = IntBuffer.wrap(vertexData);
            return this;
        }

        Builder setIndices(int[] indices) {
            this.indices = IntBuffer.wrap(indices);
            return this;
        }

        Builder setSize(Map<String, Integer> size) {
            this.size = new Vec3i(size.get("width"), size.get("height"), size.get("depth"));
            return this;
        }

        Builder setSchematics(float[] schematics) {
            this.schematics = schematics;
            return this;
        }

        public LwjglPieceMesh create() {
            LwjglPieceMesh mesh = (LwjglPieceMesh) createMesh(PIECE, vertexData, indices);
            mesh.setSize(size);
            mesh.setSchematics(schematics);
            return mesh;
        }
    }
}
