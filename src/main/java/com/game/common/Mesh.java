package com.game.common;


import java.nio.IntBuffer;

public interface Mesh {

    void bindBuffer(IntBuffer positions, IntBuffer indices);

    int getNumVertices();

    int getVao();

    String toString();

    void cleanup();

    int[] getSize();

}
