package com.game.Graphics;

import com.game.Graphics.Mesh.TerrainMesh;
import com.game.Terrain.TerrainMap;
import org.joml.Matrix4f;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.nio.IntBuffer;
import java.util.Arrays;

import static com.game.Graphics.MatrixCalc.createModelMatrix;
import static com.game.Graphics.MatrixCalc.rotationMatrix;
import static com.game.Graphics.Quad.*;

public class Chunk{
    public static final int CHUNK_SIZE = 15;
    public static final int CHUNK_HEIGHT = 40;
    public static final int SEA_LEVEL = 10;
    private static final int MOUNTAIN_LEVEL = CHUNK_HEIGHT - 6;
    public static final int CHUNK_SQR = CHUNK_SIZE * CHUNK_SIZE;
    private static final int CHUNK_CUBE = CHUNK_SQR * CHUNK_SIZE;
    private final int[]blocks;
    private final Vector3ic position;
    private TerrainMesh mesh;
    private final Matrix4f modelMatrix;
    private final TerrainMap parentMap;

    public Chunk(int xo, int yo, int zo,int[] givenMap, TerrainMap parentMap){
        this.position = new Vector3i(xo,yo,zo);
        this.parentMap = parentMap;
        this.blocks = new int[CHUNK_SQR * CHUNK_HEIGHT];
        Arrays.fill(this.blocks,0);

        // https://stackoverflow.com/questions/38204579/flatten-3d-array-to-1d-array
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z <CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    if(y > givenMap[x + (z * CHUNK_SIZE)] && y > SEA_LEVEL) continue;
                    int blockData =505;
                    if(y <= SEA_LEVEL && !(y <= givenMap[x + (z * CHUNK_SIZE)])){
                        blockData |= 0x4; //[1]00
                    }
                    blocks[x + (z * CHUNK_SIZE) + (y * (CHUNK_SQR))] = blockData ;
                }
            }
        }
        this.modelMatrix = createModelMatrix(
                rotationMatrix(0.0f, (byte) 1),
                rotationMatrix(0.0f, (byte) 2),
                rotationMatrix(0.0f, (byte) 3) ,
                new Matrix4f().identity().translation((position.x() * CHUNK_SIZE), position.y(),
                        position.z() * CHUNK_SIZE),
                new Matrix4f().identity().scale(1.0f));
    }


    public void initializeBuffers() {
        evaluateNeighbour();
        IntBuffer vertexBuffer = processQuad(blocks);
        IntBuffer indicesBuffer = IntBuffer.allocate((vertexBuffer.capacity()/4)*6);
        int index = 0;
        for (int i = 0; i < indicesBuffer.capacity(); i += 6) {
            indicesBuffer.put(index).put(index + 1).put(index + 2).put(index + 1).put(index + 3).put(index + 2);
            index += 4;
        }
        mesh = new TerrainMesh(vertexBuffer,indicesBuffer);
    }


    /*
    TODO : Get the world coordinate system working
    TODO : Show the position on map system
    TODO : Let player cannot go through the floor
    TODO : Make holes algorithm
    */
    public void evaluateNeighbour(){
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    if(blocks[x+(z*CHUNK_SIZE)+ (y*CHUNK_SQR)] == 0) continue;
                    int faces = blocks[x+(z*CHUNK_SIZE)+ (y*CHUNK_SQR)];
                    faces |= getMaterial(y,faces >> 2 & 1);
                    faces &= isNeighbourActive(LEFT,x - 1,y,z)? 0xFF : faces; //0111111
                    faces &= isNeighbourActive(RIGHT,x + 1,y,z) ? 0x17F: faces; //1011111
                    faces &= isNeighbourActive(FRONT,x,y,z + 1)? 0x1BF : faces; //1101111
                    faces &= isNeighbourActive(BACK,x,y,z - 1)? 0x1DF :faces; //1110111
                    faces &= isNeighbourActive(TOP,x,y + 1,z)? 0x1EF : faces; //1111011
                    faces &= isNeighbourActive(BOTTOM,x,y - 1,z)? 0x1F7: faces; //1111101
                    blocks[x+(z*CHUNK_SIZE)+ (y*CHUNK_SQR)] = faces;
                }
            }
        }


    }

    private boolean isNeighbourActive(Quad direction, int x, int y, int z) {
        return  switch (direction) {
            case TOP:
                yield y < CHUNK_HEIGHT  && blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;

            case BOTTOM:
                yield y >= 0 && blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0;
            case LEFT:
               yield x >= 0
                        ? blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0
                        : position.x() > 0 && parentMap.getChunk(position.x() - 1, position.y(), position.z()).getCubeData(CHUNK_SIZE - 1, y, z) != 0;
            case RIGHT:
                yield x < CHUNK_SIZE
                        ? blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0:
                        position.x() < parentMap.getSize() - 1 && parentMap.getChunk(position.x() + 1, position.y(), position.z())
                                .getCubeData(0 , y, z) != 0;
            case FRONT:
                yield z < CHUNK_SIZE
                        ? blocks[x + (z  * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0
                        : position.z() < parentMap.getSize() - 1 && parentMap.getChunk(position.x(), position.y(), position.z() + 1)
                            .getCubeData(x, y, 0) != 0;
            case BACK:
                yield z >= 0
                        ? blocks[x + (z * CHUNK_SIZE) + y * (CHUNK_SQR)] != 0
                        : position.z() > 0 && parentMap.getChunk(position.x(), position.y(), position.z() - 1)
                    .getCubeData(x, y, (CHUNK_SIZE - 1)) !=0;
        };

    }

    public static int getMaterial(int y,int isWater) {
        if (isWater == 1) {
            return 0x6;
        }else if (y <= SEA_LEVEL) {
            return 0;
        } else if (y < SEA_LEVEL + 2) {
            return 0x4;
        } else {
            return 0x2;
        }

    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
    public TerrainMesh getMesh() {
        return mesh;
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