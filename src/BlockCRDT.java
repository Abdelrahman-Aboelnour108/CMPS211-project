import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BlockCRDT {
    
    private static final int MAX_LINES = 10;
    private static final int MIN_LINES = 2;

    BlockNode root;
    private int userid;
    private Clock clock;
    private Map<BlockID, BlockNode> nodeMap;

    public BlockCRDT(int userid, Clock clock) {
        this.userid = userid;
        this.clock = clock;
        this.nodeMap = new HashMap<>();
        this.root = new BlockNode(null, null, null);
        nodeMap.put(root.getId(), root);
    }
    private BlockID generateID() {
        return new BlockID(userid, clock.tick());
    }

    public BlockNode createNode(BlockID parentID, CharCRDT content) {
        BlockID ID = generateID();
        BlockNode newNode = new BlockNode(ID, parentID, content);
        nodeMap.put(ID, newNode);
        return newNode;
    }

    private BlockNode getNode(BlockID id) {
        return nodeMap.get(id);
    }

    public void deleteNode(BlockID id) {
        BlockNode node = getNode(id);
        if (node != null) {
            node.setDeleted(true);
        }
    }

    public void insertBlock(BlockID parentID, CharCRDT content) {
        BlockNode parentNode = getNode(parentID);

        if (parentNode != null) {
            BlockNode newNode = createNode(parentID, content);
            parentNode.addChild(newNode);
        }

    }

    private void depthFirstTraversal(BlockNode node, List<BlockNode> result){
        if (node.getId() != null && !node.isDeleted()) {
            result.add(node);
        }
        for (BlockNode child : node.getChildren()) {
            depthFirstTraversal(child, result);
        }

    }

    public List<BlockNode> getOrderedNodes() {
        List<BlockNode> result = new ArrayList<>();
        depthFirstTraversal(root, result);
        return result;
    }
    

    public void checkAndSplit_Merge(BlockID targetblockID) {
        BlockNode targetNode =getNode(targetblockID);

        if (isblockEmpty(targetNode)) {
            return;
        }

        if (isSplitNeeded(targetNode)) {
            splitBlock(targetNode);
        } else if (isMergeNeeded(targetNode)) {
            mergeBlock(targetNode);
        }
       
    }

    private boolean isSplitNeeded(BlockNode node) {
        return node.getLineCount() > MAX_LINES;
    }

    private boolean isMergeNeeded(BlockNode node) {
        return node.getLineCount() < MIN_LINES;
    }

    private boolean isblockEmpty(BlockNode node) {
        return node == null || node.isDeleted() || node.getContent() == null;
    }

    private void splitBlock(BlockNode sourceNode) {
        BlockNode targetNode = createSiblingBlock(sourceNode);
        sourceNode.moveAllTextToAfterLine(targetNode, MAX_LINES/2);
    }


    private BlockNode createSiblingBlock(BlockNode sourceNode) {
        BlockNode newNode = createNode(sourceNode.getId(), new CharCRDT(this.userid));
        sourceNode.addChild(newNode);
        return newNode;
    }

    public void mergeBlock(BlockNode targetNode) {
        BlockNode siblingNode = findSiblingForMerge(targetNode);
        if (siblingNode != null) {
            if (siblingNode.moveAllTextToAfterLine(targetNode,0)) {
                deleteNode(siblingNode.getId());
            }
        }
    }
    private BlockNode findSiblingForMerge(BlockNode targetNode) {

        BlockNode parent = getNode(targetNode.getParentID());
        if (parent == null) {
            return null; 
        }

        for (BlockNode sibling : parent.getChildren()) {
            if (sibling != targetNode && !sibling.isDeleted()) {
                return sibling;
            }
        }
        return null;
    }
   
}
