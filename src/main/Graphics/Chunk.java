package Graphics;

import Terrain.TerrainMap;
import com.jogamp.opengl.GL3;
import org.joml.*;

import java.lang.Math;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import static org.joml.Math.cos;
import static org.joml.Math.sin;

public class Chunk {
    public static final int CHUNK_SIZE = 15;
    public static final int CHUNK_HEIGHT = 40;
    private static final int CHUNK_SQR = CHUNK_SIZE * CHUNK_SIZE;
    private static final int CHUNK_CUBE = CHUNK_SQR * CHUNK_SIZE;
    public static final int SEA_LEVEL = 10;
    public static final int MOUNTAIN_LEVEL = CHUNK_HEIGHT - 6;
    private Mesh mesh;
    private final Cube[] entities;
    private final Matrix4f modelMatrix;
    private final Vector3ic position;
    private final TerrainMap parentMap;
    private final GL3 gl;

    private enum Direction {
        TOP, BOTTOM, LEFT, RIGHT, FRONT, BACK
    }

    public Chunk(GL3 gl, int xo, int yo, int zo, int[] givenMap, TerrainMap parentMap) {
        this.gl = gl;
        entities = new Cube[CHUNK_SIZE * CHUNK_SIZE * CHUNK_HEIGHT];
        this.parentMap = parentMap;
        this.position = new Vector3i(xo, yo, zo);

        // https: // stackoverflow.com/questions/38204579/flatten-3d-array-to-1d-array
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    entities[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))] = new Cube(new Vector3f(x, y, z),
                            getMaterial(y,(y < SEA_LEVEL &&!(y <= givenMap[x + (z * CHUNK_SIZE)]))),
                            y <= givenMap[x + (z * CHUNK_SIZE)] || y < SEA_LEVEL,
                            parentMap.getTextureRow());
