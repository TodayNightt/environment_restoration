package Graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.jogamp.opengl.GL3;

import static org.joml.Math.cos;
import static org.joml.Math.sin;

public class Chunk {
    public static final int CHUNK_SIZE = 15;
    public static final int CHUNK_HEIGHT = 20;
    private FloatBuffer vertexDataBuffer;
    private Mesh mesh;
    // private int CHUNK_SQR = CHUNK_SIZE * CHUNK_SIZE;
    // private int CHUNK_CUBE = CHUNK_SQR * CHUNK_SIZE;
    private Cube[] entities;
    private int numVertices;
    private Matrix4f modelMatrix;

    // private Mesh mesh;
    private GL3 gl;

    public Chunk(GL3 gl, int xo, int yo, int zo, int[] givenMap) {
        this.gl = gl;
        entities = new Cube[CHUNK_SIZE * CHUNK_SIZE * CHUNK_HEIGHT];
        int[] heightMap = givenMap;
        // https: // stackoverflow.com/questions/38204579/flatten-3d-array-to-1d-array
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    entities[x + (z * CHUNK_SIZE) + (y * (CHUNK_SIZE * CHUNK_SIZE))] = new Cube(new Vector3f(x, y, z),
                            null, y <= heightMap[x + (z * CHUNK_SIZE)] ? true : false);

                }
            }
        }

        initializeBuffers();
        this.modelMatrix = createModelMatrix(rotationMatrix(0.0f, (byte) 1), rotationMatrix(0.0f, (byte) 2),
                rotationMatrix(0.0f, (byte) 3), new Matrix4f().translation(xo * CHUNK_SIZE, yo, zo * CHUNK_SIZE),
                new Matrix4f().scale(1.0f));
    }

    private void initializeBuffers() {
        vertexDataBuffer = FloatBuffer
                .allocate(((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE) + (CHUNK_HEIGHT * CHUNK_SIZE * 4)) * 24);
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    Cube self = entities[x + (z * CHUNK_SIZE) + (y * (CHUNK_SIZE * CHUNK_SIZE))];
                    if (self == null) {
                        continue;
                    }
                    Cube left = x > 0 ? entities[(x - 1) + (z * CHUNK_SIZE) + y * (CHUNK_SIZE * CHUNK_SIZE)]
                            : new Cube(new Vector3f(x - 1, y, z), null, false);
                    Cube bottom = y > 0 ? entities[x + (z * CHUNK_SIZE) + (y - 1) * (CHUNK_SIZE * CHUNK_SIZE)]
                            : new Cube(new Vector3f(x, y - 1, z), null, false);
                    Cube back = z > 0 ? entities[x + ((z - 1) * CHUNK_SIZE) + y * (CHUNK_SIZE * CHUNK_SIZE)]
                            : new Cube(new Vector3f(x, y, z - 1), null, false);
                    Cube right = x < CHUNK_SIZE - 1
                            ? entities[(x + 1) + (z * CHUNK_SIZE) + y * (CHUNK_SIZE * CHUNK_SIZE)]
                            : new Cube(new Vector3f(x + 1, y, z), null, false);
                    Cube front = z < CHUNK_SIZE - 1
                            ? entities[x + ((z + 1) * CHUNK_SIZE) + y * (CHUNK_SIZE * CHUNK_SIZE)]
                            : new Cube(new Vector3f(x, y, z + 1), null, false);

                    Quad.processQuad(vertexDataBuffer, self, left, right, bottom, back, front);

                }
            }
        }
        IntBuffer indicesBuffer = IntBuffer.allocate((vertexDataBuffer.capacity()));
        int index = 0;
        for (int i = 0; i < indicesBuffer.capacity(); i += 6) {
            indicesBuffer.put(index).put(index + 1).put(index + 2).put(index + 1).put(index + 2).put(index + 3);
            index += 4;
        }
        // for (int array : indicesBuffer.array()) {
        // System.out.print(array + " ");
        // }
        // Create VAO
        mesh = new Mesh(gl, vertexDataBuffer.array(), indicesBuffer.array());

    }

    // public void addCube(float x, float y, float z, float angleX, float angleY,
    // float angleZ, float scale, String type) {
    // Matrix4f rotationX = rotationMatrix(angleX, (byte) 1);
    // Matrix4f rotationY = rotationMatrix(angleY, (byte) 2);
    // Matrix4f rotationZ = rotationMatrix(angleZ, (byte) 3);
    // Matrix4f translation = new Matrix4f().translate(x, y, z);
    // Matrix4f scaleMatrix = new Matrix4f().scale(scale);
    // Matrix4f modelMatrix = createModelMatrix(rotationX, rotationY, rotationZ,
    // translation, scaleMatrix);

    // entities.add(new Cube(new Vector3f(x, y, z), modelMatrix, type,
    // new boolean[] { true, true, true, true, true, true }));
    // }

    // OverLoad
    // public void addCube(float x, float y, float z, float angleX, float angleY,
    // float angleZ, float scaleX, float scaleY,
    // float scaleZ, String type) {
    // Matrix4f rotationX = rotationMatrix(angleX, (byte) 1);
    // Matrix4f rotationY = rotationMatrix(angleY, (byte) 2);
    // Matrix4f rotationZ = rotationMatrix(angleZ, (byte) 3);
    // Matrix4f translation = new Matrix4f().translate(x, y, z);
    // Matrix4f scaleMatrix = new Matrix4f().scale(scaleX, scaleY, scaleZ);
    // Matrix4f modelMatrix = createModelMatrix(rotationX, rotationY, rotationZ,
    // translation, scaleMatrix);

    // entities.add(new Cube(new Vector3f(x, y, z), modelMatrix, type));
    // }

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

    // public List<Cube> getEntitiesList() {
    // return entities;
    // }

    public Mesh getMesh() {
        return mesh;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public record Cube(Vector3f position, String type, boolean is_Solid) {
    }
}
