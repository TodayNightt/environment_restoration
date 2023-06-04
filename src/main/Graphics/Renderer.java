package Graphics;

import Camera.Camera;
import Terrain.TerrainMap;
import com.jogamp.opengl.GL3;

import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL3.GL_TRIANGLES;
import static com.jogamp.opengl.GL3.GL_UNSIGNED_INT;

public class Renderer {

    protected GL3 gl;

    private final ShaderProgram shaderP;

    private UniformsMap uniformsMap;

    public Renderer(GL3 gl) throws Exception {
        this.gl = gl;
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/vertex.vert", GL3.GL_VERTEX_SHADER));
        shaderDataList.add(new ShaderProgram.ShaderData("src/main/resources/shaders/fragment.frag", GL3.GL_FRAGMENT_SHADER));
        shaderP = new ShaderProgram(gl, shaderDataList);
        createUniforms();
    }

    public void cleanup() {
        shaderP.cleanup();
    }

    public void render(TerrainMap map, Camera cam,Textures texturesList, boolean wireFrame) {
        shaderP.bind();
        uniformsMap.setUniform("projectionMatrix", cam.getProjectionMatrix());
        uniformsMap.setUniform("viewMatrix", cam.getViewMatrix());
         uniformsMap.setUniform("tex", 0);

         gl.glActiveTexture(GL3.GL_TEXTURE0);
         texturesList.bind("block_atlas");
         uniformsMap.setUniform("textureRow",map.getTextureRow());
        map.getMap().forEach(chunk -> {
            uniformsMap.setUniform("modelMatrix", chunk.getModelMatrix());
            gl.glBindVertexArray(chunk.getMesh().getVao().get(0));
            gl.glDrawElements(GL_TRIANGLES, chunk.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        });

        gl.glPolygonMode(GL3.GL_FRONT_AND_BACK, wireFrame ? GL3.GL_LINE : GL3.GL_FILL);
        // });
        gl.glBindVertexArray(0);
        shaderP.unbind();

    }

    public void createUniforms() {
        uniformsMap = new UniformsMap(shaderP.getProgramId(), gl);
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("textureRow");
         uniformsMap.createUniform("tex");
    }

}
