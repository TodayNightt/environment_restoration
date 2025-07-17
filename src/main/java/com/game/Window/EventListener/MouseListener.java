package com.game.Window.EventListener;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings({ "unused" })
public class MouseListener {
    private static MouseListener mouseListener;
    private float posX, posY, lastX, lastY;
    private int clickIndex;
    private boolean mouseJustPressed;

    private MouseListener() {
        this.posX = 0.0f;
        this.posY = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
    }

    /**
     * Get the cursor position from glfw cursorCallback, then convert the
     * coordinates to Normalized Device Coordinates(NDC)
     **/
    public static void cursorCallback(long window, double xPos, double yPos) {
        getInstance().lastX = getInstance().posX;
        getInstance().lastY = getInstance().posY;
        getInstance().posX = (float) xPos;
        getInstance().posY = (float) yPos;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mod) {
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            getInstance().mouseJustPressed = true;
        }
    }

    public static void resetMouse() {
        getInstance().mouseJustPressed = false;
    }

    public static float getX() {
        return getInstance().posX;
    }

    public static float getY() {
        return getInstance().posY;
    }

    public static boolean getMouseJustPressed() {
        return getInstance().mouseJustPressed;
    }

    public static MouseListener getInstance() {
        if (mouseListener == null) {
            mouseListener = new MouseListener();
        }
        return mouseListener;
    }

}
