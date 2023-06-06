import Camera.Camera;
import Graphics.Renderer;
import Graphics.Scene;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL3;
import org.joml.Vector3f;
import template.Sketch;

//https://github.com/jvm-graphics-labs/modern-jogl-examples/tree/master
public class Main extends Sketch {

    private Renderer renderer;
    private Camera cam;
    private Scene scene;
    private boolean wireFrame;

    protected int[] checkKey = new int[] { KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_UP,
            KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ESCAPE };
    protected boolean[] pressed = new boolean[checkKey.length];

    @Override
    public void init(GL3 gl) throws Exception {
        cam = new Camera();
        cam.setCamera(new Vector3f(-10.0f, -15.0f, -2.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 1.0f, 0.0f), 90.0f);
        cam.setPerspective(60.f, (float) window.getWidth() / window.getHeight(), 0.1f, 100.0f);
        scene = new Scene(gl);
        try {
            renderer = new Renderer(gl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wireFrame = false;
        System.out.println(gl.glGetString(GL.GL_VERSION));
    }



    @Override
    public void display(GL3 gl) {
        gl.glClearBufferfv(GL2ES3.GL_COLOR, 0, clearColor.put(0, 0f).put(1, 0f).put(2, 0f).put(3, 1f));
        renderer.render(scene, cam, wireFrame);
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
