package com.game.Graphics.Mesh;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.game.templates.Mesh;
import com.game.Utils.Vec3i;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Map;

import static com.game.Utils.BitsUtils.*;
import static com.game.Graphics.MeshFactory.createMesh;
import static com.game.templates.Mesh.MeshType.PIECE;
import static org.lwjgl.opengl.GL33.*;

@JsonDeserialize(builder = PieceMesh.Builder.class)
public class PieceMesh extends Mesh {

    private Vector3f size;
    private float[] schematics;

    public PieceMesh(IntBuffer vertexData, IntBuffer indices) {
        this.numVertices = indices.capacity();
        bindBuffer(vertexData, indices);
    }

    public float[] getSchematics() {
        return schematics;
    }


    private void setSchematics(float[] schematics) {
        this.schematics = schematics;
    }

    private void setSize(Vector3f size) {
        this.size = size;
    }

    @Override
    public Vector3f getSize() {
        return size;
    }

    @JsonPOJOBuilder(buildMethodName = "create", withPrefix = "set")
    static class Builder {
        IntBuffer vertexData;
        IntBuffer indices;
        Vector3f size;
        float[] schematics;

        Builder setVertexData(float[] vertexData) {
            this.vertexData = fiveFloatsToOneInt(vertexData);
            return this;
        }

        Builder setIndices(int[] indices) {
            this.indices = IntBuffer.wrap(indices);
            return this;
        }

        Builder setSize(Map<String, Float> size) {
            this.size = new Vector3f(size.get("width"), size.get("height"), size.get("depth"));
            return this;
        }

        Builder setSchematics(float[] schematics) {
            this.schematics = schematics;
            return this;
        }

        public PieceMesh create() {
            PieceMesh mesh = (PieceMesh) createMesh(PIECE, vertexData, indices);
            mesh.setSize(size);
            mesh.setSchematics(schematics);
            return mesh;
        }
    }
}
