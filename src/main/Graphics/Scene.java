package Graphics;

import GUI.UiInstance;
import Terrain.Generation.TextureGenerator;
import Terrain.TerrainMap;
import com.jogamp.opengl.GL3;

import java.io.IOException;

public class Scene {
    private GL3 gl;
    private Textures textureList;
    private TerrainMap terrain;
    private UiInstance uiInstance;

    public Scene(GL3 gl) throws Exception {
        this.gl = gl;
        init();
    }
    private void init() throws Exception {
        this.textureList = new Textures(gl);
        initializeTexture();
        initializeTerrainGen();
    }
    private void initializeTexture() throws IOException {
        TextureGenerator.GenerateAtlas();
        textureList.createTexture("block_atlas", "src/main/resources/textures/block_atlas_texture.png", true);
    }

    public Textures getTextureList(){
        return  textureList;
    }

    public TerrainMap getTerrain(){
        return terrain;
    }

    private void initializeTerrainGen() throws Exception {
        this.terrain = new TerrainMap(gl,"block_atlas");
    }
}
