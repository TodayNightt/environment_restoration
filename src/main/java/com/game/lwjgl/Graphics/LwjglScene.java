package com.game.lwjgl.Graphics;

import com.game.common.Camera.Camera;
import com.game.common.Graphics.Gui.GuiScene;
import com.game.common.Scene;
import com.game.common.ShaderProgram;
import com.game.common.Terrain.Generation.TextureGenerator;
import com.game.common.UniformsMap;
import com.game.common.Utils.Vec3f;
import com.game.lwjgl.Window.Window;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL33.GL_VERTEX_SHADER;

public class LwjglScene extends Scene {

    public LwjglScene(Window window) {
        this.window = window;
        isWebGL = false;
        init();
    }

    @Override
    protected void initCam() {
        cam = new Camera();
        cam.setCamera(new Vec3f(-150.0f, -40.0f, -150.0f), new Vec3f(0.0f, 0.0f, -1.0f),
                new Vec3f(0.0f, 1.0f, 0.0f), 0.0f);
        cam.setPerspective(60.f, Window.getWidth() / Window.getHeight(), 0.1f, 1000.0f);
    }


    @Override
    protected void initializeTexture() {
        createTexture("block_atlas", TextureGenerator.GenerateAtlas());
    }

    @Override
    protected void initGui() {

        guiScene = new GuiScene();

        String[] attributes = {"projectionMatrix", "viewPort", "tex"};
        ShaderProgram shaderProgram = createShaderProgram("shaders/minimap.vert", "shaders/minimap.frag");
        UniformsMap uniformsMap = createUniformMap(shaderProgram, attributes);
        guiScene.init("minimap", shaderProgram, uniformsMap);
    }

    @Override
    protected void initializeTerrainGen() {
        //Initialize shaderProgram
        ShaderProgram shaderProgram = createShaderProgram("shaders/terrain.vert", "shaders/terrain.frag");

        //Initialize uniformMap
        String[] attributes = {"projectionMatrix", "viewMatrix", "modelMatrix", "tex", "fValue"};
        UniformsMap uniformsMap = createUniformMap(shaderProgram, attributes);

        this.shaderProgramList.put("terrain", shaderProgram);
        this.uniformsMapList.put("terrain", uniformsMap);
        this.terrain = new LwjglTerrainMap("block_atlas", this);
    }

    @Override
    protected void initializePiece() {
        //Initialize shaderProgram
        ShaderProgram shaderProgram = createShaderProgram("shaders/piece.vert", "shaders/piece.frag");

        //Initialize uniformMap
        String[] attributes = {"projectionMatrix", "viewMatrix", "modelMatrix", "size"};
        UniformsMap uniformsMap = createUniformMap(shaderProgram, attributes);

        this.shaderProgramList.put("assets/piece", shaderProgram);
        this.uniformsMapList.put("assets/piece", uniformsMap);
        LwjglPieceCollection.getInstance();
    }

    @Override
    public void cleanup() {
        shaderProgramList.values().forEach(ShaderProgram::cleanup);
        LwjglTextureList.getInstance().cleanup();
        terrain.cleanup();
    }


    @Override
    public void render() {
        LwjglRenderer.getInstance().render(this);
    }


    @Override
    protected ShaderProgram createShaderProgram(String vertShader, String fragShader) {
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(vertShader, GL_VERTEX_SHADER));
        shaderDataList.add(ShaderProgram.ShaderData.createShaderByFile(fragShader, GL_FRAGMENT_SHADER));
        return new LwjglShaderProgram(shaderDataList);
    }

    @Override
    protected UniformsMap createUniformMap(ShaderProgram program, String[] attributes) {
        UniformsMap uniformsMap = new LwjglUniformsMap((int) program.getProgramId());
        for (String attribute : attributes) uniformsMap.createUniform(attribute);
        return uniformsMap;
    }

    @Override
    public void createTexture(String name, ByteBuffer buffer) {
        LwjglTextureList.getInstance().createTexture(name, buffer);
    }

}
