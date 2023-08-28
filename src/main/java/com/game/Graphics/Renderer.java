package com.game.Graphics;

import com.game.Camera.Camera;
import com.game.GameLogic.PieceManager;
import com.game.Graphics.Gui.GuiScene;
import com.game.Graphics.Gui.MiniMap;
import com.game.Terrain.TerrainMap;
import com.game.Utils.MatrixCalc;
import com.game.Window.EventListener.KeyListener;
import com.game.Window.Window;
import com.game.Utils.WorkerManager;

import static com.game.Utils.WorkerManager.stillWorking;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;


public class Renderer {
    private static Renderer instance;

    private boolean wireFrame;

    private Renderer() {
    }

    public static Renderer getInstance(){
        if(instance ==null){
            instance = new Renderer();
        }
        return instance;
    }


    public void render(Scene scene) {
        Camera cam = scene.getCamera();
        cam.key(KeyListener.getInstance().getPressed());
        if(KeyListener.getInstance().getPressed()[11]){
            scene.getTerrain().init();
        }
        if(stillWorking){
            WorkerManager.getInstance().run();
        }



        glPolygonMode(GL_FRONT_AND_BACK, wireFrame ? GL_LINE : GL_FILL);

        //Terrain
        ShaderProgram shaderP = scene.getShaderProgram("terrain");
        shaderP.bind();
        UniformsMap terrainUniforms = scene.getUniformMap("terrain");
        terrainUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
        terrainUniforms.setUniform("viewMatrix", cam.getViewMatrix());
        terrainUniforms.setUniform("tex", 0);
        TerrainMap map = scene.getTerrain();
        glActiveTexture(GL_TEXTURE0);
        TextureList.getInstance().bind(map.getTextureName());
        terrainUniforms.setUniform("fValue", new float[]{
                TerrainMap.getTextureRow()
        });
        map.getMap().stream().filter(chunk -> chunk.getMesh() != null).forEach(chunk -> {
            terrainUniforms.setUniform("modelMatrix", chunk.getModelMatrix());
            glBindVertexArray( chunk.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, chunk.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        glBindVertexArray(0);
        shaderP.unbind();

        //Piece
        shaderP = scene.getShaderProgram("piece");
        shaderP.bind();
        UniformsMap pieceUniforms = scene.getUniformMap("piece");
        pieceUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
        pieceUniforms.setUniform("viewMatrix", cam.getViewMatrix());
        PieceManager.getPieceList().forEach(piece -> {
            pieceUniforms.setUniform("modelMatrix", piece.getModelMatrix());
            pieceUniforms.setUniform("size", piece.getMesh().getSize());
            glBindVertexArray(piece.getMesh().getVao());
            glDrawElements(GL_TRIANGLES,piece.getMesh().getNumVertices(),GL_UNSIGNED_INT,0);
            piece.rotatePiece(MatrixCalc.rotationMatrix(0.5f, (byte) 2));
            piece.rotatePiece(MatrixCalc.rotationMatrix(0.3f, (byte) 1));
        });
        glBindVertexArray(0);
        shaderP.unbind();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        //GUI
        GuiScene gui = scene.getGui();
        if(gui.getMiniMap().getMesh() != null) {
            shaderP = gui.getShaderP("minimap");
            shaderP.bind();
            UniformsMap miniMapUniforms = gui.getUniformMap("minimap");
            miniMapUniforms.setUniform("projectionMatrix", cam.getOrthoProjection());
            miniMapUniforms.setUniform("viewPort", Window.getWidth(), Window.getHeight());
            miniMapUniforms.setUniform("tex", 1);
            glActiveTexture(GL_TEXTURE1);
            TextureList.getInstance().bind("minimap");
            MiniMap miniMap = gui.getMiniMap();
            glBindVertexArray(miniMap.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, miniMap.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
            shaderP.unbind();
        }

        KeyListener.declicker(new int[]{GLFW_KEY_J, GLFW_KEY_K, GLFW_KEY_L,GLFW_KEY_C});
    }

    public static void wireFrame() {
        getInstance().wireFrame = !getInstance().wireFrame;
    }

}
