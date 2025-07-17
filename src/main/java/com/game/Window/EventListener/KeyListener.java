package com.game.Window.EventListener;

import com.game.Window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    public enum Key {
        W(0), A(1), S(2), D(3), Up(4), Down(5), Left(6), Right(7), J(8), K(9), L(10), C(11), N(12), Esc(13);

        public int index;

        private Key(int key) {
            this.index = key;
        }

        public static int getIndex(Key key) {
            return key.index;
        }

    }

    private static KeyListener keyListener;
    private final int[] checkKey = new int[] { GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_S, GLFW_KEY_D, GLFW_KEY_UP,
            GLFW_KEY_DOWN, GLFW_KEY_LEFT, GLFW_KEY_RIGHT, GLFW_KEY_J, GLFW_KEY_K, GLFW_KEY_L, GLFW_KEY_C, GLFW_KEY_N,
            GLFW_KEY_ESCAPE };
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
            changeState(true, key);
        } else if (action == GLFW_RELEASE) {
            changeState(false, key);
        }
    }

    private static void changeState(boolean state, int keyCode) {
        KeyListener instance = getInstance();
        if (keyCode == instance.checkKey[Key.getIndex(Key.Esc)])
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
