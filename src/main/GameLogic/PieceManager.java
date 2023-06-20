package GameLogic;

import java.util.ArrayList;
import java.util.List;

public class PieceManager {
    private static PieceManager pieceManager;
    List<Piece> pieceList;
    private int count = 0;

    private PieceManager() {
        this.pieceList = new ArrayList<>();
    }

    public static PieceManager getInstance() {
        if (pieceManager == null) {
            pieceManager = new PieceManager();
        }
        return pieceManager;
    }

    public void addPiece(String pieceType) {
        if (!pieceList.isEmpty()) {
            if (pieceList.get(pieceList.size() - 1).getPosition().equals(0.0f, 0.0f, 0.0f)) {
                pieceList.get(pieceList.size() - 1).translatePiece(3.0f, count, 0.f);
                count += 3;
            }
        }
        pieceList.add(new Piece(pieceType));
    }

    public List<Piece> getPieceList() {
        return pieceList;
    }
}
