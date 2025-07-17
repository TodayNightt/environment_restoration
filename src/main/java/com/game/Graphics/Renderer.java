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
import static com.game.Window.EventListener.KeyListener.Key;

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
        if (pressed[Key.getIndex(Key.W)])
            cam.forward();
        if (pressed[Key.getIndex(Key.A)])
            cam.yawLeft();
        if (pressed[Key.getIndex(Key.S)])
            cam.backward();
        if (pressed[Key.getIndex(Key.D)])
            cam.yawRight();
        if (pressed[Key.getIndex(Key.Up)])
            cam.up();
        if (pressed[Key.getIndex(Key.Down)])
            cam.down();
        if (pressed[Key.getIndex(Key.Left)])
            cam.tiltUp();
        if (pressed[Key.getIndex(Key.Right)])
            cam.tiltDown();
        if (pressed[Key.getIndex(Key.J)])
            pieceManager.addPiece(PieceCollection.getPieceType().get(0), (position.x() + 3 * lookDir.x()), position.y(),
                    (position.z() + 3 * lookDir.z()));
        if (pressed[Key.getIndex(Key.K)])
            pieceManager.addPiece(PieceCollection.getPieceType().get(1), (position.x() + 3 * lookDir.x()), position.y(),
                    (position.z() + 3 * lookDir.z()));
        if (pressed[Key.getIndex(Key.L)])
            pieceManager.addPiece(PieceCollection.getPieceType().get(2), (position.x() + 3 * lookDir.x()), position.y(),
                    (position.z() + 3 * lookDir.z()));
        if (pressed[Key.getIndex(Key.C)]) {
            int[] heightMap = createHeightMap();
            terrainMap.refresh(heightMap);
            scene.refreshMapTexture(heightMap);
        }
        if (pressed[Key.getIndex(Key.N)]) {
            wireFrame();
        }
    }

}
