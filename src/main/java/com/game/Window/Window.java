package com.game.Window;

import com.game.GameLogic.PieceCollection;
import com.game.GameLogic.PieceManager;
import com.game.Graphics.Renderer;
import com.game.Graphics.Scene;
import com.game.Window.EventListener.KeyListener;
import com.game.Window.EventListener.MouseListener;
import org.joml.Random;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


//https://github.com/SpaiR/imgui-java/blob/main/imgui-app/src/main/java/imgui/app/Window.java#L25
public class Window {

    private static Window window;
//    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
//    private final ImGuiImplGl3 imGuiGl = new ImGuiImplGl3();
    private Scene scene;
    // The window handle
    private long windowID;
    private int width = 1280, height = 720;
    private Callback debugProc;

    private Window() {

    }

    public static void launch(Window app) throws Exception {
        app.init();
        app.initComponent();
        app.run();
        app.disposeAll();
    }

    public static Window getInstance() {
        if (window == null) {
            window = new Window();
        }
        return window;
    }

    public static float getHeight() {
        return getInstance().height;
    }

    public static float getWidth() {
        return getInstance().width;
    }

    private static void resized(long window, int width, int height) {
        getInstance().width = width;
        getInstance().height = height;
//        getInstance().scene.getButtonManager().evalPlacement();
        getInstance().scene.getCamera().setAspectRatio(getWidth(), getHeight());
    }

    private void initComponent() throws Exception {
        scene = new Scene(this);
    }

    public void init() throws Exception {
        initWindow();
        initImGui();
//        imGuiGlfw.init(windowID, true);
//        imGuiGl.init();
    }

    protected void initImGui() {
//        ImGui.createContext();
//
//        final ImGuiIO io = ImGui.getIO();
//        io.setIniFilename("src/main/resources/imgui.ini");
    }

    public void initWindow() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // Create the window
        windowID = glfwCreateWindow(width, height, "Environment Restoration", NULL, NULL);
        if (windowID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetWindowAspectRatio(windowID, 16, 9); //Lock aspect ratio

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowID, KeyListener::keyCallBack);
        glfwSetCursorPosCallback(windowID, MouseListener::cursorCallback);

        glfwSetFramebufferSizeCallback(windowID, Window::resized);




        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowID, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert vidmode != null;
            glfwSetWindowPos(
                    windowID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

        } // the stack frame is popped automatically


        // Make the OpenGL context current
        glfwMakeContextCurrent(windowID);
        // Enable v-sync
        glfwSwapInterval(1);
        GL.createCapabilities();

//        debugProc = GLUtil.setupDebugMessageCallback();
        System.out.println(glGetString(GL_VERSION));


        // Make the window visible
        glfwShowWindow(windowID);


        //Set depth buffer
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);
        glDepthRange(0.f, 1.0f);
    }

    protected void run() {
        while (!glfwWindowShouldClose(windowID)) {
            runFrame();
        }
    }

    protected void runFrame() {
        //Start
        startFrame();

        //Render
        render();

        //End
        endFrame();
    }

    protected void startFrame() {
        clearBuffer();
//        imGuiGlfw.newFrame();
//        ImGui.newFrame();
    }

    public void render() {
        scene.render();
    }

    protected void endFrame() {
//        ImGui.render();
//        imGuiGl.renderDrawData(ImGui.getDrawData());
//
//        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//            ImGui.updatePlatformWindows();
//            ImGui.renderPlatformWindowsDefault();
//            GLFW.glfwMakeContextCurrent(windowID);
//        }

        glViewport(0, 0, width, height);
        renderBuffer();
    }

    private void renderBuffer() {
        GLFW.glfwSwapBuffers(windowID);
        GLFW.glfwPollEvents();
    }

    protected void clearBuffer() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glClearBufferfv(GL_COLOR, 0, new float[]{0f, 0f, 0f, 1f});
//            glClearBufferfv(GL_COLOR, 0, clearColor.put(0, 0f).put(1, 0f).put(2, 0f).put(3, 1f));
        glClearDepth(1.0f);
    }

    private void disposeAll() {
//        imGuiGl.dispose();
//        imGuiGlfw.dispose();
//        ImGui.destroyContext();
        scene.cleanup();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

    }


}