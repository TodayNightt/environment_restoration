package Graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import com.jogamp.opengl.GL3;

public class ShaderProgram {

    protected GL3 gl;
    private final int programId;

    public ShaderProgram(GL3 gl, List<ShaderData> shaderDataList) throws Exception {
        this.gl = gl;
        programId = gl.glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }

        List<Integer> shaderModules = new ArrayList<>();
        shaderDataList.forEach(s -> {
            String[] file;
            try {
                file = readFile(s.shaderFile);
                shaderModules.add(createShader(file, s.shaderType));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        link(shaderModules);

    }

    protected int createShader(String[] shaderCode, int shaderType) {
        int shaderId = gl.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }
        gl.glShaderSource(shaderId, shaderCode.length, shaderCode, null);
        gl.glCompileShader(shaderId);

        int[] compiled = new int[1];
        gl.glGetShaderiv(shaderId, GL3.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            gl.glGetShaderInfoLog(shaderId, byteBuffer.capacity(), (IntBuffer) null, byteBuffer);
            throw new RuntimeException(
                    "Error compiling Shader code: " + shaderType + " " + new String(byteBuffer.array()));
        }

        gl.glAttachShader(programId, shaderId);
        return shaderId;
    }

    // https://stackoverflow.com/questions/60098830/how-to-fix-this-opengl-error-unexpected-tokens-following-preprocessor-directiv
    public static String[] readFile(String filePath) throws IOException {
        Vector<String> lines = new Vector<String>();

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(x -> lines.add(x + "\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // CONVERT VECTOR TO ARRAY
        Object[] objArray = lines.toArray();
        String[] array = Arrays.copyOf(objArray, objArray.length, String[].class);

        return array;
    }

    private void link(List<Integer> shaderModules) {
        gl.glLinkProgram(programId);
        IntBuffer buffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(programId, GL3.GL_LINK_STATUS, buffer);
        if (buffer.get(0) == 0) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(GL3.GL_INFO_LOG_LENGTH);
            gl.glGetShaderInfoLog(programId, byteBuffer.capacity(), (IntBuffer) null, byteBuffer);
            throw new RuntimeException("Error linking Shader code: " + byteBuffer.toString());
        }

        shaderModules.forEach(s -> gl.glDetachShader(programId, s));
        shaderModules.forEach(gl::glDeleteShader);
    }

    public int getProgramId() {
        return programId;
    }

    public void bind() {
        gl.glUseProgram(programId);
    }

    public void unbind() {
        gl.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            gl.glDeleteProgram(programId);
        }
    }

    public record ShaderData(String shaderFile, int shaderType) {
    }
}
