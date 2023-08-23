package com.game.lwjgl.Graphics;

import com.game.common.Camera.Camera;
import com.game.common.Graphics.Gui.GuiScene;
import com.game.common.Graphics.Gui.MiniMap;
import com.game.common.Renderer;
import com.game.common.Scene;
import com.game.common.ShaderProgram;
import com.game.common.Terrain.TerrainMap;
import com.game.common.UniformsMap;
import com.game.lwjgl.Window.EventListener.KeyListener;
import com.game.lwjgl.Window.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;


public class LwjglRenderer implements Renderer {
    private static LwjglRenderer renderer;

    private boolean wireFrame;

    private LwjglRenderer() {
    }

    public static LwjglRenderer getInstance() {
        if (renderer == null) {
            renderer = new LwjglRenderer();
        }
        return renderer;
    }

    @Override
    public void render(Scene scene) {
        Camera cam = scene.getCamera();
        cam.key(KeyListener.getInstance().getPressed());

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
        LwjglTextureList.getInstance().bind(map.getTextureName());
        terrainUniforms.setUniform("fValue", new float[]{
                TerrainMap.getTextureRow()
        });
        map.getMap().forEach(chunk -> {
            terrainUniforms.setUniform("modelMatrix", chunk.getModelMatrix());
            glBindVertexArray((Integer) chunk.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, chunk.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        glBindVertexArray(0);
        shaderP.unbind();

//        //Piece
//        shaderP = scene.getShaderProgram("piece");
//        shaderP.bind();
//        PieceManager.getPieceList().forEach(piece -> {
//            UniformsMap pieceUniforms = scene.getUniformMap("piece");
//            pieceUniforms.setMatrixUniform("projectionMatrix", cam.getProjectionMatrix());
//            pieceUniforms.setMatrixUniform("viewMatrix", cam.getViewMatrix());
//            pieceUniforms.setMatrixUniform("modelMatrix", piece.getModelMatrix());
//            pieceUniforms.setUniform("size", piece.getMesh().getSize());
//            glBindVertexArray(piece.getMesh().getVao());
//            glDrawElements(GL_TRIANGLES, piece.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
//            glBindVertexArray(0);
////            piece.rotatePiece(MatrixCalc.rotationMatrix(0.5f, (byte) 2));
////            piece.rotatePiece(MatrixCalc.rotationMatrix(0.3f, (byte) 1));
//        });
//        shaderP.unbind();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        //GUI
        GuiScene gui = scene.getGui();
        shaderP = gui.getShaderP("minimap");
        shaderP.bind();
        UniformsMap miniMapUniforms = gui.getUniformMap("minimap");
        miniMapUniforms.setUniform("projectionMatrix", cam.getOrthoProjection());
        miniMapUniforms.setUniform("viewPort", Window.getWidth(), Window.getHeight());
        miniMapUniforms.setUniform("tex", 1);
        glActiveTexture(GL_TEXTURE1);
        LwjglTextureList.getInstance().bind("minimap");
        MiniMap miniMap = gui.getMiniMap();
        glBindVertexArray((Integer) miniMap.getMesh().getVao());
        glDrawElements(GL_TRIANGLES, miniMap.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        shaderP.unbind();

        KeyListener.declicker(new int[]{GLFW_KEY_J, GLFW_KEY_K, GLFW_KEY_L});
    }

    public static void wireFrame() {
        getInstance().wireFrame = !getInstance().wireFrame;
    }

}
