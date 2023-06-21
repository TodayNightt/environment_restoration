package com.game.Camera;

import com.game.Window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static Graphics.MatrixCalc.rotationMatrix;

public class Camera {
    private final Matrix4f viewMatrix, inverseViewMatrix;
    private final Matrix4f projectionMatrix, inverseProjectionMatrix, orthoProjection;
    private Vector3f position, lookDir, target, up;

    private float FOV, Z_NEAR, Z_FAR, aspectRatio, yaw, pan;

    public Camera() {
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
        this.inverseViewMatrix = new Matrix4f();
        this.inverseProjectionMatrix = new Matrix4f();
        this.orthoProjection = new Matrix4f();
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
        this.orthoProjection.identity().ortho(0.f, Window.getWidth(), Window.getHeight(), 0.f, -1.f, 1.f);
    }

    public void setAspectRatio(float width, float height) {
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
        this.lookDir = new Vector3f(target).mulDirection(rotationY).normalize();
        this.viewMatrix.setLookAlong(lookDir, up).translate(position);
        this.viewMatrix.invert(inverseViewMatrix);

    }

    public Matrix4f getOrthoProjection() {
        return orthoProjection;
    }

    // https://github.com/OneLoneCoder/Javidx9/blob/master/ConsoleGameEngine/BiggerProjects/Engine3D/OneLoneCoder_olcEngine3D_Part3.cpp
    // private static Matrix4f pointAt(Vector3f pos, Vector3f target, Vector3f up) {
    // Vector3f newForward = new Vector3f(target).sub(pos).normalize();

    // Vector3f a = new Vector3f(newForward).mul(up.dot(newForward));
    // Vector3f newUp = new Vector3f(up).sub(a);
    // newUp.normalize();

    // Vector3f right = new Vector3f(newUp).cross(newForward);

    // return new Matrix4f(right.x(), right.y(), right.z(), 0.0f,
    // newUp.x(), newUp.y(), newUp.z(), 0.0f,
    // newForward.x(), newForward.y(), newForward.z(), 0.0f,
    // pos.x(), pos.y(), pos.z(), 1.0f);
    // }

    public void up() {
        position.y -= 0.4f;
        updateCamera();
    }

    public void down() {
        position.y += 0.4f;
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
    }

    private void forward() {
        Vector3f forward = new Vector3f(lookDir).normalize().mul(0.4f);
        position.sub(forward);
        updateCamera();
    }

    private void backward() {
        Vector3f forward = new Vector3f(lookDir).normalize().mul(0.4f);
        position.add(forward);
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
        pan += 1.0f;
        updateCamera();
    }

    private void panUp() {
        pan -= 1.0f;
        updateCamera();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }
}