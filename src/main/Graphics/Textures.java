//package Graphics;
//
//import com.jogamp.opengl.GL3;
//import com.jogamp.opengl.GLException;
//import com.jogamp.opengl.util.texture.Texture;
//import com.jogamp.opengl.util.texture.TextureIO;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.IntBuffer;
//import java.util.HashMap;
//
//import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
//import static org.lwjgl.opengl.GL30.glGenerateMipmap;
//
//public class Textures {
//
//    private final HashMap<String, Texture> textureList;
//    IntBuffer textureName;
//
//    public Textures() {
//        this.textureName = IntBuffer.allocate(1);
//        this.textureList = new HashMap<>();
//    }
//
//    public void createTexture(String name, String filePath, boolean mipMap) throws GLException, IOException {
//        Texture texture = TextureIO.newTexture(new File(filePath), true);
//        texture.getTextureObject();
//        texture.setTexParameteri(gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);
//        texture.setTexParameteri(gl,GL3.GL_TEXTURE_MIN_FILTER,GL3.GL_NEAREST_MIPMAP_NEAREST);
//        glGenerateMipmap(GL_TEXTURE_2D);
//        textureList.put(name, texture);
//    }
//
//    public Texture getTexture(String name) {
//        return textureList.get(name);
//    }
//
//    public void bind(String name) {
//        textureList.get(name).bind(gl);
//    }
//
//    public IntBuffer getTexture() {
//        return textureName;
//    }
//
//}