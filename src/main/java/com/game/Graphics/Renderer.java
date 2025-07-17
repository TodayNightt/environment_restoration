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

        KeyListener.declicker(new int[] { GLFW_KEY_J, GLFW_KEY_K, GLFW_KEY_L, GLFW_KEY_C, GLFW_KEY_N });
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
            pieceManager.addPiece(PieceCollection.getPieceType().get(0), (position.x() + 3 * lookDir.x()), position.y(),
                    (position.z() + 3 * lookDir.z()));
        if (pressed[9])
            pieceManager.addPiece(PieceCollection.getPieceType().get(1), (position.x() + 3 * lookDir.x()), position.y(),
                    (position.z() + 3 * lookDir.z()));
        if (pressed[10])
            pieceManager.addPiece(PieceCollection.getPieceType().get(2), (position.x() + 3 * lookDir.x()), position.y(),
                    (position.z() + 3 * lookDir.z()));
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
