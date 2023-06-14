package GameLogic;

import Graphics.PieceMesh;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PieceCollection {
    Map<String, PieceMesh> collection;
    public PieceCollection() throws IOException {
        initPieces();
    }

    private void initPieces() throws IOException {
        this.collection = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root = (ArrayNode) mapper.readTree(new File("src/main/resources/piece/pieces.json"));
        root.elements().forEachRemaining(item ->{
            String name = item.get("name").asText();
            PieceMesh mesh = mapper.convertValue(item.get("meshData"),PieceMesh.class);
            this.collection.put(name,mesh);
        });

    }


    protected PieceMesh getMesh(String name){
        return collection.get(name);
    }

}
