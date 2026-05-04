package com.DOCS_ProMax.APT_3ala_barka_el_allah;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Serialises / deserialises the visible text (plus formatting) of a CharCRDT
 * to/from a compact JSON string for database storage.
 *
 * Member 1 – Database Persistence & Version History
 *
 * We intentionally store only the *visible* ordered nodes so the snapshot is
 * human-readable and compact.  A full structural snapshot (the internal tree)
 * would require custom Gson adapters for every circular reference; this simpler
 * approach is sufficient for the requirements described in the task document.
 *
 * Format:
 * [
 *   { "userID": 1, "clock": 5, "parentUser": -1, "parentClock": -1,
 *     "value": "H", "bold": false, "italic": false, "deleted": false },
 *   ...
 * ]
 */
public class CrdtSerializer {

    private static final Gson GSON = new GsonBuilder().create();

    // DTO used only for serialisation – not a domain object
    private static class NodeDto {
        int userID;
        long clock;
        int parentUser;
        long parentClock;
        char value;
        boolean bold;
        boolean italic;
        boolean deleted;
    }

    // -----------------------------------------------------------------------
    // Serialise
    // -----------------------------------------------------------------------

    /**
     * Converts all ordered nodes (including deleted ones for full fidelity) in
     * {@code crdt} into a JSON string.
     */
//    public static String toJson(CharCRDT crdt) {
//        List<NodeDto> dtos = new ArrayList<>();
//        for (CharNode n : crdt.getOrderedNodes()) {
//            NodeDto dto = new NodeDto();
//            dto.userID = n.getID().getUserID();
//            dto.clock = n.getID().getClock();
//            dto.parentUser = n.getParentID() != null ? n.getParentID().getUserID() : -1;
//            dto.parentClock = n.getParentID() != null ? n.getParentID().getClock() : -1;
//            dto.value = n.getValue();
//            dto.bold = n.isBold();
//            dto.italic = n.isItalic();
//            dto.deleted = n.isDeleted();
//            dtos.add(dto);
//        }
//        return GSON.toJson(dtos);
//    }

    // -----------------------------------------------------------------------
    // Deserialise
    // -----------------------------------------------------------------------

    /**
     * Reconstructs a {@link CharCRDT} from a JSON string that was previously
     * produced by {@link #toJson(CharCRDT)}.
     *
     * @param json   The stored JSON blob.
     * @param userId The userID to assign to the reconstructed CRDT's local clock.
     */
    // -----------------------------------------------------------------------
    // Deserialise
    // -----------------------------------------------------------------------
    public static CharCRDT fromJson(String json, int userId) {
        if (json == null || json.isBlank()) return new CharCRDT(userId);
        Type listType = new TypeToken<List<NodeDto>>() {
        }.getType();
        List<NodeDto> dtos = GSON.fromJson(json, listType);

        CharCRDT crdt = new CharCRDT(userId);
        CharID lastID = crdt.rootID;

        for (NodeDto dto : dtos) {
            CharID incomingID = new CharID(dto.userID, dto.clock);

            // THE FIX: Ignore the old, potentially deleted parent IDs.
            // The JSON is already in visual order, so we just chain them sequentially!
            CharNode node = crdt.RemotelyInsertion(incomingID, lastID, dto.value);

            if (node != null) {
                node.setBold(dto.bold);
                node.setItalic(dto.italic);
                if (dto.deleted) node.SetDeleted(true);

                lastID = incomingID; // Chain the next letter to this one
            }
        }
        return crdt;
    }
    // REPLACE THIS EXISTING DTO CLASS


    // REPLACE THIS EXISTING METHOD
    public static String toJson(CharCRDT crdt) {
        List<NodeDto> dtos = new ArrayList<>();
        // THE FIX: Use the new method to save all tombstones
        for (CharNode n : crdt.getAllNodesIncludingDeleted()) {
            NodeDto dto = new NodeDto();
            dto.userID = n.getID().getUserID();
            dto.clock = n.getID().getClock();
            dto.parentUser = n.getParentID() != null ? n.getParentID().getUserID() : -1;
            dto.parentClock = n.getParentID() != null ? n.getParentID().getClock() : -1;
            dto.value = n.getValue();
            dto.bold = n.isBold();
            dto.italic = n.isItalic();
            dto.deleted = n.isDeleted();
            dtos.add(dto);
        }
        return GSON.toJson(dtos);
    }



    // REPLACE THIS EXISTING METHOD

    // ADD THIS DTO CLASS (Below the existing NodeDto class)
    private static class BlockDto {
        int idUser; long idClock;
        int pUser; long pClock;
        String charJson;
        boolean deleted;
    }

    // ADD THESE TWO METHODS (At the bottom of the file)
    public static String toDocumentJson(BlockCRDT doc) {
        List<BlockDto> bDtos = new ArrayList<>();
        for (BlockNode bn : doc.getAllNodesIncludingDeleted()) {
            BlockDto b = new BlockDto();
            b.idUser = bn.getId().getUserID();
            b.idClock = bn.getId().getClock();
            b.pUser = bn.getParentID() != null ? bn.getParentID().getUserID() : -1;
            b.pClock = bn.getParentID() != null ? bn.getParentID().getClock() : -1;
            b.charJson = toJson(bn.getContent());
            b.deleted = bn.isDeleted();
            bDtos.add(b);
        }
        return GSON.toJson(bDtos);
    }

    public static void loadDocumentJson(String json, BlockCRDT doc) {
        if (json == null || json.isBlank()) return;
        try {
            Type listType = new TypeToken<List<BlockDto>>(){}.getType();
            List<BlockDto> dtos = GSON.fromJson(json, listType);

            // Wipe existing screen blocks
            doc.getAllNodesIncludingDeleted().forEach(bn -> bn.setDeleted(true));

            for (BlockDto b : dtos) {
                BlockID id = new BlockID(b.idUser, b.idClock);
                BlockID pID = (b.pUser == -1) ? null : new BlockID(b.pUser, b.pClock);
                CharCRDT content = fromJson(b.charJson, doc.getUserid());
                BlockNode node = doc.insertBlockWithID(id, pID, content);
                if (b.deleted) node.setDeleted(true);
            }
        } catch (Exception e) {
            // BACKWARD COMPATIBILITY: If parsing fails, it's an old single-block save!
            CharCRDT legacy = fromJson(json, doc.getUserid());
            doc.getAllNodesIncludingDeleted().forEach(bn -> bn.setDeleted(true));
            doc.insertTopLevelBlock(legacy);
        }
    }
}