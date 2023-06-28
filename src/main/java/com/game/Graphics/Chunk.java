package com.game.Graphics;

import com.game.Graphics.Mesh.TerrainMesh;
import com.game.Terrain.TerrainMap;
import org.joml.*;

import java.lang.Math;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static com.game.Graphics.MatrixCalc.createModelMatrix;
import static com.game.Graphics.MatrixCalc.rotationMatrix;
import static com.game.Graphics.Quad.*;

public class Chunk {
    public static final int CHUNK_SIZE = 15;
    public static final int CHUNK_HEIGHT = 40;
    public static final int SEA_LEVEL = 10;
    public static final int MOUNTAIN_LEVEL = CHUNK_HEIGHT - 6;
    static final int CHUNK_SQR = CHUNK_SIZE * CHUNK_SIZE;
    private static final int CHUNK_CUBE = CHUNK_SQR * CHUNK_SIZE;
    private final Cube[] entities;
    private final Matrix4f modelMatrix;
    private final Vector3ic position;
    private final TerrainMap parentMap;
    private TerrainMesh mesh;

    public Chunk(int xo, int yo, int zo, int[] givenMap, TerrainMap parentMap) {
        entities = new Cube[CHUNK_SIZE * CHUNK_SIZE * CHUNK_HEIGHT];
        this.parentMap = parentMap;
        this.position = new Vector3i(xo, yo, zo);

        // https: // stackoverflow.com/questions/38204579/flatten-3d-array-to-1d-array
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    entities[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))] = new Cube(new Vector3f(x, y, z),
                            getMaterial(y, (y < SEA_LEVEL && !(y <= givenMap[x + (z * CHUNK_SIZE)]))),
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


    public static Integer getMaterial(int y, boolean isWater) {
        if (isWater) {
            return 3;
        } else if (y < SEA_LEVEL) {
            return 0;
        } else if (y <= SEA_LEVEL + 2) {
            return 2;
        } else if (y >= MOUNTAIN_LEVEL) {
            return 4;
        } else {
            return 1;

        }

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
                            Cube left = getNeighbour(LEFT, x, y, z);
                            Cube bottom = getNeighbour(BOTTOM, x, y, z);
                            Cube back = getNeighbour(BACK, x, y, z);
                            Cube right = getNeighbour(RIGHT, x, y, z);
                            Cube front = getNeighbour(FRONT, x, y, z);
                            Cube top = getNeighbour(TOP, x, y, z);

                            processQuad(vertexDataBuffer, self, left, right, top, bottom, back, front);
                        }
                    }
                }

                public Runnable pass(int y) {
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
        mesh = new TerrainMesh(vertexDataBuffer.array(), indicesBuffer.array());
    }

    private Cube getNeighbour(Quad direction, int x, int y, int z) {
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

    public TerrainMesh getMesh() {
        return mesh;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public void cleanup(){
        mesh.cleanup();
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
            float xOffset = (type % textureRow) / textureRow;
            float yOffset = (float) ((Math.floor(type / textureRow)) / textureRow);
            return new Vector2f(xOffset, yOffset);
        }
    }
}
