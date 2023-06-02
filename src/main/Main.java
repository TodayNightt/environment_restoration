import java.io.IOException;

import org.joml.Vector3f;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import Camera.Camera;
import Graphics.Renderer;
import Graphics.Textures;
import Terrain.TerrainMap;
import Terrain.Generation.TextureGenerator;
import template.Sketch;

//https://github.com/jvm-graphics-labs/modern-jogl-examples/tree/master
public class Main extends Sketch {

    private Renderer renderer;
    private Camera cam;
    private TerrainMap terrainMap;
    private Textures textureList;
    private boolean wireFrame;
    protected int[] checkKey = new int[] { KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_UP,
            KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ESCAPE };
    protected boolean[] pressed = new boolean[checkKey.length];

    @Override
    public void init(GL3 gl) {
        cam = new Camera();
        cam.setCamera(new Vector3f(0.0f, 0.0f, -2.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 1.0f, 0.0f), 0.0f);
        cam.setPerspective(90.f, (float) window.getWidth() / window.getHeight(), 0.1f, 20.0f);
        textureList = new Textures(gl);
        try {
            renderer = new Renderer(gl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wireFrame = false;
        initializeTexture();
        terrainMap = new TerrainMap(gl);
        System.out.println(gl.glGetString(GL.GL_VERSION));
    }

    private void initializeTexture() {
        TextureGenerator.GenerateDirtMap();
        TextureGenerator.GenerateGrassMap();
        try {
            textureList.createTexture("dirt", "resources/textures/dirt_texture.png", true);
            textureList.createTexture("grass", "resources/textures/grass_texture.png", true);
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void display(GL3 gl) {
        gl.glClearBufferfv(GL2ES3.GL_COLOR, 0, clearColor.put(0, 0f).put(1, 0f).put(2, 0f).put(3, 1f));
        renderer.render(terrainMap, cam, textureList, wireFrame);
        cam.key(pressed);
        // int MB = 1024 * 1024;
        // Runtime runtime = Runtime.getRuntime();

        // // Print used memory
        // System.out.println("Used Memory:"
        // + (runtime.totalMemory() - runtime.freeMemory()) / MB);

        // // Print free memory
        // System.out.println("Free Memory:"
        // + runtime.freeMemory() / MB);

        // // Print total available memory
        // System.out.println("Total Memory:" + runtime.totalMemory() / MB);

        // // Print Maximum available memory
        // System.out.println("Max Memory:" + runtime.maxMemory() / MB);
    }

    @Override
    public void reshape(GL3 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        cam.setAspectRatio(w, h);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_N) {
            wireFrame = !wireFrame;
        }
        changeState(true, keyEvent.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.isAutoRepeat()) {
            return;
        }
        changeState(false, keyEvent.getKeyCode());
    }

    public void changeState(boolean state, int keyCode) {
        if (keyCode == checkKey[checkKey.length - 1])
            quit();
        for (int i = 0; i < checkKey.length - 1; i++) {
            if (keyCode == checkKey[i]) {
                pressed[i] = state;
            }
        }

    }

    public static void main(String[] args) {

        new Main().setup("OpenGL");
    }
}
