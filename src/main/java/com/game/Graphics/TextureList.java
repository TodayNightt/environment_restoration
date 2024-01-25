package com.game.Graphics;


import org.lwjgl.opengl.GL33;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.BufferUtils.createIntBuffer;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

public class TextureList {
    protected static TextureList instance;

    private final HashMap<String, Integer> textureList;

    public TextureList() {
        this.textureList = new HashMap<>();
    }


    public static TextureList getInstance() {
        if (instance == null) {
            instance = new TextureList();
        }
        return instance;
    }


    public void createTexture(String name, ByteBuffer buffer) {
        IntBuffer w = createIntBuffer(1);
        IntBuffer h = createIntBuffer(1);
        IntBuffer channels = createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer textureBuffer = stbi_load_from_memory(buffer, w, h, channels, 4);
        if (textureBuffer == null) {
            throw new RuntimeException("Texture [" + name + "] not loaded: " + stbi_failure_reason());
        }
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 4);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        getInstance().textureList.put(name, textureId);
        stbi_image_free(textureBuffer);

    }


    public int getTexture(String name) {
        return textureList.get(name);
    }


    public void bind(String name) {
        glBindTexture(GL_TEXTURE_2D, textureList.get(name));
    }


    public void cleanup() {
        textureList.values().forEach(GL33::glDeleteTextures);
    }


}