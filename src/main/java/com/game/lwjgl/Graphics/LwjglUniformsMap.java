package com.game.lwjgl.Graphics;

import com.game.common.UniformsMap;
import com.game.common.Utils.Mat4f;

import javax.management.RuntimeErrorException;
import java.awt.*;
import java.util.HashMap;

import static org.lwjgl.opengl.GL33.*;

public class LwjglUniformsMap extends UniformsMap {


    public LwjglUniformsMap(int programId) {
        this.programId = programId;
        uniforms = new HashMap<>();
    }

    @Override
    protected Object getUniformLocation(String uniformName) {
        Integer location = (Integer) uniforms.get(uniformName);
        if (location == null) {
            throw new RuntimeException(String.format("Could not find uniform [%s]", uniformName));
        }
        return location;
    }

//    @Override
//    public void setMatrixUniform(String name, Matrix4f value) {
//        float[] valueF = new float[16];
//        value.get(valueF);
//        glUniformMatrix4fv((Integer) getUniformLocation(name), false, valueF);
//
//    }

    @Override
    public void setUniform(String name, Mat4f value) {
        glUniformMatrix4fv((Integer) getUniformLocation(name), false, value.get());
    }

    @Override
    public void setUniform(String name, int value) {
        glUniform1i((Integer) getUniformLocation(name), value);
    }

    @Override
    public void setUniform(String name, float value) {
        glUniform1f((Integer) getUniformLocation(name), value);
    }


    @Override
    public void setUniform(String name, float value1, float value2) {
        glUniform2f((Integer) getUniformLocation(name), value1, value2);
    }

    public void setUniform(String name, Color value) {
        glUniform3f((Integer) getUniformLocation(name), value.getRed(), value.getGreen(), value.getBlue());
    }

    @Override
    public void setUniform(String name, float[] value) {
        glUniform1fv((Integer) getUniformLocation(name), value);
    }

    @Override
    public void setUniform(String name, int[] value) {
        glUniform3i((Integer) getUniformLocation(name), value[0], value[1], value[2]);
    }

    @Override
    public void createUniform(String name) {
        int uniformLocation = glGetUniformLocation((int) programId, name);
        if (uniformLocation < 0) {
            throw new RuntimeErrorException(
                    null, String.format("Could not find uniform [%s] in shader program [%d]", name, programId));
        }
        uniforms.put(name, uniformLocation);
    }
}
