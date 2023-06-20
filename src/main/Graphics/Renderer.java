package Graphics;

import Camera.Camera;
import GameLogic.PieceManager;
import Terrain.TerrainMap;
import Window.EventListener.KeyListener;
import Window.EventListener.MouseListener;

import static org.lwjgl.opengles.GLES30.*;


public class Renderer {
    float rotate, r;

    public Renderer() {
        rotate = 0.5f;
        r = 0.5f;
    }


    public void render(Scene scene, boolean wireFrame) {
        Camera cam = scene.getCamera();
        cam.key(KeyListener.getInstance().getPressed());
        scene.getButtonManager().whichOne(MouseListener.getX(), MouseListener.getY());
        //ButtonManager

        ShaderProgram shaderP = scene.getShaderProgram("button");
        shaderP.bind();
        scene.getButtonManager().getButtonList().forEach(button -> {
            UniformsMap buttonUniform = scene.getUniformMap("button");
            buttonUniform.setUniform("projectionMatrix", cam.getOrthoProjection());
            buttonUniform.setUniform("resizeFactor", button.getResizeFactor());
            buttonUniform.setUniform("currentColor", button.getColor());
            glBindVertexArray(button.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, button.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        glBindVertexArray(0);

//        //Terrain
        shaderP = scene.getShaderProgram("terrain");
        shaderP.bind();
        UniformsMap terrainUniforms = scene.getUniformMap("terrain");
        terrainUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
        terrainUniforms.setUniform("viewMatrix", cam.getViewMatrix());
        terrainUniforms.setUniform("tex", 0);
        TerrainMap map = scene.getTerrain();
        glActiveTexture(GL_TEXTURE0);
        scene.getTextureList().bind(map.getTextureName());
        terrainUniforms.setUniform("textureRow", scene.getTerrain().getTextureRow());
        map.getMap().forEach(chunk -> {
            terrainUniforms.setUniform("modelMatrix", chunk.getModelMatrix());
            glBindVertexArray(chunk.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, chunk.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        });
//        glPolygonMode(GL_FRONT_AND_BACK, wireFrame ? GL_LINE : GL_FILL);
        glBindVertexArray(0);
        shaderP.unbind();


//        //Piece
        shaderP = scene.getShaderProgram("piece");
        shaderP.bind();
        PieceManager.getInstance().getPieceList().forEach(piece -> {
            UniformsMap pieceUniforms = scene.getUniformMap("piece");
            pieceUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
            pieceUniforms.setUniform("viewMatrix", cam.getViewMatrix());
            pieceUniforms.setUniform("modelMatrix", piece.getModelMatrix());
            pieceUniforms.setUniform("size", piece.getMesh().getSize());
            glBindVertexArray(piece.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, piece.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
//            glPolygonMode(GL_FRONT_AND_BACK, wireFrame ? GL_LINE : GL_FILL);
            glBindVertexArray(0);
            piece.rotatePiece(MatrixCalc.rotationMatrix(rotate, (byte) 2));
            piece.rotatePiece(MatrixCalc.rotationMatrix(r, (byte) 1));
        });
        shaderP.unbind();
    }

}
