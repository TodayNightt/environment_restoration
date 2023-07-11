package com.game.Graphics;

import java.nio.IntBuffer;

import static com.game.Graphics.Chunk.CHUNK_SQR;
import static com.game.Graphics.Chunk.CHUNK_HEIGHT;
import static com.game.Graphics.Chunk.CHUNK_SIZE;

//https://github.com/TanTanDev/first_voxel_engine/blob/main/src/voxel_tools/mesh_builder.rs
public enum Quad {
    TOP, BOTTOM, FRONT, BACK, LEFT, RIGHT;


    public static IntBuffer processQuad(int[] data){
        IntBuffer vertexData = IntBuffer.allocate((data.length * 6) * 4);
        for (int y = 0; y < CHUNK_HEIGHT; y++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    int block = data[x + (z * CHUNK_SIZE) + (y * CHUNK_SQR)];
                    if ( block== 0 || block == 1) continue;
                    int textureID = (block >> 1) & 0x3;
                    if(((block >> 8) & 1 )== 1){
                        vertexData.put(getMeshFace(LEFT,x,y,z,textureID));
                    }
                    if(((block>> 7) & 1) == 1){
                        vertexData.put(getMeshFace(RIGHT,x,y,z,textureID));
                    }
                    if(((block>>6)&1)== 1){
                        vertexData.put(getMeshFace(FRONT,x,y,z,textureID));
                    }
                    if(((block>>5 )& 1) == 1){
                        vertexData.put(getMeshFace(BACK,x,y,z,textureID));
                    }
                    if(((block>> 4)& 1)== 1){
                        vertexData.put(getMeshFace(TOP,x,y,z,textureID));
                    }
                    if(((block>>3)&1)== 1){
                        vertexData.put(getMeshFace(BOTTOM,x,y,z,textureID));
                    }
                }
            }
        }
        return vertexData.flip().slice(0, vertexData.limit());
    }


    //https://learnopengl.com/code_viewer.php?code=advanced/faceculling-exercise1
    private static int[] getMeshFace(Quad direction, int x, int y, int z, int textureID){
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
        //X(5)Z(5)Y(6)UV(2)ID(2)
        final int a1 = x << 15 | (z+1) << 10 | (y+1) << 4;
        final int a2 = (x + 1) << 15 | (z+1) << 10 | (y+1) << 4;
        final int a3 = x << 15 | (z + 1) << 10 | y << 4;
        final int a4 = (x+1) << 15 | (z+1) << 10 | y << 4;
        final int a5 = x << 15 | z << 10 | (y+1) << 4;
        final int a6 = (x + 1) << 15 | z << 10 | (y+1) << 4;
        final int a7 = x << 15 | z  << 10 | y << 4;
        final int a8 = (x+1) << 15 | z << 10 | y << 4;

         return switch (direction) {
             case LEFT -> new int[]{
                     a5 | getUVs(0, textureID),
                     a1 | getUVs(1, textureID),
                     a7 | getUVs(2, textureID),
                     a3 | getUVs(3, textureID)
             };
             case RIGHT -> new int[]{
                     a2 | getUVs(0, textureID),
                     a6 | getUVs(1, textureID),
                     a4 | getUVs(2, textureID),
                     a8 | getUVs(3, textureID)
             };
             case TOP -> new int[]{
                     a5 | getUVs(0, textureID),
                     a6 | getUVs(1, textureID),
                     a1 | getUVs(2, textureID),
                     a2 | getUVs(3, textureID)
             };
             case BOTTOM -> new int[]{
                     a4 | getUVs(0, textureID),
                     a8 | getUVs(1, textureID),
                     a3 | getUVs(2, textureID),
                     a7 | getUVs(3, textureID)
             };
             case FRONT -> new int[]{
                     a1 | getUVs(0, textureID),
                     a2 | getUVs(1, textureID),
                     a3 | getUVs(2, textureID),
                     a4 | getUVs(3, textureID),
             };
             case BACK -> new int[]{
                     a8 | getUVs(0, textureID),
                     a6 | getUVs(1, textureID),
                     a7 | getUVs(2, textureID),
                     a5 | getUVs(3, textureID)

             };
         };
    }

    private static int getUVs(int place,int textureID){
        return place << 2 | textureID;
    }

    private static void getPosition(String dir,int a1 , int a2 ,int a3 , int a4){
        System.out.println(Integer.toBinaryString(a1));
        System.out.printf("%s a1,a2 :(%d,%d,%d),(%d,%d,%d) %n",dir,a1>>11,a1 & 0x3F,a1>>6 & 0x1F,a2>>11,a2 & 0x3F,a2>>6 & 0x1);
        System.out.printf("   a3,a4 :(%d,%d,%d),(%d,%d,%d) %n",a3>>11,a3 & 0x3F,a3>>6 & 0x1F,a4>>11,a4 & 0x3F,a4>>6 & 0x1);
    }
}

