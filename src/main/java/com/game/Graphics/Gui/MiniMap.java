package com.game.Graphics.Gui;

import com.game.Graphics.Mesh.QuadMesh;

import java.nio.IntBuffer;

public class MiniMap {
    private QuadMesh mesh;

    public MiniMap(int posX,int posY,int size){
        int[] positions = new int[]{
                posX << 7 | posY << 2,
                (posX + size) << 7 | posY << 2 | 1,
                posX << 7 | (posY + size) << 2 | 2 ,
                (posX + size) << 7 | (posY + size) << 2 | 3
        };
        IntBuffer vertexBuffer = IntBuffer.wrap(positions);
        mesh = new QuadMesh(vertexBuffer);
    }


    public QuadMesh getMesh(){
        return mesh;
    }






}
