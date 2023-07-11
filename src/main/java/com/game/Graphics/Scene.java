package com.game.Graphics;

import com.game.Camera.Camera;
import com.game.GameLogic.PieceCollection;
import com.game.Graphics.Gui.GuiScene;
import com.game.Terrain.Generation.TextureGenerator;
import com.game.Terrain.TerrainMap;
import com.game.Window.Window;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
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
    private final Window window;
    private GuiScene guiScene;

    public Scene(Window window) {
        this.window = window;
        init();
    }

    private void init(){
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
        cam.setCamera(new Vector3f(-150.0f, -40.0f, -150.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 1.0f, 0.0f), 0.0f);
        cam.setPerspective(60.f, Window.getWidth() / Window.getHeight(), 0.1f, 1000.0f);
    }

    private void initializeTexture() {
        textureList.createTexture("block_atlas", TextureGenerator.GenerateAtlas());
    }

    private void initGui() {
        guiScene = new GuiScene();

        guiScene.init("minimap","shaders/minimap.vert","shaders/minimap.frag");
        UniformsMap uniformsMap = guiScene.getUniformMap("minimap");
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewPort");
        uniformsMap.createUniform("tex");
    }

    private void initializeTerrainGen() {
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
        uniformsMap.createUniform("fValue");


        this.shaderProgramList.put("terrain", shaderProgram);
        this.uniformsMapList.put("terrain", uniformsMap);
        this.terrain = new TerrainMap(this,"block_atlas");
    }

    private void initializePiece() {
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
        PieceCollection.getInstance();
    }
    public void cleanup() {
        shaderProgramList.values().forEach(ShaderProgram::cleanup);
        textureList.cleanup();
        terrain.cleanup();
    }

    public void render(){
        Renderer.getInstance().render(this);
    }
    public void addMapTexture(ByteBuffer buffer){
        textureList.createTexture("minimap",buffer);
    }

    //Getter
    public Camera getCamera() {
        return cam;
    }
    public TextureList getTextureList() {
        return textureList;
    }
    public TerrainMap getTerrain() {
        return terrain;
    }
    public ShaderProgram getShaderProgram(String name) {
        return shaderProgramList.get(name);
    }
    public UniformsMap getUniformMap(String name) {
        return uniformsMapList.get(name);
    }
    public GuiScene getGui(){
        return guiScene;
    }
}
