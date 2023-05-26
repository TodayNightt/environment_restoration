package Graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import com.jogamp.opengl.GL3;

import static com.jogamp.common.nio.Buffers.newDirectFloatBuffer;
import static com.jogamp.common.nio.Buffers.newDirectIntBuffer;
import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;

public class Mesh {
    protected int numVertices;
    protected IntBuffer vao;
    protected IntBuffer vbo;
    protected GL3 gl;

    public Mesh(float[] positions, float[] colors, int[] indices, GL3 gl) {
        this.gl = gl;
        this.numVertices = indices.length;
        // Create VAO;
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
        gl.glBufferData(GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * 4, vertexDataBuffer, GL_STATIC_DRAW);
        // Enable the vertex data attribute
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Create vbo[1] for color
        FloatBuffer colorDataBuffer = newDirectFloatBuffer(colors);
        gl.glBindBuffer(GL_ARRAY_BUFFER, vbo.get(1));
        gl.glBufferData(GL_ARRAY_BUFFER, colorDataBuffer.capacity() * 4, colorDataBuffer, GL_STATIC_DRAW);
        // Enable the color data attribute
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

        // Crete vbo for color
        IntBuffer indicesBuffer = newDirectIntBuffer(indices);
        gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo.get(2));
        gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity() * 4, indicesBuffer, GL_STATIC_DRAW);

        // Clear the Buffer
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
        vertexDataBuffer = null;
        colorDataBuffer = null;
        indicesBuffer = null;

    }

    // public void cleanup() {
    // vboIdList.stream().forEach(GL4::glDeleteBuffers);
    // gl.glDeleteVertexArrays(vaoId);
    // }

    public int getNumVertices() {
        return numVertices;
    }

    public IntBuffer getVao() {
        return vao;
    }
}