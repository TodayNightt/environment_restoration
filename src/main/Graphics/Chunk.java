package Graphics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.joml.Math.cos;
import static org.joml.Math.sin;

public class Chunk {
    private List<Cube> entities;
    private Mesh mesh;

    public Chunk(Mesh mesh) {
        entities = new ArrayList<>();
        this.mesh = mesh;
        for (float x = -3; x < 3; x++) {
            for (float z = -6; z < 0; z++) {
                addCube(x, -1.0f, z, 0.0f, 0.0f, 0.0f, 0.5f);
            }
        }
    }

    public void addCube(float x, float y, float z, float angleX, float angleY, float angleZ, float scale) {
        Matrix4f rotationX = rotationMatrix(angleX, (byte) 1);
        Matrix4f rotationY = rotationMatrix(angleY, (byte) 2);
        Matrix4f rotationZ = rotationMatrix(angleZ, (byte) 3);
        Matrix4f translation = new Matrix4f().translate(x, y, z);
        Matrix4f scaleMatrix = new Matrix4f().scale(scale);
        Matrix4f modelMatrix = createModelMatrix(rotationX, rotationY, rotationZ, translation, scaleMatrix);

        entities.add(new Cube(new Vector3f(x, y, z), modelMatrix));
    }

    // OverLoad
    public void addCube(float x, float y, float z, float angleX, float angleY, float angleZ, float scaleX, float scaleY,
            float scaleZ) {
        Matrix4f rotationX = rotationMatrix(angleX, (byte) 1);
        Matrix4f rotationY = rotationMatrix(angleY, (byte) 2);
        Matrix4f rotationZ = rotationMatrix(angleZ, (byte) 3);
        Matrix4f translation = new Matrix4f().translate(x, y, z);
        Matrix4f scaleMatrix = new Matrix4f().scale(scaleX, scaleY, scaleZ);
        Matrix4f modelMatrix = createModelMatrix(rotationX, rotationY, rotationZ, translation, scaleMatrix);

        entities.add(new Cube(new Vector3f(x, y, z), modelMatrix));
    }

    private static Matrix4f createModelMatrix(Matrix4f rotationX, Matrix4f rotationY, Matrix4f rotationZ,
            Matrix4f translation, Matrix4f scale) {
        return new Matrix4f().identity().mul(rotationX).mul(rotationY).mul(rotationZ).mul(translation).mul(scale);
    }

    // https://github.com/OneLoneCoder/Javidx9/blob/master/ConsoleGameEngine/BiggerProjects/Engine3D/OneLoneCoder_olcEngine3D_Part3.cpp
    public static Matrix4f rotationMatrix(float angle, byte mode) {
        float angleRad = (float) Math.toRadians(angle);
        switch (mode) {
            case 1:
                return new Matrix4f()
                        .m00(1.0f)
                        .m11(cos(angleRad))
                        .m12(-sin(angleRad))
                        .m21(sin(angleRad))
                        .m22(cos(angleRad))
                        .m33(1.0f);
            case 2:
                return new Matrix4f()
                        .m00(cos(angleRad))
                        .m02(sin(angleRad))
                        .m20(-sin(angleRad))
                        .m11(1.0f)
                        .m22(cos(angleRad))
                        .m33(1.0f);
            case 3:
                return new Matrix4f()
                        .m00(cos(angleRad))
                        .m01(-sin(angleRad))
                        .m10(sin(angleRad))
                        .m11(cos(angleRad))
                        .m22(1.0f)
                        .m33(1.0f);

        }
        return null;
    }

    public List<Cube> getEntitiesList() {
        return entities;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public record Cube(Vector3f position, Matrix4f modelMatrix) {
    }
}
