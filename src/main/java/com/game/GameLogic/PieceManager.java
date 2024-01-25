package com.game.GameLogic;

import com.game.Camera.Camera;
import com.game.Graphics.ShaderProgram;
import com.game.Graphics.UniformsMap;
import com.game.Utils.MatrixCalc;
import com.game.templates.SceneItem;

import java.util.ArrayList;
import java.util.List;

import static com.game.Graphics.Scene.createShaderProgram;
import static com.game.Graphics.Scene.createUniformMap;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class PieceManager extends SceneItem {

    private ShaderProgram shaderProgram;
    private UniformsMap uniformsMap;
    private final List<Piece> pieceList;
    private final int count = 0;

    public PieceManager() {
        this.pieceList = new ArrayList<>();
    }

    public void addPiece(String pieceType, float x, float y, float z) {
        pieceList.add(new Piece(pieceType, x, y, z));
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }

    @Override
    public void render(Camera cam, boolean isWireFrame) {
        glPolygonMode(GL_FRONT_AND_BACK, isWireFrame ? GL_LINE : GL_FILL);
        shaderProgram.bind();
        uniformsMap.setUniform("projectionMatrix", cam.getProjectionMatrix());
        uniformsMap.setUniform("viewMatrix", cam.getViewMatrix());
        pieceList.forEach(piece -> {
            uniformsMap.setUniform("modelMatrix", piece.getModelMatrix());
            uniformsMap.setUniform("size", piece.getMesh().getSize());
            glBindVertexArray(piece.getMesh().getVao());
            glDrawElements(GL_TRIANGLES, piece.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
            piece.rotatePiece(MatrixCalc.rotationMatrix(0.5f, (byte) 2));
            piece.rotatePiece(MatrixCalc.rotationMatrix(0.3f, (byte) 1));
        });
        glBindVertexArray(0);
        shaderProgram.unbind();
    }

    @Override
    public void init(String id, String vertShader, String fragShader, String[] uniformList) {
        this.shaderProgram = createShaderProgram(vertShader, fragShader);
        this.uniformsMap = createUniformMap(shaderProgram, uniformList);
    }

    @Override
    public void cleanup() {

    }
}
