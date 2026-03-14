import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockNode {

private final BlockID id;
private final BlockID parentID;
private final CharCRDT content;
private  boolean isDeleted;
private final List<BlockNode> children;

    public BlockNode(BlockID id, BlockID parentID, CharCRDT content) {
    this.id = id;
    this.parentID = parentID;
    this.content = content;
    this.isDeleted = false;
    this.children = new ArrayList<>();
    }

    public BlockID getId() {
        return id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public CharCRDT getContent() {
        return content;
    }

    public List<BlockNode> getChildren() {
        return children;
    }

    public int getLineCount() {
       if (isEmpty() || isDeleted())
           return 0;
        return getTotalLineCount();

    }
    
    public BlockID getParentID() {
        return parentID;
    }

    public CharNode addChar(CharID parentID, char value) {
        CharNode newCharNode = content.createNode(parentID, value);
        return newCharNode;
    }
    
    public List<CharNode> getChars() {
        return content.getOrderedNodes();
    }

     public void deleteBlock() {
         setDeleted(true);
     }
    private int getTotalLineCount() {
        int lines = 1;
        for (CharNode charNode : content.getOrderedNodes()) {
            if (charNode.getValue() == '\n') {
                lines++;
            }
        }
        return lines;
    }

    private boolean isEmpty() {
        return content.getOrderedNodes().isEmpty();
    }
    
    public void addChild(BlockNode child) {
        children.add(child);
        children.sort(Comparator.comparing(BlockNode::getId));
    }

}

