import org.joml.Vector3f;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL3;

import Camera.Camera;
import Graphics.Chunk;
import Graphics.Mesh;
import Graphics.Renderer;
import template.Sketch;

//https://github.com/jvm-graphics-labs/modern-jogl-examples/tree/master
public class Main extends Sketch {

    private Renderer renderer;
    private Camera cam;
    private Chunk chunk;

    @Override
    public void init(GL3 gl) {
        cam = new Camera();
        cam.setCamera(new Vector3f(0.0f, 0.0f, -8.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 1.0f, 0.0f), 0.0f);
        cam.setPerspective(90.f, (float) window.getWidth() / window.getHeight(), 0.01f, 1000.0f);
        try {
            renderer = new Renderer(gl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float[] positions = new float[] {
                // VO
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };
        float[] colors = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        int[] indices = new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };
        Mesh mesh = new Mesh(positions, colors, indices, gl);
        chunk = new Chunk(mesh);

    }

    @Override
    public void display(GL3 gl) {
        gl.glClearBufferfv(GL2ES3.GL_COLOR, 0, clearColor.put(0, 0f).put(1, 0f).put(2, 0f).put(3, 1f));
        renderer.render(chunk, cam);
    }

    @Override
    public void reshape(GL3 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
        cam.setAspectRatio(w, h);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                cam.up();
                break;
            case KeyEvent.VK_DOWN:
                cam.down();
                break;
            case KeyEvent.VK_LEFT:
                cam.left();
                break;
            case KeyEvent.VK_RIGHT:
                cam.right();
                break;
            case KeyEvent.VK_W:
                cam.forward();
                break;
            case KeyEvent.VK_S:
                cam.backward();
                break;
            case KeyEvent.VK_A:
                cam.yawLeft();
                break;
            case KeyEvent.VK_D:
                cam.yawRight();
                break;
            case KeyEvent.VK_ESCAPE:
                quit();
        }
    }

    public static void main(String[] args) {
        new Main().setup("OpenGL");
    }
}
