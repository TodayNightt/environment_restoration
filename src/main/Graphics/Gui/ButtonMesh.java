package Graphics.Gui;

import Graphics.Mesh;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.opengles.GLES30.*;

public class ButtonMesh implements Mesh {
    private final int vao;
    private final int numVertices;

    public ButtonMesh(float[] positions, int[] indices) {
        this.numVertices = indices.length;
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
            glVertexAttribPointer(0, 2, GL_FLOAT, false, Float.BYTES * 2, 0);
//            glEnableVertexAttribArray(1);
//            glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 5, Float.BYTES * 3);

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

    protected static ButtonMesh createSquare(float leftMost, float topMost, float buttonSize) {
        final int[] indices = new int[]{
                0, 1, 2, 1, 2, 3
        };
        final float[] vertexData = new float[]{
                leftMost, topMost,
                leftMost + buttonSize, topMost,
                leftMost, topMost + buttonSize,
                leftMost + buttonSize, topMost + buttonSize
        };
        return new ButtonMesh(vertexData, indices);
    }

    @Override
    public int getNumVertices() {
        return numVertices;
    }

    @Override
    public int getVao() {
        return vao;
    }

    @Override
    public void cleanup() {
        glDeleteBuffers(vao);
    }
}
