package Graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import com.jogamp.opengl.GL3;

import static com.jogamp.common.nio.Buffers.newDirectFloatBuffer;
import static com.jogamp.common.nio.Buffers.newDirectIntBuffer;
import static com.jogamp.opengl.GL3.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL3.GL_FLOAT;
import static com.jogamp.opengl.GL3.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL3.GL_ELEMENT_ARRAY_BUFFER;;

public class Mesh {
    private IntBuffer vao, vbo;
    private int numVertices;
    // private GL3 gl;

    public Mesh(GL3 gl, float[] positions, int[] indices) {
        // this.gl = gl;
        this.numVertices = indices.length;
        // Create vao
        vao = newDirectIntBuffer(3);
        gl.glGenVertexArrays(1, vao);
        gl.glBindVertexArray(vao.get(0));

        // Create vbo buffer
        vbo = newDirectIntBuffer(3);
        gl.glGenBuffers(3, vbo);

        // Add vbo[0] for vertices
        FloatBuffer vertexDataBuffer = newDirectFloatBuffer(positions);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        // the * 4 is use to calculate the size of the buffer as float or int is 4 bytes
        gl.glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * Float.BYTES, vertexDataBuffer, GL_STATIC_DRAW);
        // Enable the vertex data attribute
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 5, 0);
        // Enable uvCordinates embedded in the buffer
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 5, Float.BYTES * 3);

        // Create vbo[1] for textCoord

        // Crete vbo for color
        IntBuffer indicesBuffer = newDirectIntBuffer(indices);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.get(2));
        gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * Integer.BYTES, indicesBuffer,
                GL_STATIC_DRAW);

        // Clear the Buffer
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
        vertexDataBuffer = null;
        indicesBuffer = null;

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