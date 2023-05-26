package Graphics;

import static com.jogamp.opengl.GL3.GL_TRIANGLES;
import static com.jogamp.opengl.GL3.GL_UNSIGNED_INT;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;

import Camera.Camera;

public class Renderer {

    protected GL3 gl;

    private ShaderProgram shaderP;

    private UniformsMap uniformsMap;

    public Renderer(GL3 gl) throws Exception {
        this.gl = gl;
        List<ShaderProgram.ShaderData> shaderDataList = new ArrayList<>();
        shaderDataList.add(new ShaderProgram.ShaderData("resources/shaders/vertex.vert", GL3.GL_VERTEX_SHADER));
        shaderDataList.add(new ShaderProgram.ShaderData("resources/shaders/fragment.frag", GL3.GL_FRAGMENT_SHADER));
        shaderP = new ShaderProgram(gl, shaderDataList);
        createUniforms();
    }

    public void cleanup() {
        shaderP.cleanup();
    }

    public void render(Chunk chunk, Camera cam) {
        shaderP.bind();
        uniformsMap.setUniform("projectionMatrix", cam.getProjectionMatrix());
        uniformsMap.setUniform("viewMatrix", cam.getViewMatrix());

        Mesh mesh = chunk.getMesh();
        chunk.getEntitiesList().forEach(cube -> {
            uniformsMap.setUniform("modelMatrix", cube.modelMatrix());
            gl.glBindVertexArray(mesh.getVao().get(0));
            gl.glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        gl.glBindVertexArray(0);
        shaderP.unbind();

    }

    public void createUniforms() {
        uniformsMap = new UniformsMap(shaderP.getProgramId(), gl);
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("viewMatrix");
        uniformsMap.createUniform("modelMatrix");
    }

}
