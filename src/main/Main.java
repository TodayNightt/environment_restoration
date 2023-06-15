import Camera.Camera;
import Graphics.Renderer;
import Graphics.Scene;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
//import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengles.GLES;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;


import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.egl.EGL15.*;
import static org.lwjgl.opengles.GLES31.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    protected FloatBuffer clearColor = FloatBuffer.allocate(8),
            clearDepth = FloatBuffer.allocate(1);
    // The window handle
    private long window;
    private int width=1280,height= 720;
    private Camera camera;
    private Scene scene;
    private Renderer renderer;
    private Callback debugProc;

    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    public void run() throws Exception {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");


        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() throws Exception {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_ANY_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_CREATION_API,GLFW_EGL_CONTEXT_API);
        glfwWindowHint(GLFW_CLIENT_API,GLFW_OPENGL_ES_API);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);

        // Create the window
        window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwSetFramebufferSizeCallback(window,(window,width,height)-> resized(width,height));

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        GLES.createCapabilities();
        System.out.println(glGetString(GL_VERSION));
//        debugProc = GLDebugMessageCallback.
        initComponent();

        // Make the window visible
        glfwShowWindow(window);
    }

    private void initComponent() throws Exception {
        camera = new Camera();
        camera.setCamera(new Vector3f(0.0f, 0.0f, -5.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 1.0f, 0.0f), 0.0f);
        camera.setPerspective(60.f, (float) width / height, 0.1f, 1000.0f);
        scene = new Scene();
        renderer = new Renderer();
    }

    private void loop(){
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.


        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);
        glDepthRangef(0.f,1.0f);
//        glDepthRange(0.f, 1.0f);


        // Set the clear color
//        glClearColor(1.0f, 1.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glClearBufferfv(GL_COLOR, 0, new float[]{0f,0f,0f,1f});
//            glClearBufferfv(GL_COLOR, 0, clearColor.put(0, 0f).put(1, 0f).put(2, 0f).put(3, 1f));
            glClearDepthf(1.0f);

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            glViewport(0, 0, width, height);

            renderer.render(scene,camera,false);

            glfwSwapBuffers(window); // swap the color buffers
        }
    }

    private void resized(int width,int height){
        this.width = width;
        this.height= height;
        camera.setAspectRatio(width,height);
    }

}