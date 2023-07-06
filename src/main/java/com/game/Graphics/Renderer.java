package com.game.Graphics;

import com.game.Camera.Camera;
import com.game.Terrain.TerrainMap;
import com.game.Window.EventListener.KeyListener;
import com.game.Window.Window;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL33.*;


public class Renderer {
    private static Renderer renderer;
    float rotate, r;
    private static boolean wireFrame = false;

    private Renderer() {
        rotate = 0.5f;
        r = 0.5f;
    }

    public static Renderer getInstance(){
        if(renderer == null){
            renderer = new Renderer();
        }
        return renderer;
    }


    public void render(Scene scene) {
        Camera cam = scene.getCamera();
        cam.key(KeyListener.getInstance().getPressed());



        glPolygonMode(GL_FRONT_AND_BACK, wireFrame ? GL_LINE : GL_FILL);

        //Terrain
        ShaderProgram shaderP = scene.getShaderProgram("terrain");
        shaderP.bind();
        UniformsMap terrainUniforms = scene.getUniformMap("terrain");
        terrainUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
        terrainUniforms.setUniform("viewMatrix", cam.getViewMatrix());
        terrainUniforms.setUniform("tex",0);
        TerrainMap map = scene.getTerrain();
        glActiveTexture(GL_TEXTURE0);
        scene.getTextureList().bind(map.getTextureName());
        terrainUniforms.setUniform("fValue", new float[]{
                TerrainMap.getTextureRow()
        });
        map.getMap().forEach(chunk -> {
            terrainUniforms.setUniform("modelMatrix", chunk.getModelMatrix());
            glBindVertexArray(chunk.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, chunk.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        });
        glBindVertexArray(0);
        shaderP.unbind();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        //GUI
        GuiScene gui = scene.getGui();
        shaderP = gui.getShaderP("minimap");
        shaderP.bind();
        UniformsMap miniMapUniforms = gui.getUniformMap("minimap");
        miniMapUniforms.setUniform("projectionMatrix",cam.getOrthoProjection());
        miniMapUniforms.setUniform("viewPort",new Vector2f(Window.getWidth(),Window.getHeight()));
        miniMapUniforms.setUniform("tex",1);
        glActiveTexture(GL_TEXTURE1);
        scene.getTextureList().bind("minimap");
        MiniMap miniMap = gui.getMiniMap();
        glBindVertexArray(miniMap.getMesh().getVao());
        glDrawElements(GL_TRIANGLES,miniMap.getMesh().getNumVertices(),GL_UNSIGNED_INT,0);
        shaderP.unbind();






//        //Piece
//        shaderP = scene.getShaderProgram("piece");
//        shaderP.bind();
//        PieceManager.getInstance().getPieceList().forEach(piece -> {
//            UniformsMap pieceUniforms = scene.getUniformMap("piece");
//            pieceUniforms.setUniform("projectionMatrix", cam.getProjectionMatrix());
//            pieceUniforms.setUniform("viewMatrix", cam.getViewMatrix());
//            pieceUniforms.setUniform("modelMatrix", piece.getModelMatrix());
//            pieceUniforms.setUniform("size", piece.getMesh().getSize());
//            glBindVertexArray(piece.getMesh().getVao());
//            glDrawElements(GL_TRIANGLES, piece.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
//            glBindVertexArray(0);
//            piece.rotatePiece(MatrixCalc.rotationMatrix(rotate, (byte) 2));
//            piece.rotatePiece(MatrixCalc.rotationMatrix(r, (byte) 1));
//        });
//        shaderP.unbind();

    }

    public static void wireFrame(){
        wireFrame = !wireFrame;
    }

}
