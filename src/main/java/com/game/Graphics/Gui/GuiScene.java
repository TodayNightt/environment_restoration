package com.game.Graphics.Gui;


import com.game.Graphics.ShaderProgram;
import com.game.Graphics.UniformsMap;

import java.util.HashMap;
import java.util.Map;

public class GuiScene {
    private final Map<String, ShaderProgram> shaderPrograms;
    private final Map<String, UniformsMap> uniformsMaps;
    private MiniMap miniMap;

    public GuiScene() {
        this.shaderPrograms = new HashMap<>();
        this.uniformsMaps = new HashMap<>();
        initComponents();
    }

    private void initComponents() {
        this.miniMap = new MiniMap(16, 1, 3);
    }

    public void init(String type, ShaderProgram shaderProgram, UniformsMap uniformsMap) {
        shaderPrograms.put(type, shaderProgram);
        uniformsMaps.put(type, uniformsMap);
    }

    public MiniMap getMiniMap() {
        return miniMap;
    }

    public UniformsMap getUniformMap(String type) {
        return uniformsMaps.get(type);
    }

    public ShaderProgram getShaderP(String type) {
        return shaderPrograms.get(type);
    }
}
