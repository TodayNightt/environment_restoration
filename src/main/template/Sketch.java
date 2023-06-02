package template;

import java.nio.FloatBuffer;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;

public class Sketch implements GLEventListener, KeyListener, MouseListener {

    public final boolean DEBUG = false;
    protected GLWindow window;
    protected Animator animator;
    protected FloatBuffer clearColor = GLBuffers.newDirectFloatBuffer(4),
            clearDepth = GLBuffers.newDirectFloatBuffer(1);
    public static FloatBuffer matBuffer = GLBuffers.newDirectFloatBuffer(16),
            vecBuffer = GLBuffers.newDirectFloatBuffer(4);

    private GLProfile glProfile;

    public Sketch(String name) {
        setup(name);
    }

    public Sketch() {
    }

    public void setup(String name) {
        glProfile = GLProfile.get(GLProfile.GL3);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        // glCapabilities.setDepthBits(16);

        window = GLWindow.create(glCapabilities);

        if (DEBUG) {
            window.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        }

        window.setUndecorated(false);
        window.setAlwaysOnTop(false);
        window.setFullscreen(false);
        window.setPointerVisible(true);
        window.setTitle(name);
        window.setSize(1200, 720);

        window.setVisible(true);

        window.addGLEventListener(this);
        window.addKeyListener(this);
        window.addMouseListener(this);

        animator = new Animator();
        animator.add(window);
        animator.start();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyed(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {

                        // stop the animator thread when user close the window
                        animator.stop();
                        // This is actually redundant since the JVM will terminate when all threads are
                        // closed.
                        // It's useful just in case you create a thread and you forget to stop it.
                        quit();
                    }
                }).start();
            }
        });

    }

    @Override
    public final void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glDepthMask(true);
        gl.glDepthFunc(GL3.GL_LEQUAL);
        gl.glDepthRange(0.f, 1.0f);

        try {
            init(gl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(GL3 gl) {
    }

    @Override
    public final void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glClearColor(0.f, 0.f, 0.f, 0.f);
        gl.glClearDepth(1.0f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        display(gl);

    }

    protected void display(GL3 gl) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        reshape(drawable.getGL().getGL3(), width, height);
    }

    protected void reshape(GL3 gl, int width, int height) {
    }

    @Override
    public final void dispose(GLAutoDrawable drawable) {
        end(drawable.getGL().getGL3());
    }

    protected void end(GL3 gl) {
    }

    protected void quit() {
        new Thread(new Runnable() {
            public void run() {
                window.destroy();
            }
        }).start();
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mouseWheelMoved(MouseEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

}