//                    if (y < SEA_LEVEL && !entities[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))].isSolid) {
//                        entities[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))] = new Cube(new Vector3f(x, y, z), 3, true,parentMap.getTextureRow());
//                    }
                }
            }
        }

        int offset = position.x() >= 0 ? 5 : 0;
        this.modelMatrix = createModelMatrix(
                rotationMatrix(0.0f, (byte) 1),
                rotationMatrix(0.0f, (byte) 2),
                rotationMatrix(0.0f, (byte) 3),
                new Matrix4f().identity().translation((position.x() * CHUNK_SIZE) + offset, position.y(),
                        position.z() * CHUNK_SIZE),
                new Matrix4f().identity().scale(1.0f));

    }

    public void initializeBuffers() {
        FloatBuffer vertexDataBuffer = FloatBuffer
                .allocate((CHUNK_CUBE + (CHUNK_SQR * 4)) * 24);
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            new Thread((new Runnable() {
                int y;
                @Override
                public void run() {
                    for (int z = 0; z < CHUNK_SIZE; z++) {
                        for (int x = 0; x < CHUNK_SIZE; x++) {
                            Cube self = entities[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))];
                            if (self == null) {
                                continue;
                            }
                            Cube left = getNeighbour(Direction.LEFT, x, y, z);
                            Cube bottom = getNeighbour(Direction.BOTTOM, x, y, z);
                            Cube back = getNeighbour(Direction.BACK, x, y, z);
                            Cube right = getNeighbour(Direction.RIGHT, x, y, z);
                            Cube front = getNeighbour(Direction.FRONT, x, y, z);
                            Cube top = getNeighbour(Direction.TOP, x, y, z);

                            Quad.processQuad(vertexDataBuffer, self, left, right, top, bottom, back, front);
                        }
                    }
                }
                public Runnable pass(int y){
                    this.y = y;
                    return this;
                }
            }).pass(y)).run();
        }
        IntBuffer indicesBuffer = IntBuffer.allocate((vertexDataBuffer.capacity() / 28) * 3);
        int index = 0;
        for (int i = 0; i < indicesBuffer.capacity(); i += 6) {
            indicesBuffer.put(index).put(index + 1).put(index + 2).put(index + 1).put(index + 2).put(index + 3);
            index += 4;
        }
        mesh = new Mesh(gl, vertexDataBuffer.array(), indicesBuffer.array());
    }

    private Cube getNeighbour(Direction direction, int x, int y, int z) {
        return switch (direction) {
            case TOP -> y < CHUNK_HEIGHT - 1
                    ? entities[x + (z * CHUNK_SIZE) + (y + 1) * (CHUNK_SQR)]
                    : new Cube(new Vector3f(x, y + 1, z), null, false);
            case BOTTOM -> y > 0
                    ? entities[x + (z * CHUNK_SIZE) + (y - 1) * (CHUNK_SQR)]
                    : new Cube(new Vector3f(x, y - 1, z), null, false);
            case LEFT -> x > 0
                    ? entities[(x - 1) + (z * CHUNK_SIZE) + y * (CHUNK_SQR)]
                    : position.x() > 0
                    ? parentMap.getChunk(position.x() - 1, position.y(), position.z())
                    .getCubeData((CHUNK_SIZE - 1) + x, y, z)
                    : new Cube(new Vector3f(x - 1, y, z), null, false);
            case RIGHT -> x < CHUNK_SIZE - 1
                    ? entities[(x + 1) + (z * CHUNK_SIZE) + y * (CHUNK_SQR)]
                    : position.x() < parentMap.getSize() - 1
                    ? parentMap.getChunk(position.x() + 1, position.y(), position.x())
                    .getCubeData((x + 1) % CHUNK_SIZE, y, z)
                    : new Cube(new Vector3f(x + 1, y, z), null, false);
            case FRONT -> z < CHUNK_SIZE - 1
                    ? entities[x + ((z + 1) * CHUNK_SIZE) + y * (CHUNK_SQR)]
                    : position.z() < parentMap.getSize() - 1
                    ? parentMap.getChunk(position.x(), position.y(), position.z() + 1)
                    .getCubeData(x, y, (z + 1) % CHUNK_SIZE)
                    : new Cube(new Vector3f(x, y, z + 1), null, false);
            case BACK -> z > 0 ? entities[x + ((z - 1) * CHUNK_SIZE) + y * (CHUNK_SQR)]
                    : position.z() > 0
                    ? parentMap.getChunk(position.x(), position.y(), position.z() - 1)
                    .getCubeData(x, y, (CHUNK_SIZE - 1) + z)
                    : new Cube(new Vector3f(x, y, z - 1), null, false);
        };

    }

    private static Matrix4f createModelMatrix(Matrix4f rotationX, Matrix4f rotationY, Matrix4f rotationZ,
                                              Matrix4f translation, Matrix4f scale) {
        return new Matrix4f().identity().mul(rotationX).mul(rotationY).mul(rotationZ).mul(translation).mul(scale);
    }

    // https://github.com/OneLoneCoder/Javidx9/blob/master/ConsoleGameEngine/BiggerProjects/Engine3D/OneLoneCoder_olcEngine3D_Part3.cpp
    public static Matrix4f rotationMatrix(float angle, byte mode) {
        float angleRad = (float) Math.toRadians(angle);
        return switch (mode) {
            case 1 -> new Matrix4f()
                    .m00(1.0f)
                    .m11(cos(angleRad))
                    .m12(-sin(angleRad))
                    .m21(sin(angleRad))
                    .m22(cos(angleRad))
                    .m33(1.0f);
            case 2 -> new Matrix4f()
                    .m00(cos(angleRad))
                    .m02(sin(angleRad))
                    .m20(-sin(angleRad))
                    .m11(1.0f)
                    .m22(cos(angleRad))
                    .m33(1.0f);
            case 3 -> new Matrix4f()
                    .m00(cos(angleRad))
                    .m01(-sin(angleRad))
                    .m10(sin(angleRad))
                    .m11(cos(angleRad))
                    .m22(1.0f)
                    .m33(1.0f);
            default -> null;
        };
    }

    public static Integer getMaterial(int y,boolean isWater) {
        if (isWater){
            return 3;
        }else if (y <= SEA_LEVEL + 2) {
            return 2;
        } else if (y >= MOUNTAIN_LEVEL) {
            return 4;
        } else {
            if(new Random().nextDouble()< 0.01){
                return 0;
            }else{
                return 1;
            }

        }

    }

    public Cube getCubeData(int x, int y, int z) {
        return entities[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)];
    }

    public boolean isChunk(int x, int y, int z) {
        return position.equals(x, y, z);
    }

    @Override
    public String toString() {
        return position.toString();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public record Cube(Vector3f position, Integer type, boolean isSolid, Vector2f uvOffset) {
        public Cube(Vector3f position, Integer type, boolean isSolid) {
            this(position, type, isSolid, null);
        }
        public Cube(Vector3f position, Integer type, boolean isSolid, float textureRow) {
            this(position, type, isSolid, getUVOffset(type, textureRow));
        }

        //https://www.youtube.com/watch?v=6T182r4F6J8
        private static Vector2f getUVOffset(Integer type, float textureRow) {
            if (type == null) {
                return new Vector2f(0.0f);
            }
            float xOffset =  (type % textureRow) / textureRow;
            float yOffset = (float) ((Math.floor(type / textureRow)) / textureRow);
            return new Vector2f(xOffset, yOffset);
        }
    }
}
