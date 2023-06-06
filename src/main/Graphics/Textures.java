package Graphics;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;

public class Textures {

    IntBuffer textureName;
    private final GL3 gl;
    private final HashMap<String, Texture> textureList;

    public Textures(GL3 gl) {
        this.gl = gl;
        this.textureName = IntBuffer.allocate(1);
        this.textureList = new HashMap<>();
    }

    public void createTexture(String name, String filePath, boolean mipMap) throws GLException, IOException {
        Texture texture = TextureIO.newTexture(new File(filePath), true);
        texture.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);
        texture.setTexParameteri(gl,GL3.GL_TEXTURE_MIN_FILTER,GL3.GL_NEAREST_MIPMAP_NEAREST);
        gl.glGenerateMipmap(GL3.GL_TEXTURE_2D);
        textureList.put(name, texture);
    }

    public Texture getTexture(String name) {
        return textureList.get(name);
    }

    public void bind(String name) {
        textureList.get(name).bind(gl);
    }

    public IntBuffer getTexture() {
        return textureName;
    }

}