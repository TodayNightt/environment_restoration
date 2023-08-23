package com.game.lwjgl.Window.EventListener;

import com.game.lwjgl.Graphics.LwjglRenderer;
import com.game.lwjgl.Window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener keyListener;
    private final int[] checkKey = new int[]{GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_S, GLFW_KEY_D, GLFW_KEY_UP,
            GLFW_KEY_DOWN, GLFW_KEY_LEFT, GLFW_KEY_RIGHT, GLFW_KEY_J, GLFW_KEY_K, GLFW_KEY_L, GLFW_KEY_ESCAPE};
    private final boolean[] pressed = new boolean[checkKey.length];


    private KeyListener() {
    }

    public static KeyListener getInstance() {
        if (keyListener == null) {
            keyListener = new KeyListener();
        }

        return keyListener;
    }

    public static void keyCallBack(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_N) {
                LwjglRenderer.wireFrame();
            }
            changeState(true, key);
        } else if (action == GLFW_RELEASE) {
            changeState(false, key);
        }
    }


    private static void changeState(boolean state, int keyCode) {
        KeyListener instance = getInstance();
        if (keyCode == instance.checkKey[instance.checkKey.length - 1])
            glfwSetWindowShouldClose(Window.getWindowId(), true);
        for (int i = 0; i < instance.checkKey.length - 1; i++) {
            if (keyCode == instance.checkKey[i]) {
                instance.pressed[i] = state;
            }
        }
    }

    public static void declicker(int[] codes) {
        for (int code : codes) {
            changeState(false, code);
        }
    }

    public boolean[] getPressed() {
        return pressed;
    }
}
