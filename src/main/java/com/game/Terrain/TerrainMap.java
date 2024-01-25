package com.game.Terrain;

import com.game.Camera.Camera;
import com.game.Graphics.ShaderProgram;
import com.game.Graphics.TextureList;
import com.game.Graphics.UniformsMap;
import com.game.Utils.WorkerManager;
import com.game.templates.Mesh;
import com.game.templates.SceneItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.game.Graphics.Scene.createShaderProgram;
import static com.game.Graphics.Scene.createUniformMap;
import static com.game.Utils.TerrainContraints.CHUNK_SIZE;
import static com.game.Utils.TerrainContraints.MAP_SIZE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TerrainMap extends SceneItem {

    private final List<Chunk> chunkList;
    private final String textureName;
    private int[] heightMap;

    private ShaderProgram shaderProgram;
    private UniformsMap uniformsMap;

    public TerrainMap(String texture) {
        this.textureName = texture;
        this.chunkList = new ArrayList<>();
    }

    @Override
    public void init(String id, String vertShader, String fragShader, String[] uniformList) {
        this.shaderProgram = createShaderProgram(vertShader, fragShader);
        this.uniformsMap = createUniformMap(shaderProgram, uniformList);
    }

    public void refresh(int[] heightMap) {
        initChunks(heightMap);
    }

    protected void initChunks(int[] heightMap) {
        this.heightMap = heightMap;
        chunkList.clear();

        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                int[] newHeightMap = new int[CHUNK_SIZE * CHUNK_SIZE];
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        int offset = CHUNK_SIZE * MAP_SIZE;
                        newHeightMap[x + (z * CHUNK_SIZE)] = heightMap[(x + (j * CHUNK_SIZE)) + ((z *
                                offset) + (i * (offset * CHUNK_SIZE)))];
                    }
                }
                chunkList.add(new Chunk(j, 0, i, newHeightMap, this));
            }
        }
        WorkerManager.getInstance().pass(chunkList).start();
    }


    // https://stackoverflow.com/questions/22131437/return-objects-from-arraylist
    public Optional<Chunk> getChunk(int x, int y, int z) {
        return this.chunkList.stream().filter(chunk -> chunk.isChunk(x, y, z)).findFirst();
    }

    @Override
    public void render(Camera cam, boolean isWireFrame) {
        glPolygonMode(GL_FRONT_AND_BACK, isWireFrame ? GL_LINE : GL_FILL);
        shaderProgram.bind();
        uniformsMap.setUniform("projectionMatrix", cam.getProjectionMatrix());
        uniformsMap.setUniform("viewMatrix", cam.getViewMatrix());
        uniformsMap.setUniform("tex", 0);
        glActiveTexture(GL_TEXTURE0);
        TextureList.getInstance().bind(textureName);
        uniformsMap.setUniform("fValue", new float[]{
                TerrainMap.getTextureRow()
        });
        chunkList.stream().filter(chunk -> chunk.getMesh().isPresent()).forEach(chunk -> {
            uniformsMap.setUniform("modelMatrix", chunk.getModelMatrix());
            Mesh mesh = chunk.getMesh().get();
            glBindVertexArray(mesh.getVao());
            glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        glBindVertexArray(0);
        shaderProgram.unbind();
    }

    @Override
    public void cleanup() {
        chunkList.forEach(Chunk::cleanup);
    }

    public int getSize() {
        return MAP_SIZE;
    }

    public List<Chunk> getMap() {
        return chunkList;
    }

    public static float getTextureRow() {
        return 3.0f;
    }

    public String getTextureName() {
        return textureName;
    }

    public int[] getHeightMap() {
        return heightMap;
    }
}
