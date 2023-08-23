package com.game.common;

import com.game.common.Utils.Mat4f;

import java.util.Map;

public abstract class UniformsMap {
    protected Object programId;
    protected Map<String, Object> uniforms;

    protected abstract Object getUniformLocation(String uniformName);

//    public abstract void setMatrixUniform(String name, Matrix4f value);

    public abstract void setUniform(String name, Mat4f value);

    public abstract void setUniform(String name, int value);

    public abstract void setUniform(String name, float value);

//    public abstract void setUniform(String name, Vector3f value);
//
//    public abstract void setUniform(String name, Vector2f value);

    public abstract void setUniform(String name, float value1, float value2);


//    public abstract void setUniform(String name, Color value);

    public abstract void setUniform(String name, float[] value);

    /**
     * @param name  attribute name
     * @param value a size 3 int array for piece size
     * @return void
     */
    public abstract void setUniform(String name, int[] value);

    public abstract void createUniform(String name);
}
