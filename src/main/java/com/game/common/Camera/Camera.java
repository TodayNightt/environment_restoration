package com.game.common.Camera;

import com.game.common.Utils.Mat4f;
import com.game.common.Utils.Vec3f;
import com.game.common.Utils.Vec3i;

import static com.game.common.MatrixCalc.rotationMatrix;

public class Camera {
    private final Mat4f viewMatrix;
    private Mat4f inverseViewMatrix;
    private final Mat4f projectionMatrix;
    private Mat4f inverseProjectionMatrix;
    private final Mat4f orthoProjection;
    private Vec3f position, lookDir, target, up;
    private final Vec3i realPosition;

    private float windowWidth;
    private float windowHeight;


    private float FOV, Z_NEAR, Z_FAR, aspectRatio, yaw, pan;

    public Camera() {
        this.viewMatrix = new Mat4f();
        this.projectionMatrix = new Mat4f();
        this.inverseViewMatrix = new Mat4f();
        this.inverseProjectionMatrix = new Mat4f();
        this.orthoProjection = new Mat4f();
        this.realPosition = new Vec3i();
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
        this.inverseProjectionMatrix = this.projectionMatrix.invert();
        this.orthoProjection.identity().ortho(0.f, windowWidth, windowHeight, 0.f, -1.f, 1.f);
    }

    public void setWindowSize(float width, float height) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.aspectRatio = width / height;
        updatePerspective();
    }

    // com.game.Camera
    public void setCamera(Vec3f pos, Vec3f target, Vec3f up, float yaw) {
        this.position = pos;
        this.target = target;
        this.up = up;
        this.yaw = yaw;
        updateCamera();
    }

    public void updateCamera() {
        Mat4f rotationY = rotationMatrix(yaw, (byte) 2);
        Mat4f rotationX = rotationMatrix(pan, (byte) 1);
        this.lookDir = new Vec3f(target).mulDirection(rotationX).mulDirection(rotationY);
        this.viewMatrix.identity().lookAlong(lookDir, up).translate(position);
        this.inverseViewMatrix = this.viewMatrix.invert();
        this.realPosition.set(inverseViewMatrix.m30(), inverseViewMatrix.m31(), inverseViewMatrix.m32());
    }

    public Mat4f getOrthoProjection() {
        return orthoProjection;
    }


    public void up() {
        position.addY(-0.4f);
        updateCamera();
    }

    public void down() {
        position.addY(0.4f);
        updateCamera();
    }

    public void left() {
        position.addX(0.1f);
        updateCamera();
    }

    public void right() {
        position.addX(-0.1f);
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
            panDown();
        if (keys[7])
            panUp();
//        if (keys[8])
//            addPiece(0);
//        if (keys[9])
//            addPiece(1);
//        if (keys[10])
//            addPiece(2);
    }

//    public void addPiece(int index) {
//        System.out.println("Hello");
//        PieceManager.addPiece(PieceCollection.getPieceType().get(index), (realPosition.x() + 3 * lookDir.x()), realPosition.y(), (realPosition.z() + 3 * lookDir.z()));
//    }

    private void forward() {
        Vec3f forward = new Vec3f(lookDir).normalize().mul(0.4f).setY(0f);
        position.add(forward);
        updateCamera();
    }

    private void backward() {
        Vec3f forward = new Vec3f(lookDir).normalize().mul(0.4f).setY(0f);
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

    public Mat4f getProjectionMatrix() {
        return projectionMatrix;
    }


    public Mat4f getViewMatrix() {
        return this.viewMatrix;
    }


    public Vec3i getPosition() {
        return realPosition;
    }

}