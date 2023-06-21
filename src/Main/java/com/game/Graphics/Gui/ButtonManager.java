package com.game.Graphics.Gui;

import java.util.ArrayList;
import java.util.List;

public class ButtonManager {
    private final List<Button> buttonList;
//    private PieceManager pieceManager;

    public ButtonManager() {
        buttonList = new ArrayList<>();
//        pieceManager = new PieceManager();
    }

//    protected ButtonManager(String[] initPieces, boolean vertical, boolean horizontal) {
//        buttonList = new ArrayList<>();
//        for (String pieceType : initPieces) {
//            buttonList.add(new Button(pieceType));
//        }
//    }

//    public void addButtonByMeshData(float[] position, int[] indices) {
//        ButtonMesh mesh = new ButtonMesh(position, indices);
//        addButton(new Button(mesh));
//    }

    public void addButton(float leftMost, float topMost, float buttonSize) {
        this.buttonList.add(Button.createButton(leftMost, topMost, buttonSize));
    }

    public void whichOne(float posX, float posY) {
        for (Button button : buttonList) {
            if (button.isCurrent(posX, posY)) {
//                addPiece(PieceCollection.getInstance().getPieceType().get(new Random().nextInt(PieceCollection.getInstance().getPieceType().size())));
                break;
            }
        }
    }

    public List<Button> getButtonList() {
        return buttonList;
    }

    public void evalPlacement() {
        buttonList.forEach(Button::eval);
    }
}
