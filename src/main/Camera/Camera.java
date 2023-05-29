package Camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import Graphics.Chunk;

public class Camera {
    private Matrix4f viewMatrix, projectionMatrix, rotation;
    private Vector3f position, lookDir, target, up;

    private float FOV, Z_NEAR, Z_FAR, aspectRatio, yaw;

    public Camera() {
        this.viewMatrix = new Matrix4f();
        this.projectionMatrix = new Matrix4f();
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
    }

    public void setAspectRatio(float width, float height) {
        this.aspectRatio = (float) width / height;
        updatePerspective();
    }

    // Camera
    public void setCamera(Vector3f pos, Vector3f target, Vector3f up, float yaw) {
        this.position = pos;
        this.target = target;
        this.up = up;
        this.yaw = yaw;
        updateCamera();
    }

    public void updateCamera() {
        this.rotation = Chunk.rotationMatrix(yaw, (byte) 2);
        this.lookDir = new Vector3f(target).mulDirection(rotation).normalize();
        // this.target = new Vector3f(position).add(lookDir);
        // this.target.y = 0;
        // this.viewMatrix = pointAt(position, target, up).invert();
        // this.yaw = 0;

        this.viewMatrix.setLookAlong(lookDir, up).translate(position);

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
        position.y -= 0.1f;
        updateCamera();
    }

    public void down() {
        position.y += 0.1f;
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
        else if (keys[1])
            yawLeft();
        else if (keys[2])
            backward();
        else if (keys[3])
            yawRight();
        else if (keys[4])
            up();
        else if (keys[5])
            down();
        else if (keys[6])
            left();
        else if (keys[7])
            right();
    }

    public void forward() {
        Vector3f forward = new Vector3f(lookDir).normalize().mul(0.06f);
        position.sub(forward);
        updateCamera();
    }

    public void backward() {
        Vector3f forward = new Vector3f(lookDir).normalize().mul(0.06f);
        position.add(forward);
        updateCamera();
    }

    public void yawLeft() {
        yaw -= 1.0f;
        updateCamera();
    }

    public void yawRight() {
        yaw += 1.0f;
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

};