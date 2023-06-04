package Graphics;

import com.jogamp.opengl.GL3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static com.jogamp.common.nio.Buffers.newDirectFloatBuffer;
import static com.jogamp.common.nio.Buffers.newDirectIntBuffer;
import static com.jogamp.opengl.GL3.*;

public class Mesh {
    private final IntBuffer vao;
    private final int numVertices;

    public Mesh(GL3 gl, float[] positions, int[] indices) {
        // this.gl = gl;
        this.numVertices = indices.length;
        // Create vao
        vao = newDirectIntBuffer(3);
        gl.glGenVertexArrays(1, vao);
        gl.glBindVertexArray(vao.get(0));

        // Create vbo buffer
        IntBuffer vbo = newDirectIntBuffer(3);
        gl.glGenBuffers(3, vbo);

        // Add vbo[0] for vertices
        FloatBuffer vertexDataBuffer = newDirectFloatBuffer(positions);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        // the * 4 is used to calculate the size of the buffer as float or int is 4 bytes
        gl.glBufferData(GL_ARRAY_BUFFER, (long) vertexDataBuffer.capacity() * Float.BYTES, vertexDataBuffer, GL_STATIC_DRAW);
        // Enable the vertex data attribute
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 7, 0);
        // Enable uvCoordinates embedded in the buffer
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 7, Float.BYTES * 3);
        //Enable uvOffset embedded in the buffer
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 2, GL_FLOAT, false, Float.BYTES * 7, Float.BYTES * 5);

        // Create vbo[1] for textCoord

        // Crete vbo for color
        IntBuffer indicesBuffer = newDirectIntBuffer(indices);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.get(2));
        gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, (long) indicesBuffer.capacity() * Integer.BYTES, indicesBuffer,
                GL_STATIC_DRAW);

        // Clear the Buffer
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);

    }

    // public void cleanup() {
    // gl.glDeleteBuffers(GL_ARRAY_BUFFER, vbo);
    // }

    public int getNumVertices() {
        return numVertices;
    }

    public IntBuffer getVao() {
        return vao;
    }

}