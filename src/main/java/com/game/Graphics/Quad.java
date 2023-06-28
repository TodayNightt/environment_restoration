package com.game.Graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static com.game.Graphics.Chunk.*;

//https://github.com/TanTanDev/first_voxel_engine/blob/main/src/voxel_tools/mesh_builder.rs
public enum Quad {
    TOP, BOTTOM, FRONT, BACK, LEFT, RIGHT;

    private static final float HALF_SIZE = 0.5f;


    public static float[] getMeshFace(Quad faces, Vector3f position, Vector2f uvOffset) {
        return switch (faces) {
            case TOP -> new float[]{
                    //Vertex                                                                //UV        //UV offsets
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case BOTTOM -> new float[]{
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case BACK -> new float[]{
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case FRONT -> new float[]{
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
            case LEFT -> new float[]{
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x - HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,

            };
            case RIGHT -> new float[]{
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z - HALF_SIZE, 0.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y + HALF_SIZE, position.z + HALF_SIZE, 1.0f, 1.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z - HALF_SIZE, 0.0f, 0.0f, uvOffset.x, uvOffset.y,
                    position.x + HALF_SIZE, position.y - HALF_SIZE, position.z + HALF_SIZE, 1.0f, 0.0f, uvOffset.x, uvOffset.y,
            };
        };
    }

    public static void processQuad(FloatBuffer vertexDataBuffer, Cube self, Cube left, Cube right, Cube top,
                                   Cube bottom, Cube back, Cube front) {
        Vector3f position = self.position();
        if (self.isSolid()) {
            if (!left.isSolid()) {
                vertexDataBuffer.put(getMeshFace(LEFT, position, self.uvOffset()));
            }
            if (!right.isSolid()) {
                vertexDataBuffer.put(getMeshFace(RIGHT, position, self.uvOffset()));
            }
            if (!top.isSolid()) {
                vertexDataBuffer.put(getMeshFace(TOP, position, self.uvOffset()));
            }
            if (!bottom.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BOTTOM, position, self.uvOffset()));
            }
            if (!back.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BACK, position, self.uvOffset()));
            }
            if (!front.isSolid()) {
                vertexDataBuffer.put(getMeshFace(FRONT, position, self.uvOffset()));
            }
        } else {
            if (left.isSolid()) {
                vertexDataBuffer.put(getMeshFace(LEFT, position, left.uvOffset()));
            }
            if (bottom.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BOTTOM, position, bottom.uvOffset()));
            }
            if (back.isSolid()) {
                vertexDataBuffer.put(getMeshFace(BACK, position, back.uvOffset()));
            }

        }
    }

    public static IntBuffer newProcessQuad(int[] data){
        IntBuffer vertexData = IntBuffer.allocate(data.length);
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    int block = data[x + (z * CHUNK_SIZE) + (y * CHUNK_SQR)];
                    if ( block== 0 || block == 1) continue;

                    if(block >> 7 == 1){
                        vertexData.put(newGetFace(LEFT,x,y,z));
                    }
                    if(((block>> 6) & 1) == 1){
                        vertexData.put(newGetFace(RIGHT,x,y,z));
                        int position = vertexData.position();
                        getPosition("Right",vertexData.get(position-4),vertexData.get(position-3), vertexData.get(position-2), vertexData.get(position-1) );
                    }
                    if(((block>>5)&1)== 1){
                        vertexData.put(newGetFace(FRONT,x,y,z));
                        int position = vertexData.position();
                        getPosition("Front",vertexData.get(position-4),vertexData.get(position-3), vertexData.get(position-2), vertexData.get(position-1) );

                    }
                    if(((block>>4 )& 1) == 1){
                        vertexData.put(newGetFace(BACK,x,y,z));
                    }
                    if(((block>> 3)& 1)== 1){
                        vertexData.put(newGetFace(TOP,x,y,z));
                    }
                    if(((block>>2)&1)== 1){
                        vertexData.put(newGetFace(BOTTOM,x,y,z));
                    }
                }
            }
        }


        return vertexData.flip().slice(0, vertexData.limit());
    }

    private static int[] newGetFace(Quad direction, int x, int y,int z){
        int[] vertexData = new int[4];
        /* a1    a2
           _______
           |     |  //Front
           |     |
           -------
           a3   a4

           a5   a6
           _______
           |     | //Back
           |     |
           -------
           a7   a8
        * */

        final int a1 = x << 11 | (z+1) << 6 | (y+1);
        final int a2 = (x + 1) << 11 | (z+1) << 6 | (y+1);
        final int a3 = x << 11 | (z + 1) << 6 | y;
        final int a4 = (x+1) << 11 | (z+1) << 6 | y;
        final int a5 = x << 11 | z << 6 | (y+1);
        final int a6 = (x + 1) << 11 | z << 6 | (y+1);
        final int a7 = x << 11 | z  << 6 | y;
        final int a8 = (x+1) << 11 | z << 6 | y;

        switch (direction){
            case LEFT -> {
                vertexData[0] = a5;
                vertexData[1] = a1;
                vertexData[2] = a7;
                vertexData[3] = a3;
                return vertexData;
            }
            case RIGHT -> {
                vertexData[0] =a2;
                vertexData[1] =a6;
                vertexData[2] = a4;
                vertexData[3] = a8;
                return vertexData;
            }
            case TOP -> {
                vertexData[0] =a5;
                vertexData[1] =a6;
                vertexData[2] = a1;
                vertexData[3] = a2;
                return vertexData;
            }
            case BOTTOM -> {
                vertexData[0] = a7;
                vertexData[1] = a8;
                vertexData[2] = a3;
                vertexData[3] = a4;
                return vertexData;
            }
            case FRONT -> {
                vertexData[0] = a1;
                vertexData[1] = a2;
                vertexData[2] = a3;
                vertexData[3] = a4;
                return vertexData;
            }
            case BACK -> {
                vertexData[0] = a5;
                vertexData[1] = a6;
                vertexData[2] = a7;
                vertexData[3] = a8;
                return vertexData;
            }
        }
        return  vertexData;
    }

    private static void getPosition(String dir,int a1 , int a2 ,int a3 , int a4){
        System.out.println(Integer.toBinaryString(a1));
        System.out.println(String.format("%s a1,a2 :(%d,%d,%d),(%d,%d,%d) ",dir,a1>>11,a1 & 0x3F,a1>>6 & 0x1F,a2>>11,a2 & 0x3F,a2>>6 & 0x1));
        System.out.println(String.format("   a3,a4 :(%d,%d,%d),(%d,%d,%d) ",a3>>11,a3 & 0x3F,a3>>6 & 0x1F,a4>>11,a4 & 0x3F,a4>>6 & 0x1));
    }
}

