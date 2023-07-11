package com.game.Graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.management.RuntimeErrorException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class UniformsMap {
    private final int programId;
    private final Map<String, Integer> uniforms;

    public UniformsMap(int programId) {
        this.programId = programId;
        uniforms = new HashMap<>();
    }

    private int getUniformLocation(String uniformName) {
        Integer location = uniforms.get(uniformName);
        if (location == null) {
            throw new RuntimeException(String.format("Could not find uniform [%s]",uniformName));
        }
        return location;
    }

    public void setUniform(String name, Matrix4f value) {
        float[] valueF = new float[16];
        value.get(valueF);
        glUniformMatrix4fv(getUniformLocation(name), false, valueF);

    }

    public void setUniform(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, Vector3f value) {
        glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector2f value) {
        glUniform2f(getUniformLocation(name), value.x, value.y);
    }

    public void setUniform(String name, Color value) {
        glUniform3f(getUniformLocation(name), value.getRed(), value.getGreen(), value.getBlue());
    }
    public void setUniform(String name, float[] value) {
        glUniform1fv(getUniformLocation(name),value);
    }

    public void createUniform(String name) {
        int uniformLocation = glGetUniformLocation(programId, name);
        if (uniformLocation < 0) {
            throw new RuntimeErrorException(
                    null, String.format("Could not find uniform [%s] in shader program [%d]", name, programId));
        }
        uniforms.put(name, uniformLocation);
    }
}
