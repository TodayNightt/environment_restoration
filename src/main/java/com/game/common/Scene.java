package com.game.common;

import com.game.common.Camera.Camera;
import com.game.common.Graphics.Gui.GuiScene;
import com.game.common.Terrain.TerrainMap;

import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class Scene {
    public static boolean isWebGL;
    protected TextureList textureList;
    protected TerrainMap terrain;
    protected HashMap<String, ShaderProgram> shaderProgramList;
    protected HashMap<String, UniformsMap> uniformsMapList;
    protected Camera cam;
    protected Object window;
    protected GuiScene guiScene;

    public void init() {
        this.shaderProgramList = new HashMap<>();
        this.uniformsMapList = new HashMap<>();
        initCam();
        initGui();
        initializeTexture();
//        initializePiece();
        initializeTerrainGen();
    }

    protected abstract void initCam();

    protected abstract void initializeTexture();

    protected abstract void initGui();

    protected abstract void initializeTerrainGen();

    protected abstract void initializePiece();

    public abstract void createTexture(String name, ByteBuffer buffer);

    public abstract void cleanup();

    public abstract void render();

    public Camera getCamera() {
        return cam;
    }

    public ShaderProgram getShaderProgram(String name) {
        return shaderProgramList.get(name);
    }

    public TerrainMap getTerrain() {
        return terrain;
    }

    public UniformsMap getUniformMap(String name) {
        return uniformsMapList.get(name);
    }

    public GuiScene getGui() {
        return guiScene;
    }

    protected abstract UniformsMap createUniformMap(ShaderProgram program, String[] attribute);

    protected abstract ShaderProgram createShaderProgram(String vertShader, String fragShader);

}
