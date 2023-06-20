package GameLogic;

import Graphics.MatrixCalc;
import Graphics.PieceMesh;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Piece {
    private final PieceMesh mesh;
    private final Matrix4f modelMatrix;
    private Vector3f position;

    public Piece(String pieceType){
        this.position = new Vector3f(0.0f,0.0f,0.0f);
        this.mesh = PieceCollection.getInstance().getMesh(pieceType);
        this.modelMatrix = MatrixCalc.createModelMatrix(
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity(),
                new Matrix4f().identity().translation(position),
                new Matrix4f().scale(1.0f)
        );

    }
    public Vector3f getPosition(){
        return position;
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
