package Graphics;

import com.jogamp.opengl.GL3;
import org.joml.Matrix4f;

import javax.management.RuntimeErrorException;
import java.util.HashMap;
import java.util.Map;

public class UniformsMap {
    private final GL3 gl;
    private final int programId;
    private final Map<String, Integer> uniforms;

    public UniformsMap(int programId, GL3 gl) {
        this.programId = programId;
        uniforms = new HashMap<>();
        this.gl = gl;
    }

    private int getUniformLocation(String uniformName) {
        Integer location = uniforms.get(uniformName);
        if (location == null) {
            throw new RuntimeException("Could not find uniform [" + uniformName + "]");
        }
        return location;
    }

    public void setUniform(String name, Matrix4f value) {
        Integer location = uniforms.get(name);
        if (location == null) {
            throw new RuntimeException("Could not find uniform [" + name + "]");
        }
        float[] valueF = new float[16];
        value.get(valueF);
        gl.glUniformMatrix4fv(location, 1, false, valueF, 0);
    }

    public void setUniform(String name, int value) {
        gl.glUniform1i(getUniformLocation(name), value);
    }
    public void setUniform(String name, float value) {
        gl.glUniform1f(getUniformLocation(name),value);
    }
    public void createUniform(String name) {
        int uniformLocation = gl.glGetUniformLocation(programId, name);
        if (uniformLocation < 0) {
            throw new RuntimeErrorException(
                    null, String.format("Could not find uniform [%s] in shader program [%d]", name, programId));
        }
        uniforms.put(name, uniformLocation);
    }
}
