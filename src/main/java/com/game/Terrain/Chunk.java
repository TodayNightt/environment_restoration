package com.game.Terrain;

import com.game.Graphics.Faces;
import com.game.Graphics.Mesh.MeshFactory;
import com.game.templates.Mesh;
import org.joml.Matrix4f;
import org.joml.Vector3i;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Optional;

import static com.game.Graphics.Faces.*;
import static com.game.Utils.MatrixCalc.createModelMatrix;
import static com.game.Utils.MatrixCalc.rotationMatrix;
import static com.game.Utils.TerrainContraints.*;
import static com.game.templates.Mesh.MeshType.TERRAIN;

public class Chunk extends Thread {

    private int[] blocks;
    private Vector3i position;
    private Optional<Mesh> mesh;
    private Matrix4f modelMatrix;
    private TerrainMap parentMap;
    private IntBuffer vertexBuffer, indicesBuffer;

    @Override
    public void run() {
        // System.out.println("Starting thread for " + this);
        initializeBuffers();
        // System.out.println("Ending thread for " + this);
    }

    public Chunk(int x, int y, int z, int[] heightMap, TerrainMap parent) {
        init(x, y, z, heightMap, parent);
    }

    public void init(int xx, int yy, int zz, int[] givenMap, TerrainMap parentMap) {
        this.position = new Vector3i(xx, yy, zz);
        this.parentMap = parentMap;
        this.blocks = new int[CHUNK_SQR * CHUNK_HEIGHT + (CHUNK_SQR)];
        Arrays.fill(this.blocks, 0);

        // https://stackoverflow.com/questions/38204579/flatten-3d-array-to-1d-array
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    if (y > givenMap[x + (z * CHUNK_SIZE)] && y > SEA_LEVEL)
                        continue;
                    int blockData = 505;
                    if (y <= SEA_LEVEL && !(y <= givenMap[x + (z * CHUNK_SIZE)])) {
                        blockData |= 0x4; // [1]00
                    }
                    blocks[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))] = blockData;
                }
            }
        }

        this.mesh = Optional.empty();

        this.modelMatrix = createModelMatrix(
                rotationMatrix(0.0f, (byte) 1),
                rotationMatrix(0.0f, (byte) 2),
                rotationMatrix(0.0f, (byte) 3),
                new Matrix4f().identity().translation((position.x() * CHUNK_SIZE), position.y(),
                        position.z() * CHUNK_SIZE),
                new Matrix4f().identity().scale(1.0f));
    }

    public void initializeBuffers() {
        evaluateNeighbour();
        vertexBuffer = Faces.processQuad(blocks);
        indicesBuffer = IntBuffer.allocate((vertexBuffer.capacity() / 4) * 6);
        int index = 0;
        for (int i = 0; i < indicesBuffer.capacity(); i += 6) {
            indicesBuffer.put(index).put(index + 1).put(index + 2).put(index + 1).put(index + 3).put(index + 2);
            index += 4;
        }

    }

    public void generateMesh() {
        mesh = Optional.of(MeshFactory.createMesh(TERRAIN, vertexBuffer, indicesBuffer));
    }

    /*
     * TODO : Get the world coordinate system working
     * TODO : Show the position on map system
     * TODO : Let player cannot go through the floor
     * TODO : Make holes algorithm
     * TODO : Block lighting
     * TODO : Which even zero or non-zero case are smaller, use that to calculate
     * the meshes
     */
    public void evaluateNeighbour() {
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    if (blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SQR)] == 0)
                        continue;
                    int faces = blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SQR)];
                    faces |= getMaterial(y, faces >> 2 & 1);
                    faces &= isNeighbourActive(LEFT, x - 1, y, z) ? 0xFF : faces; // 0111111
                    faces &= isNeighbourActive(RIGHT, x + 1, y, z) ? 0x17F : faces; // 1011111
                    faces &= isNeighbourActive(FRONT, x, y, z + 1) ? 0x1BF : faces; // 1101111
                    faces &= isNeighbourActive(BACK, x, y, z - 1) ? 0x1DF : faces; // 1110111
                    faces &= isNeighbourActive(TOP, x, y + 1, z) ? 0x1EF : faces; // 1111011
                    faces &= isNeighbourActive(BOTTOM, x, y - 1, z) ? 0x1F7 : faces; // 1111101
                    blocks[x + (z * CHUNK_SIZE) + (y * CHUNK_SQR)] = faces;
                }
            }
        }

    }

    private boolean isNeighbourActive(Faces direction, int x, int y, int z) {
        Optional<Chunk> neighbour;
        switch (direction) {
            case TOP:
                return y <= CHUNK_HEIGHT && blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;
            case BOTTOM:
                return y >= 0 && blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;
            case LEFT:
                if (x >= 0) {
                    return blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;
                }

                neighbour = parentMap.getChunk(position.x() - 1, position.y(), position.z());

                return neighbour.filter(chunk -> chunk.getCubeData(CHUNK_SIZE - 1, y, z) != 0).isPresent();

            case RIGHT:
                if (x < CHUNK_SIZE - 1) {
                    return blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;
                }

                neighbour = parentMap.getChunk(position.x() + 1, position.y(), position.z());

                return neighbour.filter(chunk -> chunk.getCubeData(0, y, z) != 0).isPresent();
            case FRONT:
                if (z < CHUNK_SIZE - 1) {
                    return blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;
                }

                neighbour = parentMap.getChunk(position.x(), position.y(), position.z() + 1);

                return neighbour.filter(chunk -> chunk.getCubeData(x, y, 0) != 0).isPresent();

            case BACK:
                if (z >= 0) {
                    return blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;
                }

                neighbour = parentMap.getChunk(position.x(), position.y(), position.z() - 1);

                return neighbour.filter(chunk -> chunk.getCubeData(x, y, (CHUNK_SIZE - 1)) != 0).isPresent();

            default:
                return false;
        }

    }

    public static int getMaterial(int y, int isWater) {
        if (isWater == 1) {
            return 0x6;
        } else if (y <= SEA_LEVEL) {
            return 0;
        } else if (y < SEA_LEVEL + 2) {
            return 0x4;
        } else {
            return 0x2;
        }

    }

    public boolean gotMesh() {
        return mesh.isPresent();
    }

    public boolean isChunk(int x, int y, int z) {
        return position.equals(x, y, z);
    }

    public void cleanup() {
        mesh.ifPresent(Mesh::cleanup);
    }

    // Getter
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public Optional<Mesh> getMesh() {
        return mesh;
    }

    public int getCubeData(int x, int y, int z) {
        return blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)];
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + position.x() + " " + position.y() + " " + position.z();
    }
}