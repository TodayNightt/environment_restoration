package com.game.Graphics;

import com.game.Graphics.Mesh.MapMesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MiniMap {
    private MapMesh mesh;

    public MiniMap(int posX,int posY,int size){
        int[] positions = new int[]{
                posX << 7 | posY << 2,
                (posX + size) << 7 | posY << 2 | 1,
                posX << 7 | (posY + size) << 2 | 2 ,
                (posX + size) << 7 | (posY + size) << 2 | 3
        };
        IntBuffer vertexBuffer = IntBuffer.wrap(positions);
        mesh = new MapMesh(vertexBuffer);
    }


    public MapMesh getMesh(){
        return mesh;
    }






}
