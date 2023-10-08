package com.game;

import com.game.Graphics.Gui.Button;
import com.game.Graphics.Renderer;
import com.game.Graphics.Scene;
import com.game.Window.Window;


public class GameWindow extends Window {

    private Scene scene;
    private Renderer renderer;


    @Override
    public void initComponent() {
        scene = new Scene();
        scene.initializeTerrain("shaders/terrain.vert","shaders/terrain.frag",new String[]{"projectionMatrix", "viewMatrix", "modelMatrix", "tex", "fValue"});
        renderer = new Renderer();
        renderer.add(scene);
    }

    @Override
    public void render() {
        renderer.render();
    }

    @Override
    protected void disposeAll(){
        super.disposeAll();
        scene.cleanup();
    }

    @Override
    public void resized(long window, int width, int height){
        super.resized(window,width,height);
        scene.getCamera().setWindowSize(width,height);
    }

}
