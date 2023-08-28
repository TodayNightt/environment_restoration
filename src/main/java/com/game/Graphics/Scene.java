package com.game.Graphics;

import com.game.Camera.Camera;
import com.game.GameLogic.PieceManager;
import com.game.Graphics.Gui.GuiScene;
import com.game.Terrain.TerrainMap;
import com.game.Terrain.Generation.TextureGenerator;
import com.game.Utils.FileUtils;
import com.game.Window.Window;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.game.Utils.FileUtils.loadShaderFromResources;
import static org.lwjgl.opengl.GL33.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL33.GL_VERTEX_SHADER;

public class Scene{

    protected TerrainMap terrain;
    protected HashMap<String, ShaderProgram> shaderProgramList;
    protected HashMap<String, UniformsMap> uniformsMapList;
    protected Camera cam;
    protected Window window;
    protected GuiScene guiScene;

    public Scene(Window window) {
        this.window = window;
        init();
    }

    public void init() {
        this.shaderProgramList = new HashMap<>();
        this.uniformsMapList = new HashMap<>();
        initCam();
        initGui();
        initializeTexture();
        initializePiece();
        initializeTerrainGen();
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

        guiScene = new GuiScene();

        ShaderProgram shaderP = createShaderProgram("shaders/minimap.vert", "shaders/minimap.frag");
        String[] attributes = {"projectionMatrix", "viewPort", "tex"};
        UniformsMap uniformsMap = createUniformMap(shaderP,attributes);
        guiScene.init("minimap",shaderP,uniformsMap);
    }


    protected void initializeTerrainGen() {
        //Initialize shaderProgram
        ShaderProgram shaderP = createShaderProgram("shaders/terrain.vert", "shaders/terrain.frag");
        //Initialize uniformMap
        String[] attributes = {"projectionMatrix", "viewMatrix", "modelMatrix", "tex", "fValue"};
        UniformsMap uniformsMap = createUniformMap(shaderP,attributes);

        this.shaderProgramList.put("terrain",shaderP);
        this.uniformsMapList.put("terrain",uniformsMap);
        this.terrain = new TerrainMap("block_atlas", this);
    }


    protected void initializePiece() {
        //Initialize shaderProgram
        ShaderProgram shaderP = createShaderProgram("shaders/piece.vert", "shaders/piece.frag");
        //Initialize uniformMap
        String[] attributes = {"projectionMatrix", "viewMatrix", "modelMatrix", "size"};
        UniformsMap uniformsMap = createUniformMap(shaderP,attributes);
        this.shaderProgramList.put("piece",shaderP);
        this.uniformsMapList.put("piece",uniformsMap);
        PieceCollection.init();
    }


    public void cleanup() {
        shaderProgramList.values().forEach(ShaderProgram::cleanup);
        TextureList.getInstance().cleanup();
        terrain.cleanup();
    }


    public void createTexture(String name, ByteBuffer buffer) {
        TextureList.getInstance().createTexture(name, buffer);
    }

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

    protected ShaderProgram createShaderProgram(String vertShader, String fragShader) {
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(vertShader, GL_VERTEX_SHADER));
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(fragShader, GL_FRAGMENT_SHADER));
        return new ShaderProgram(shaderDataList);
    }

    protected UniformsMap createUniformMap(ShaderProgram program, String[] attributes) {
        return new UniformsMap(program.getProgramId(),attributes);
    }

}
