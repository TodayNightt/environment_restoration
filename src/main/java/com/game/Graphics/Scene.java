package com.game.Graphics;

import com.game.Camera.Camera;
import com.game.GameLogic.PieceManager;
import com.game.Graphics.Gui.GuiManager;
import com.game.Graphics.Gui.Minimap;
import com.game.Terrain.Generation.NoiseMap;
import com.game.Terrain.Generation.TextureGenerator;
import com.game.Terrain.TerrainMap;
import com.game.Window.Window;
import com.game.templates.SceneItem;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.*;

import static com.game.Terrain.Generation.NoiseMap.createHeightMap;
import static com.game.Utils.TerrainContraints.*;
import static org.lwjgl.opengl.GL33.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL33.GL_VERTEX_SHADER;

public class Scene{

    public enum SceneType{
        ITEM,
        GUI
    }

    protected HashMap<String,SceneItem>sceneItems;
    protected Camera cam;
    protected GuiManager guiScene;

    public Scene() {
        init();
    }

    public void init() {
        this.sceneItems = new HashMap<>();
        initCam();
        initGui();
        initializeTexture();
        initializePiece();
    }

    protected void initCam() {
        cam = new Camera();
        cam.setCamera(new Vector3f(-150.0f, -40.0f, -150.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 1.0f, 0.0f), 0.0f);
        cam.setPerspective(60.f, Window.getWidth() / Window.getHeight(), 0.1f, 1000.0f);
    }

    protected void initializeTexture() {
        createTexture("block_atlas", TextureGenerator.GenerateAtlas());
    }

    protected void initGui() {
       guiScene = new GuiManager();
        guiScene.init("minimap","shaders/minimap.vert","shaders/minimap.frag",new String[]{"projectionMatrix", "viewPort","tex"});
        guiScene.addItem("minimap",Minimap.create(16, 1, 3));
        sceneItems.put("gui",guiScene);
    }

    public void initializeTerrain(String vertShader,String fragShader,String[]uniformList) {
        TerrainMap terrainMap = new TerrainMap("block_atlas");
        terrainMap.init("terrain",vertShader,fragShader,uniformList);
        int[] heightMap = createHeightMap();
        terrainMap.refresh(heightMap);
        refreshMapTexture(heightMap);
        this.sceneItems.put("terrain",terrainMap);
    }

    protected void initializePiece() {
        PieceManager pieceManager = new PieceManager();
        pieceManager.init("piece","shaders/piece.vert", "shaders/piece.frag",new String[]{"projectionMatrix", "viewMatrix", "modelMatrix", "size"});
        this.sceneItems.put("piece",pieceManager);
        PieceCollection.init();
    }

    public void cleanup() {
        TextureList.getInstance().cleanup();
        sceneItems.forEach((key,value)->value.cleanup());
    }

    public void refreshMapTexture(int [] heightMap){
        createTexture("minimap",TextureGenerator.createColoredMap(heightMap, MAP_SIZE * CHUNK_SIZE));
    }

    public void createTexture(String name, ByteBuffer buffer) {
        TextureList.getInstance().createTexture(name, buffer);
    }

    public Camera getCamera() {
        return cam;
    }

    public HashMap<String, SceneItem> getSceneItems() {
        return sceneItems;
    }

    public static ShaderProgram createShaderProgram(String vertShader, String fragShader) {
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(vertShader, GL_VERTEX_SHADER));
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(fragShader, GL_FRAGMENT_SHADER));
        return new ShaderProgram(shaderDataList);
    }

    public static UniformsMap createUniformMap(ShaderProgram program, String[] attributes) {
        return new UniformsMap(program.getProgramId(),attributes);
    }

    public GuiManager guiManager(){
        return guiScene;
    }
}
