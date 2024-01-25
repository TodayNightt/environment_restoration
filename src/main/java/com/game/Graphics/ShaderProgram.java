package com.game.Graphics;

import com.game.Utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;


public class ShaderProgram {


    protected final int programId;

    protected List<Integer> shaderModules;


    public ShaderProgram(List<ShaderData> shaderDataList) {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Could not create Shader");
        }
        shaderModules = new ArrayList<>();
        shaderDataList.forEach(s -> shaderModules.add(createShader(s.shaderSource(), s.shaderType())));
        link(shaderModules);

    }


    protected int createShader(String shaderCode, int shaderType) {
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

        glAttachShader(programId, shaderId);
        return shaderId;
    }


    protected void link(List<Integer> shaderModules) {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetShaderInfoLog(programId, 1024));
        }

        shaderModules.forEach(s -> {
            glDetachShader(programId, s);
            glDeleteShader(s);
        });
    }


    public void bind() {
        glUseProgram(programId);
    }


    public void unbind() {
        glUseProgram(0);
    }

    public int getProgramId() {
        return programId;
    }


    public void cleanup() {
        unbind();
        shaderModules.forEach(s -> glDetachShader(programId, s));
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }


    public record ShaderData(String shaderSource, int shaderType) {
        public static ShaderData createShaderByFile(String filePath, int shaderType) {
            return new ShaderData(FileUtils.loadShaderFromResources(filePath), shaderType);
        }
    }

}
