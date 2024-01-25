package com.game.templates;

import com.game.Camera.Camera;

public abstract class SceneItem {

    public abstract void render(Camera cam,boolean isWireFrame);

    public abstract void init(String id,String vertShader, String fragShader,String[] uniformList);

    public abstract void cleanup();

}
