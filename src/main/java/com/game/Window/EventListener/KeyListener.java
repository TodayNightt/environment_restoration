package com.game.Window.EventListener;

import com.game.Graphics.Renderer;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener keyListener;
    private final int[] checkKey = new int[]{GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_S, GLFW_KEY_D, GLFW_KEY_UP,
            GLFW_KEY_DOWN, GLFW_KEY_LEFT, GLFW_KEY_RIGHT,GLFW_KEY_B, GLFW_KEY_ESCAPE};
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
            if(key == GLFW_KEY_N){
                Renderer.wireFrame();
            }
            changeState(window, true, key);
        } else if (action == GLFW_RELEASE) {
            changeState(window, false, key);
        }
    }


    private static void changeState(long window, boolean state, int keyCode) {
        KeyListener instance = getInstance();
        if (keyCode == instance.checkKey[instance.checkKey.length - 1])
            glfwSetWindowShouldClose(window, true);
        for (int i = 0; i < instance.checkKey.length - 1; i++) {
            if (keyCode == instance.checkKey[i]) {
                instance.pressed[i] = state;
            }
        }
    }

    public boolean[] getPressed() {
        return pressed;
    }
}
