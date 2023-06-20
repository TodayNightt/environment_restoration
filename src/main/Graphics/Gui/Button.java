package Graphics.Gui;

import Window.Window;
import org.joml.Vector3d;

import java.awt.*;

public class Button {
    private static final Color normal = new Color(200, 0, 100);
    private static final Color selected = new Color(0, 170, 200);
    private final float leftMost, topMost, buttonSize;
    private final float initialWindowWidth;
    private float resizeFactor;
    private ButtonMesh mesh;
    private Vector3d position;
    private String pieceType;
    private Color currentColor;

    private Button(float leftMost, float topMost, float buttonSize) {
        this.initialWindowWidth = Window.getWidth();
        this.leftMost = initialWindowWidth * leftMost;
        this.topMost = initialWindowWidth * topMost;
        this.buttonSize = initialWindowWidth * buttonSize;
        this.resizeFactor = Window.getWidth() / initialWindowWidth;
        this.currentColor = normal;
        generateMesh();
    }

    protected static Button createButton(float leftMost, float topMost, float buttonSize) {
        return new Button(leftMost, topMost, buttonSize);
    }

    private void generateMesh() {
        mesh = ButtonMesh.createSquare(leftMost, topMost, buttonSize);
    }

    protected boolean isCurrent(float posX, float posY) {
        if (posX > leftMost * resizeFactor && posX < leftMost + buttonSize * resizeFactor && posY < topMost + buttonSize * resizeFactor && posY > topMost * resizeFactor) {
            currentColor = selected;
            return true;
        }
        currentColor = normal;
        return false;
    }

    public Color getColor() {
        return currentColor;
    }


    protected String getType() {
        return pieceType;
    }

    public ButtonMesh getMesh() {
        return mesh;
    }

    protected void eval() {
        resizeFactor = Window.getWidth() / initialWindowWidth;
    }

    public float getResizeFactor() {
        return resizeFactor;
    }
}
