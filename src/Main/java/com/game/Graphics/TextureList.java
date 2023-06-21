package com.game.Graphics;

import com.game.Utils.FileUtils;
import org.lwjgl.opengl.GL33;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.BufferUtils.createIntBuffer;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class TextureList {

    private final HashMap<String, Integer> textureList;

    public TextureList() {
        this.textureList = new HashMap<>();
    }

    public void createTexture(String name, String filePath) throws URISyntaxException {
        File file = new File(FileUtils.loadFromResources(filePath).toUri());
        IntBuffer w = createIntBuffer(1);
        IntBuffer h = createIntBuffer(1);
        IntBuffer channels = createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer textureBuffer = stbi_load(file.toString(), w, h, channels, 4);
        if (textureBuffer == null) {
            throw new RuntimeException("Image file [" + filePath + "] not loaded: " + stbi_failure_reason());
        }

        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(), h.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        textureList.put(name, textureId);
        stbi_image_free(textureBuffer);
    }

    public Integer getTexture(String name) {
        return textureList.get(name);
    }

    public void bind(String name) {
        glBindTexture(GL_TEXTURE_2D, textureList.get(name));
    }

    public void cleanup() {
        textureList.values().forEach(GL33::glDeleteTextures);
    }


}