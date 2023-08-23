package com.game.lwjgl.Graphics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.game.common.Constants;
import com.game.common.PieceCollection;
import com.game.lwjgl.Graphics.Mesh.LwjglPieceMesh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LwjglPieceCollection extends PieceCollection {

    private static LwjglPieceCollection instance;
    Map<String, LwjglPieceMesh> collection;

    private LwjglPieceCollection() {
        initPieces();
    }


    public static LwjglPieceCollection getInstance() {
        if (instance == null) {
            instance = new LwjglPieceCollection();
        }
        return instance;
    }

    @Override
    protected void initPieces() {
        this.collection = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root;
        try {
            root = (ArrayNode) mapper.readTree(Constants.PIECE_DATA);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        root.elements().forEachRemaining(item -> {
            String name = item.get("name").asText();
            LwjglPieceMesh mesh = mapper.convertValue(item.get("meshData"), LwjglPieceMesh.class);
            this.collection.put(name, mesh);
        });

    }

    @Override
    public List<String> getPieceType() {
        return getInstance().collection.keySet().stream().toList();
    }

    public LwjglPieceMesh getMesh(String name) {
        return collection.get(name);
    }


}
