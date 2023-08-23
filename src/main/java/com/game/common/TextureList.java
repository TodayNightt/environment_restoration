package com.game.common;

import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class TextureList {

    public HashMap<String, Object> textureList;

    public abstract void createTexture(String name, ByteBuffer buffer);

    public abstract Object getTexture(String name);

    public abstract void bind(String textureName);

    public abstract void cleanup();
}
