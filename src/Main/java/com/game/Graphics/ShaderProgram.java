package com.game.Graphics;

import com.game.Utils.FileUtils;
import org.lwjgl.opengl.GL33;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;


public class ShaderProgram {

    private final int programId;
    private List<Integer> shaderModules;

    public ShaderProgram(List<ShaderData> shaderDataList) throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        shaderModules = new ArrayList<>();
        shaderDataList.forEach(s -> shaderModules.add(createShader(s.shaderSource(), s.shaderType)));
        link(shaderModules);

    }

//    // https://stackoverflow.com/questions/60098830/how-to-fix-this-opengl-error-unexpected-tokens-following-preprocessor-directiv
//    public static String[] readFile(String filePath) throws IOException {
//        Vector<String> lines = new Vector<>();
//
//        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
//            stream.forEach(x -> lines.add(x + "\n"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // CONVERT VECTOR TO ARRAY
//        Object[] objArray = lines.toArray();
//
//        return Arrays.copyOf(objArray, objArray.length, String[].class);
//    }

    protected int createShader(CharSequence shaderCode, int shaderType) {
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

    private void link(List<Integer> shaderModules) {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking Shader code: " + glGetShaderInfoLog(programId, 1024));
        }

        shaderModules.forEach(s -> glDetachShader(programId, s));
        shaderModules.forEach(GL33::glDeleteShader);
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
        shaderModules.forEach(s -> glDetachShader(programId, s));
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public record ShaderData(CharSequence shaderSource, int shaderType) {
        public static ShaderData createShaderByFile(String filePath, int shaderType) throws IOException, URISyntaxException {
            return new ShaderData(Files.readString(FileUtils.loadFromResources(filePath)), shaderType);
        }
    }

}
