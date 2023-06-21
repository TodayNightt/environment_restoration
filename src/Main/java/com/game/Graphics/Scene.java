package com.game.Graphics;

import com.game.Camera.Camera;
import com.game.Graphics.Gui.ButtonManager;
import com.game.Terrain.Generation.TextureGenerator;
import com.game.Terrain.TerrainMap;
import com.game.Window.Window;
import org.joml.Vector3f;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL33.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL33.GL_VERTEX_SHADER;

public class Scene {
    private TextureList textureList;
    private TerrainMap terrain;
    private HashMap<String, ShaderProgram> shaderProgramList;
    private HashMap<String, UniformsMap> uniformsMapList;
    private Camera cam;
    private Window window;
    private ButtonManager buttonManager;

    public Scene(Window window) throws Exception {
        this.window = window;
        init();
    }

    private void init() throws Exception {
        this.textureList = new TextureList();
        this.shaderProgramList = new HashMap<>();
        this.uniformsMapList = new HashMap<>();
        initCam();
        initGui();
        initializeTexture();
        initializePiece();
        initializeTerrainGen();
    }

    private void initCam() {
        cam = new Camera();
        cam.setCamera(new Vector3f(0.0f, 0.0f, -5.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 1.0f, 0.0f), 0.0f);
        cam.setPerspective(60.f, Window.getWidth() / Window.getHeight(), 0.1f, 1000.0f);
    }

    private void initializeTexture() throws URISyntaxException, IOException {
        TextureGenerator.GenerateAtlas();
        textureList.createTexture("block_atlas", "textures/block_atlas_texture.png");
    }

    private void initGui() throws Exception {

        //Initialize shaderProgram
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile("shaders/button.vert", GL_VERTEX_SHADER));
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile("shaders/button.frag", GL_FRAGMENT_SHADER));
        ShaderProgram shaderProgram = new ShaderProgram(shaderDataList);

        //Initialize uniformMap
        UniformsMap uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("resizeFactor");
        uniformsMap.createUniform("currentColor");


        buttonManager = new ButtonManager();
        buttonManager.addButton(0.03f, 0.05f, 0.05f);
        buttonManager.addButton(0.03f, 0.11f, 0.05f);
        shaderProgramList.put("button", shaderProgram);
        uniformsMapList.put("button", uniformsMap);
    }


    public TextureList getTextureList() {
        return textureList;
    }

    public TerrainMap getTerrain() {
        return terrain;
    }

    private void initializeTerrainGen() throws Exception {

        //Initialize shaderProgram
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile("shaders/terrain.vert", GL_VERTEX_SHADER));
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile("shaders/terrain.frag", GL_FRAGMENT_SHADER));
        ShaderProgram shaderProgram = new ShaderProgram(shaderDataList);

        //Initialize uniformMap
        UniformsMap uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("tex");
        uniformsMap.createUniform("textureRow");


        this.shaderProgramList.put("terrain", shaderProgram);
        this.uniformsMapList.put("terrain", uniformsMap);
        this.terrain = new TerrainMap("block_atlas");
    }

    private void initializePiece() throws Exception {

        //Initialize shaderProgram
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile("shaders/piece.vert", GL_VERTEX_SHADER));
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile("shaders/piece.frag", GL_FRAGMENT_SHADER));
        ShaderProgram shaderProgram = new ShaderProgram(shaderDataList);

        //Initialize uniformMap
        UniformsMap uniformsMap = new UniformsMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("size");

        this.shaderProgramList.put("piece", shaderProgram);
        this.uniformsMapList.put("piece", uniformsMap);
    }

    public Camera getCamera() {
        return cam;
    }


    public ShaderProgram getShaderProgram(String name) {
        return shaderProgramList.get(name);
    }

    public ButtonManager getButtonManager() {
        return buttonManager;
    }

    public void cleanup() {
        shaderProgramList.values().forEach(ShaderProgram::cleanup);
    }

    public UniformsMap getUniformMap(String name) {
        return uniformsMapList.get(name);
    }
}
