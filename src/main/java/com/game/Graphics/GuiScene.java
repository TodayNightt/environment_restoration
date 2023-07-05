package com.game.Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class GuiScene {
    private Map<String,ShaderProgram> shaderPrograms;
    private Map<String,UniformsMap> uniformsMaps;
    private MiniMap miniMap;

    public GuiScene(){
        this.shaderPrograms = new HashMap<>();
        this.uniformsMaps = new HashMap<>();
        initComponents();
    }

    private void initComponents(){
        this.miniMap = new MiniMap(16,1,3);
    }

    public void init(String type,String vertShader, String fragShader){
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(vertShader,GL_VERTEX_SHADER));
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(fragShader,GL_FRAGMENT_SHADER));
        ShaderProgram shaderProgram = new ShaderProgram(shaderDataList);

        UniformsMap uniformsMap = new UniformsMap(shaderProgram.getProgramId());

        shaderPrograms.put(type,shaderProgram);
        uniformsMaps.put(type,uniformsMap);
    }

    public MiniMap getMiniMap(){
        return miniMap;
    }


    public UniformsMap getUniformMap(String type){
        return uniformsMaps.get(type);
    }

    public ShaderProgram getShaderP(String type){
        return shaderPrograms.get(type);
    }
}
