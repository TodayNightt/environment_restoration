package com.game.Graphics;

import com.game.Camera.Camera;
import com.game.GameLogic.PieceManager;
import com.game.Terrain.TerrainMap;
import com.game.Utils.WorkerManager;
import com.game.Window.EventListener.KeyListener;
import com.game.templates.SceneItem;
import org.joml.Vector3f;

import java.util.HashMap;

import static com.game.Terrain.Generation.NoiseMap.createHeightMap;
import static com.game.Utils.WorkerManager.stillWorking;
import static org.lwjgl.glfw.GLFW.*;


public class Renderer {

    private Scene scene;

    private boolean wireFrame;

    public Renderer() {
    }

    public void add(Scene scene) {
        this.scene = scene;
    }

    public void render() {
        Camera cam = scene.getCamera();
        handleInput(KeyListener.getInstance().getPressed());
        HashMap<String, SceneItem> sceneItems = scene.getSceneItems();
        if (stillWorking) {
            WorkerManager.getInstance().run();
        }
        sceneItems.forEach((key, value) -> value.render(cam, wireFrame));


//        glPolygonMode(GL_FRONT_AND_BACK, wireFrame ? GL_LINE : GL_FILL);

        //Terrain
//        ShaderProgram shaderP = scene.getShaderProgram("terrain");
//        shaderProgram.bind();
//        UniformsMap terrainUniforms = scene.getUniformMap("terrain");
//        terrainUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
//        terrainUniforms.setUniform("viewMatrix", cam.getViewMatrix());
//        terrainUniforms.setUniform("tex", 0);
//        TerrainMap map = scene.getTerrain();
//        glActiveTexture(GL_TEXTURE0);
//        TextureList.getInstance().bind(map.getTextureName());
//        terrainUniforms.setUniform("fValue", new float[]{
//                TerrainMap.getTextureRow()
//        });
//        map.getMap().stream().filter(chunk -> chunk.getMesh() != null).forEach(chunk -> {
//            terrainUniforms.setUniform("modelMatrix", chunk.getModelMatrix());
//            glBindVertexArray( chunk.getMesh().getVao());
//            glDrawElements(GL_TRIANGLES, chunk.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
//        });
//        glBindVertexArray(0);
//        shaderProgram.unbind();

//        //Piece
//        ShaderProgram shaderP = scene.getShaderProgram("piece");
//        shaderP.bind();
//        UniformsMap pieceUniforms = scene.getUniformMap("piece");
//        pieceUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
//        pieceUniforms.setUniform("viewMatrix", cam.getViewMatrix());
//        PieceManager.getPieceList().forEach(piece -> {
//            pieceUniforms.setUniform("modelMatrix", piece.getModelMatrix());
//            pieceUniforms.setUniform("size", piece.getMesh().getSize());
//            glBindVertexArray(piece.getMesh().getVao());
//            glDrawElements(GL_TRIANGLES,piece.getMesh().getNumVertices(),GL_UNSIGNED_INT,0);
//            piece.rotatePiece(MatrixCalc.rotationMatrix(0.5f, (byte) 2));
//            piece.rotatePiece(MatrixCalc.rotationMatrix(0.3f, (byte) 1));
//        });
//        glBindVertexArray(0);
//        shaderP.unbind();
//
//        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

//        //GUI
//        GuiManager gui = scene.guiManager();
//        if(gui.getMiniMap().getMesh() != null) {
//           ShaderProgram shaderP = gui.getShaderP("minimap");
//            shaderP.bind();
//            UniformsMap miniMapUniforms = gui.getUniformMap("minimap");
//            miniMapUniforms.setUniform("projectionMatrix", cam.getOrthoProjection());
//            miniMapUniforms.setUniform("viewPort", Window.getWidth(), Window.getHeight());
//            miniMapUniforms.setUniform("tex", 1);
//            glActiveTexture(GL_TEXTURE1);
//            TextureList.getInstance().bind("minimap");
//            Minimap quad = gui.getMiniMap();
//            glBindVertexArray(quad.getMesh().getVao());
//            glDrawElements(GL_TRIANGLES, quad.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
//            shaderP.unbind();
//        }

        KeyListener.declicker(new int[]{GLFW_KEY_J, GLFW_KEY_K, GLFW_KEY_L, GLFW_KEY_C, GLFW_KEY_N});
    }

    public void wireFrame() {
        wireFrame = !wireFrame;
    }

    public void handleInput(boolean[] pressed) {
        PieceManager pieceManager = (PieceManager) scene.getSceneItems().get("piece");
        TerrainMap terrainMap = (TerrainMap) scene.getSceneItems().get("terrain");
        Camera cam = scene.getCamera();
        Vector3f position = cam.getPosition();
        Vector3f lookDir = cam.getLookDir();
        if (pressed[0])
            cam.forward();
        if (pressed[1])
            cam.yawLeft();
        if (pressed[2])
            cam.backward();
        if (pressed[3])
            cam.yawRight();
        if (pressed[4])
            cam.up();
        if (pressed[5])
            cam.down();
        if (pressed[6])
            cam.panUp();
        if (pressed[7])
            cam.panDown();
        if (pressed[8])
            pieceManager.addPiece(PieceCollection.getPieceType().get(0), (position.x() + 3 * lookDir.x()), position.y(), (position.z() + 3 * lookDir.z()));
        if (pressed[9])
            pieceManager.addPiece(PieceCollection.getPieceType().get(1), (position.x() + 3 * lookDir.x()), position.y(), (position.z() + 3 * lookDir.z()));
        if (pressed[10])
            pieceManager.addPiece(PieceCollection.getPieceType().get(2), (position.x() + 3 * lookDir.x()), position.y(), (position.z() + 3 * lookDir.z()));
        if (pressed[11]) {
            int[] heightMap = createHeightMap();
            terrainMap.refresh(heightMap);
            scene.refreshMapTexture(heightMap);
        }
        if (pressed[12]) {
            wireFrame();
        }
    }

}
