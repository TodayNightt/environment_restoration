package GUI;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import imgui.ImDrawData;

import java.nio.IntBuffer;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;

public class UiMesh {
    private IntBuffer vao;
    private IntBuffer vbo;
    private GL3 gl;

    public UiMesh(GL3 gl){
        vao = GLBuffers.newDirectIntBuffer(1);
        gl.glGenVertexArrays(1,vao);

        vbo = GLBuffers.newDirectIntBuffer(2);
        gl.glGenBuffers(2,vbo);

        gl.glBindBuffer(GL_ARRAY_BUFFER,vbo.get(0));
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0,2,GL3.GL_FLOAT,false, ImDrawData.SIZEOF_IM_DRAW_VERT,0);

        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1,2,GL3.GL_FLOAT,false,ImDrawData.SIZEOF_IM_DRAW_VERT,8);

        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2,4, GL_UNSIGNED_BYTE,true,ImDrawData.SIZEOF_IM_DRAW_VERT,16);

        gl.glBindBuffer(GL_ARRAY_BUFFER,vbo.get(1));

        gl.glBindVertexArray(0);
    }

    public IntBuffer getVao(){
        return vao;
    }

    public IntBuffer getVbo(){
        return vbo;
    }
}
