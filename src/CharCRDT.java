import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharCRDT {
    private CharNode root;
    private int userid;
    private Clock clock;
    private Map<CharID, CharNode> nodeMap;
    public final CharID rootID = new CharID(-1, -1);

    public CharCRDT(int userid) {
        this.userid = userid;
        this.clock = new Clock();
        this.root = new CharNode(rootID, null, '\0');
        this.nodeMap = new HashMap<>();
        this.nodeMap.put(rootID, root);
    }

    public CharID generateID() {

        return new CharID(userid, clock.tick());
    }



   /* public CharNode createNode(CharID parentID, char value) {
        CharID ID = generateID();
        return new CharNode(ID, parentID, value);
    }*/

    private void depthFirstTraversal(CharNode node, List<CharNode> result){
        if (node!=root  && !node.isDeleted()) {
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
    // JANA BOSY 3LA DA
    public CharNode insertNode(CharID parentID, char value) {
        CharNode parent = nodeMap.get(parentID);
        if (parent != null) {
            CharID newID = generateID();
            CharNode newNode = new CharNode(newID, parentID, value);

            parent.addChild(newNode);      // Attaches it to the tree
            nodeMap.put(newID, newNode);   // Puts it in the map
            return newNode;
        }

        return null;
    }



}
