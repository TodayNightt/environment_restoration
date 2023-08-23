package com.game.common;

import com.game.common.Utils.FileUtils;

import java.util.List;

public abstract class ShaderProgram {

    protected Object programId;

    protected List<Object> shaderModules;

    public abstract void bind();

    public abstract void unbind();

    public abstract void cleanup();

    /**
     * @param shaderCode the source code of the shader
     * @param shaderType type of shader
     * @return Object (LWJGL: int , WebGL : WebGLShader)
     */
    protected abstract Object createShader(String shaderCode, int shaderType);

    protected abstract void link(List<Object> shaderModules);

    public Object getProgramId() {
        return programId;
    }

    public record ShaderData(String shaderSource, int shaderType) {
        public static ShaderData createShaderByFile(String filePath, int shaderType) {
            return new ShaderData(FileUtils.loadShaderFromResources(filePath), shaderType);
        }
    }

}
