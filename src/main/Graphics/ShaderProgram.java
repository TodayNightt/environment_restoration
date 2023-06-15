package Graphics;

import org.lwjgl.opengles.GLES31;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import static org.lwjgl.opengles.GLES31.*;


public class ShaderProgram {

    private final int programId;

    public ShaderProgram( List<ShaderData> shaderDataList) throws Exception {
        programId = glCreateProgram();
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

    // https://stackoverflow.com/questions/60098830/how-to-fix-this-opengl-error-unexpected-tokens-following-preprocessor-directiv
    public static String[] readFile(String filePath) throws IOException {
        Vector<String> lines = new Vector<>();

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(x -> lines.add(x + "\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // CONVERT VECTOR TO ARRAY
        Object[] objArray = lines.toArray();

        return Arrays.copyOf(objArray, objArray.length, String[].class);
    }

    protected int createShader(String[] shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Error creating shader. Type: " + shaderType);
        }
        glShaderSource(shaderId,shaderCode);
        glCompileShader(shaderId);


        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException(
                    "Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);
        return shaderId;
    }

    private void link(List<Integer> shaderModules) {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: "+glGetShaderInfoLog(programId,1024));
        }

        shaderModules.forEach(s -> glDetachShader(programId, s));
        shaderModules.forEach(GLES31::glDeleteShader);
    }

    public int getProgramId() {
        return programId;
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public record ShaderData(String shaderFile, int shaderType) {
    }
}
