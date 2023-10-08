package com.game.Graphics.Gui;

import com.game.Graphics.Mesh.MeshFactory;
import com.game.templates.GuiItem;
import com.game.templates.Mesh;


public class Minimap extends GuiItem {

    private final Mesh mesh;

    private Minimap(int posX,int posY,int size){
        mesh = MeshFactory.createQuadMesh(posX, posY, size);
    }
    public static Minimap create(int posX,int posY,int size){
        return new Minimap(posX, posY, size);
    }

    public Mesh getMesh() {
        return mesh;
    }
}
