package com.game.GameLogic;

import com.game.Graphics.Mesh.PieceMesh;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieceCollection {

    private static PieceCollection instance;
    Map<String, PieceMesh> collection;

    private PieceCollection() {
        initPieces();
    }


    public static PieceCollection getInstance() {
        if (instance == null) {
            instance = new PieceCollection();
        }
        return instance;
    }

    private void initPieces() {
        this.collection = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root;
        try {
            root = (ArrayNode) mapper.readTree(new File("src/main/resources/piece/pieces.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        root.elements().forEachRemaining(item -> {
            String name = item.get("name").asText();
            PieceMesh mesh = mapper.convertValue(item.get("meshData"), PieceMesh.class);
            this.collection.put(name, mesh);
        });

    }

    public static List<String> getPieceType() {
        return getInstance().collection.keySet().stream().toList();
    }

    public PieceMesh getMesh(String name) {
        return collection.get(name);
    }

}
