package com.game.Graphics.Gui;

import static com.game.Graphics.Scene.createShaderProgram;
import static com.game.Graphics.Scene.createUniformMap;
import static org.lwjgl.opengl.GL33.*;
import com.game.Camera.Camera;
import com.game.Graphics.ShaderProgram;
import com.game.Graphics.TextureList;
import com.game.Graphics.UniformsMap;
import com.game.Window.Window;
import com.game.templates.GuiItem;
import com.game.templates.SceneItem;

import java.util.HashMap;
import java.util.Map;

public class GuiManager extends SceneItem {

    private final Map<String, ShaderProgram> shaderPrograms;
    private final Map<String, UniformsMap> uniformsMaps;

    private final Map<String,GuiItem> guiItems;

    public GuiManager() {
        this.shaderPrograms = new HashMap<>();
        this.uniformsMaps = new HashMap<>();
        this.guiItems = new HashMap<>();
    }

    public void create(String type, ShaderProgram shaderProgram, UniformsMap uniformsMap) {
        shaderPrograms.put(type, shaderProgram);
        uniformsMaps.put(type, uniformsMap);
    }

    public void addItem(String id,GuiItem item){
        guiItems.put(id,item);
    }

    public Minimap getMiniMap() {
        return (Minimap) guiItems.get("minimap");
    }


    public UniformsMap getUniformMap(String type) {
        return uniformsMaps.get(type);
    }

    public ShaderProgram getShaderP(String type) {
        return shaderPrograms.get(type);
    }

    @Override
    public void render(Camera cam,boolean isWireFrame) {
            ShaderProgram shaderP = shaderPrograms.get("minimap");
            shaderP.bind();
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            UniformsMap miniMapUniforms = uniformsMaps.get("minimap");
            miniMapUniforms.setUniform("projectionMatrix", cam.getOrthoProjection());
            miniMapUniforms.setUniform("viewPort", Window.getWidth(), Window.getHeight());
            miniMapUniforms.setUniform("tex", 1);
            glActiveTexture(GL_TEXTURE1);
            TextureList.getInstance().bind("minimap");
            Minimap quad = (Minimap) guiItems.get("minimap");
            glBindVertexArray(quad.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, quad.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
            shaderP.unbind();

    }

    @Override
    public void init(String id,String vertShader, String fragShader, String[] uniformList) {
        ShaderProgram shaderProgram = createShaderProgram(vertShader,fragShader);
        UniformsMap uniformsMap = createUniformMap(shaderProgram,uniformList);
        this.shaderPrograms.put(id,shaderProgram);
        this.uniformsMaps.put(id,uniformsMap);
    }

    @Override
    public void cleanup() {

    }


}
