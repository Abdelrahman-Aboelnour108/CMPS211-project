import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharCRDT {
    private CharNode root;
    private int userid;
    private Clock clock;
    private Map<CharID, CharNode> nodeMap;

    public CharCRDT(int userid) {
        this.userid = userid;
        this.clock = new Clock();
        this.root = new CharNode(null, null, '\0');
        this.nodeMap = new HashMap<>();
    }

    public CharID generateID() {
        return new CharID(userid, clock.tick());
    }



    public CharNode createNode(CharID parentID, char value) {
        CharID ID = generateID();
        return new CharNode(ID, parentID, value);
    }

    private void depthFirstTraversal(CharNode node, List<CharNode> result){
        if (node.getID() != null && !node.isDeleted()) {
            result.add(node);
        }
        for (CharNode child : node.getChildren()) {
            depthFirstTraversal(child, result);
        }

    }
    public List<CharNode> getOrderedNodes() {
        List<CharNode> result = new ArrayList<>();
        depthFirstTraversal(root, result);
        return result;
    }




}
