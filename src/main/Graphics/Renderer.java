package Graphics;

import Camera.Camera;
import GameLogic.Piece;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;


public class Renderer {
    float rotate,r;
    public Renderer(){
        rotate = 0.5f;
        r =0.5f;
    }


    public void render( Scene scene, Camera cam, boolean wireFrame) {
        //Terrain
        ShaderProgram shaderP =scene.getShaderProgram("terrain");
        shaderP.bind();
        UniformsMap terrainUniforms = scene.getUniformMap("terrain");
        terrainUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
        terrainUniforms.setUniform("viewMatrix", cam.getViewMatrix());
//         terrainUniforms.setUniform("tex", 0);
//         TerrainMap map = scene.getTerrain();
//         gl.glActiveTexture(GL3.GL_TEXTURE0);
//         scene.getTextureList().bind(map.getTextureName());
//        terrainUniforms.setUniform("textureRow",scene.getTerrain().getTextureRow());
        scene.getTerrain().getMap().forEach(chunk -> {
            terrainUniforms.setUniform("modelMatrix", chunk.getModelMatrix());
            glBindVertexArray(chunk.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, chunk.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        glPolygonMode(GL_FRONT_AND_BACK, wireFrame ? GL_LINE : GL_FILL);
        glBindVertexArray(0);
        shaderP.unbind();


        //Piece
        shaderP = scene.getShaderProgram("piece");
        List<Piece> list= scene.getPiece();

        shaderP.bind();
        list.forEach(piece -> {
            UniformsMap pieceUniforms = scene.getUniformMap("piece");
            pieceUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
            pieceUniforms.setUniform("viewMatrix", cam.getViewMatrix());
            pieceUniforms.setUniform("modelMatrix", piece.getModelMatrix());
            pieceUniforms.setUniform("size", piece.getMesh().getSize());
            glBindVertexArray(piece.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, piece.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
            glPolygonMode(GL_FRONT_AND_BACK, wireFrame ? GL_LINE : GL_FILL);
            glBindVertexArray(0);
            piece.rotatePiece(MatrixCalc.rotationMatrix(rotate, (byte) 2));
            piece.rotatePiece(MatrixCalc.rotationMatrix(r, (byte) 1));
        });
            shaderP.unbind();
    }

}
