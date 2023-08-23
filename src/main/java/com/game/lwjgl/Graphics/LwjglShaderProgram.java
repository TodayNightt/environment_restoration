package com.game.lwjgl.Graphics;

import com.game.common.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;


public class LwjglShaderProgram extends ShaderProgram {


    public LwjglShaderProgram(List<ShaderData> shaderDataList) {
        programId = glCreateProgram();
        if ((int) programId == 0) {
            throw new RuntimeException("Could not create Shader");
        }
        shaderModules = new ArrayList<>();
        shaderDataList.forEach(s -> shaderModules.add(createShader(s.shaderSource(), s.shaderType())));
        link(shaderModules);

    }

    @Override
    protected Object createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);


        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException(
                    "Error compiling Shader code: " + shaderCode + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader((int) programId, shaderId);
        return shaderId;
    }

    @Override
    protected void link(List<Object> shaderModules) {
        glLinkProgram((int) programId);
        if (glGetProgrami((int) programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetShaderInfoLog((int) programId, 1024));
        }

        shaderModules.forEach(s -> {
            glDetachShader((int) programId, (Integer) s);
            glDeleteShader((Integer) s);
        });
    }

    @Override
    public void bind() {
        glUseProgram((int) programId);
    }

    @Override
    public void unbind() {
        glUseProgram(0);
    }

    @Override
    public void cleanup() {
        unbind();
        shaderModules.forEach(s -> glDetachShader((int) programId, (Integer) s));
        if ((int) programId != 0) {
            glDeleteProgram((int) programId);
        }
    }


}
