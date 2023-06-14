package GameLogic;

import Graphics.MatrixCalc;
import Graphics.PieceMesh;
import org.joml.Matrix4f;

public class Piece {
    private final PieceMesh mesh;
    private final Matrix4f modelMatrix;

    public Piece(PieceCollection collection,String pieceType){
        this.mesh = collection.getMesh(pieceType);
        this.modelMatrix = MatrixCalc.createModelMatrix(
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity().translation(0f,0f,0f),
                new Matrix4f().scale(1.0f)
        );

    }

    public void translatePiece(float x, float y,float z){
        this.modelMatrix.translation(x,y,z);
    }
    public void rotatePiece(Matrix4f rotation){
        this.modelMatrix.mul(rotation);
    }

    public PieceMesh getMesh(){
        return mesh;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}
