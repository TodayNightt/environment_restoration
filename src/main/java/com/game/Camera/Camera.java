package com.game.Camera;

import com.game.GameLogic.PieceManager;
import com.game.Graphics.PieceCollection;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import static com.game.Utils.MatrixCalc.rotationMatrix;

public class Camera {
    private final Matrix4f viewMatrix, inverseViewMatrix;
    private final Matrix4f projectionMatrix, inverseProjectionMatrix;
    private final Matrix4f orthoProjection;
    private Vector3f position, lookDir, target, up;
    private final Vector3f realPosition;

    private float windowWidth;
    private float windowHeight;


    private float FOV, Z_NEAR, Z_FAR, aspectRatio, yaw, pan;

    public Camera() {
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        this.inverseViewMatrix = new Matrix4f();
        this.inverseProjectionMatrix = new Matrix4f();
        this.orthoProjection = new Matrix4f();
        this.realPosition = new Vector3f();
    }

    // Projection
    public void setPerspective(float FOV, float aspectRatio, float Z_NEAR, float Z_FAR) {
        this.FOV = (float) Math.toRadians(FOV);
        this.Z_NEAR = Z_NEAR;
        this.Z_FAR = Z_FAR;
        this.aspectRatio = aspectRatio;
        updatePerspective();
    }

    private void updatePerspective() {
        this.projectionMatrix.identity().setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        this.projectionMatrix.invert(inverseProjectionMatrix);
        this.orthoProjection.identity().ortho(0.f, windowWidth, windowHeight, 0.f, -1.f, 1.f);
    }

    public void setWindowSize(float width, float height) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.aspectRatio = width / height;
        updatePerspective();
    }

    // com.game.Camera
    public void setCamera(Vector3f pos, Vector3f target, Vector3f up, float yaw) {
        this.position = pos;
        this.target = target;
        this.up = up;
        this.yaw = yaw;
        updateCamera();
    }

    public void updateCamera() {
        Matrix4f rotationY = rotationMatrix(yaw, (byte) 2);
        Matrix4f rotationX = rotationMatrix(pan, (byte) 1);
        this.lookDir = new Vector3f(target).mulDirection(rotationX).mulDirection(rotationY);
        this.viewMatrix.identity().setLookAlong(lookDir, up).translate(position);
        this.viewMatrix.invert(inverseViewMatrix);
        this.realPosition.set(inverseViewMatrix.m30(),inverseViewMatrix.m31(),  inverseViewMatrix.m32());
    }

    public Matrix4f getOrthoProjection() {
        return orthoProjection;
    }


    public void up() {
        position.y-=0.4f;
        updateCamera();
    }

    public void down() {
        position.y+=0.4f;
        updateCamera();
    }

    public void left() {
        position.x += 0.1f;
        updateCamera();
    }

    public void right() {
        position.x -= 0.1f;
        updateCamera();
    }

    public void key(boolean[] keys) {
        if (keys[0])
            forward();
        if (keys[1])
            yawLeft();
        if (keys[2])
            backward();
        if (keys[3])
            yawRight();
        if (keys[4])
            up();
        if (keys[5])
            down();
        if (keys[6])
            panUp();
        if (keys[7])
            panDown();
        if (keys[8])
            addPiece(0);
        if (keys[9])
            addPiece(1);
        if (keys[10])
            addPiece(2);
    }

    public void addPiece(int index) {
        PieceManager.addPiece(PieceCollection.getPieceType().get(index), (realPosition.x() + 3 * lookDir.x()), realPosition.y(), (realPosition.z() + 3 * lookDir.z()));
    }

    private void forward() {
        Vector3f forward = new Vector3f(lookDir).normalize().mul(0.4f).negate();
        forward.y = 0f;
        position.add(forward);
        updateCamera();
    }

    private void backward() {
        Vector3f forward = new Vector3f(lookDir).normalize().mul(0.4f).negate();
        forward.y = 0f;
        position.sub(forward);
        updateCamera();
    }

    private void yawLeft() {
        yaw -= 1.0f;
        updateCamera();
    }

    private void yawRight() {
        yaw += 1.0f;
        updateCamera();
    }

    private void panDown() {
        pan -= .5f;
        updateCamera();
    }

    private void panUp() {
        pan += .5f;
        updateCamera();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }


    public Matrix4f getViewMatrix() {
        return this.viewMatrix;
    }


    public Vector3f getPosition() {
        return realPosition;
    }

}