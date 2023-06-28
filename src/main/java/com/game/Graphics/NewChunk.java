package com.game.Graphics;

import com.game.Graphics.Mesh.NewTerrainMesh;
import com.game.Graphics.Mesh.TerrainMesh;
import com.game.Terrain.NewTerrainMap;
import org.joml.Matrix4f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.nio.IntBuffer;
import java.util.Arrays;

import static com.game.Graphics.Chunk.*;
import static com.game.Graphics.MatrixCalc.createModelMatrix;
import static com.game.Graphics.MatrixCalc.rotationMatrix;
import static com.game.Graphics.Quad.*;
import static com.game.Terrain.Generation.NoiseMap.GenerateMap;

public class NewChunk{
    private final int[]blocks;
    private final Vector3ic position;
    private NewTerrainMesh mesh;
    private int activeBlock = 0;
    private final Matrix4f modelMatrix;
    private final NewTerrainMap parentMap;

    public NewChunk(int xo, int yo, int zo,int[] givenMap, NewTerrainMap parentMap){
        this.position = new Vector3i(xo,yo,zo);
        this.parentMap = parentMap;
        this.blocks = new int[15*15*40];
        Arrays.fill(this.blocks,0);

        // https: // stackoverflow.com/questions/38204579/flatten-3d-array-to-1d-array
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z <CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    if(y > givenMap[x + (z * CHUNK_SIZE)])continue;
                    blocks[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))] = 127;
                    activeBlock++;
                }
            }
        }
        this.modelMatrix = createModelMatrix(
                rotationMatrix(0.0f, (byte) 1),
                rotationMatrix(0.0f, (byte) 2),
                rotationMatrix(0.0f, (byte) 3),
                new Matrix4f().identity().translation((position.x() * CHUNK_SIZE), position.y(),
                        position.z() * CHUNK_SIZE),
                new Matrix4f().identity().scale(1.0f));
    }
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
    public NewTerrainMesh getMesh() {
        return mesh;
    }

    public void initializeBuffers(){
        evaluateNeighbour();
        IntBuffer vertexBuffer = newProcessQuad(blocks);
        IntBuffer indicesBuffer = IntBuffer.allocate((vertexBuffer.capacity()/4)*6);
        int index = 0;
        for (int i = 0; i < indicesBuffer.capacity(); i += 6) {
            indicesBuffer.put(index).put(index + 1).put(index + 2).put(index + 1).put(index + 2).put(index + 3);
            index += 4;
        }
        mesh = new NewTerrainMesh(vertexBuffer,indicesBuffer);
    }

    public void evaluateNeighbour(){
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z <CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    if(blocks[x+(z*CHUNK_SIZE)+ (y*CHUNK_SQR)] == 0) continue;
                    int faces = blocks[x+(z*CHUNK_SIZE)+ (y*CHUNK_SQR)];
                    faces &= isNeighbourActive(LEFT,x,y,z)? 0x3F : faces;
                    faces &= isNeighbourActive(RIGHT,x,y,z) ? 0x5F: faces;
                    faces &= isNeighbourActive(FRONT,x,y,z)? 0x6F : faces;
                    faces &= isNeighbourActive(BACK,x,y,z)? 0x77 :faces;
                    faces &= isNeighbourActive(TOP,x,y,z)? 0x7B : faces;
                    faces &= isNeighbourActive(BOTTOM,x,y,z)? 0x7D: faces;
                    blocks[x+(z*CHUNK_SIZE)+ (y*CHUNK_SQR)] = faces;
                }
            }
        }
    }

    private boolean isNeighbourActive(Quad direction, int x, int y, int z) {
        return switch (direction) {
            case TOP:
                yield y <= CHUNK_HEIGHT - 1 && blocks[x + (z * CHUNK_SIZE) + (y + 1) * (CHUNK_SQR)] != 0;
            case BOTTOM:
                yield y > 0 && blocks[x + (z * CHUNK_SIZE) + (y - 1) * (CHUNK_SQR)] != 0;
            case LEFT:
                yield x > 0
                        ? blocks[(x - 1) + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0
                        : position.x() > 0 && parentMap.getChunk(position.x() - 1, position.y(), position.z())
                        .getCubeData((CHUNK_SIZE - 1) + x, y, z) != 0;
            case RIGHT:
                yield x < CHUNK_SIZE -1
                        ? blocks[(x + 1) + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0:
                        position.x() < parentMap.getSize() - 1 && parentMap.getChunk(position.x() + 1, position.y(), position.x())
                                .getCubeData((x + 1) % CHUNK_SIZE, y, z) != 0;
            case FRONT:
                yield z < CHUNK_SIZE - 1
                        ? blocks[x + ((z + 1) * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0
                        : position.z() < parentMap.getSize() - 1 && parentMap.getChunk(position.x(), position.y(), position.z() + 1)
                            .getCubeData(x, y, (z + 1) % CHUNK_SIZE) != 0;
            case BACK:
                yield z > 0
                        ? blocks[x + ((z - 1) * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0
                        : position.z() > 0 && parentMap.getChunk(position.x(), position.y(), position.z() - 1)
                    .getCubeData(x, y, (CHUNK_SIZE - 1) + z)!=0;
        };

    }

    public int getCubeData(int x, int y, int z) {
        return blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)];
    }

    public boolean isChunk(int x, int y, int z) {
        return position.equals(x, y, z);
    }

    public void cleanup(){
        mesh.cleanup();
    }

}