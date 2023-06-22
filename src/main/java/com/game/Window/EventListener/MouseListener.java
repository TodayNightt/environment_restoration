package com.game.Window.EventListener;

public class MouseListener {
    private static MouseListener mouseListener;
    private float posX, posY, lastX, lastY;

    private MouseListener() {
        this.posX = 0.0f;
        this.posY = 0.0f;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
    }

//    public static float getOrthoX() {
//        getX()
//    }
//
//    public static float getOrthoY() {
//
//    }

    /**
     * Get the cursor position from glfw cursorCallback, then convert the coordinates to Normalized Device Coordinates(NDC)
     **/
    public static void cursorCallback(long window, double xPos, double yPos) {
        getInstance().lastX = getInstance().posX;
        getInstance().lastY = getInstance().posY;
        getInstance().posX = (float) xPos;
        getInstance().posY = (float) yPos;
    }

//    public static float getOrthoX() {
//        getInstance().posX = ((float) xPos / com.game.Window.getWidth()) * 2.0f - 1.0f;
//
//    }
//
//    public static float getOrthoY() {
//        getInstance().posY = ((float) yPos / com.game.Window.getHeight()) * 2.0f - 1.0f;
//    }

    public static float getX() {
        return getInstance().posX;
    }

    public static float getY() {
        return getInstance().posY;
    }


    public static MouseListener getInstance() {
        if (mouseListener == null) {
            mouseListener = new MouseListener();
        }
        return mouseListener;
    }

}
