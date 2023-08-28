package com.game.Graphics;

import com.game.Utils.Mat4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.management.RuntimeErrorException;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class UniformsMap {

    private int programId;

    private Map<String,Integer> uniforms;


    public UniformsMap(int programId,String[] attributes) {
        this.programId = programId;
        uniforms = new HashMap<>();
        for(String attribute : attributes){
            createUniform(attribute);
        }
    }


    protected int getUniformLocation(String uniformName) {
        Integer location =  uniforms.get(uniformName);
        if (location == null) {
            throw new RuntimeException(String.format("Could not find uniform [%s]", uniformName));
        }
        return location;
    }

    public void setUniform(String name, Matrix4f value) {
        float[] valueF = new float[16];
        value.get(valueF);
        glUniformMatrix4fv(getUniformLocation(name), false, valueF);

    }
//    public void setUniform(String name, Mat4f value) {
//
//        glUniformMatrix4fv((Integer) getUniformLocation(name), false, value.get());
//    }


    public void setUniform(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniformU(String name,int value){
        glUniform1ui(getUniformLocation(name),value);
    }




    public void setUniform(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }



    public void setUniform(String name, float value1, float value2) {
        glUniform2f(getUniformLocation(name), value1, value2);
    }

    public void setUniform(String name, Color value) {
        glUniform3f(getUniformLocation(name), value.getRed(), value.getGreen(), value.getBlue());
    }


    public void setUniform(String name, float[] value) {
        glUniform1fv(getUniformLocation(name), value);
    }

    public void setUniform(String name, Vector3f value) {
        glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
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
