import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockNode {

private final BlockID id;
private final BlockID parentID;
private final CharCRDT charCRDT;
private  boolean isDeleted;
private final List<BlockNode> children;

    public BlockNode(BlockID id, BlockID parentID, CharCRDT content) {
    this.id = id;
    this.parentID = parentID;
    this.charCRDT = content;
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
        return charCRDT;
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
        return charCRDT.insertNode(parentID, value);
    }
    
    public List<CharNode> getChars() {
        return charCRDT.getOrderedNodes();
    }

    public boolean moveAllTextToAfterLine(BlockNode targetNode,int splitLineCount) {
        int currentLines = 0;
        CharID lastCharID = targetNode.getLastCharID();

        for (CharNode charNode : getChars()) {
            
            if (currentLines >= splitLineCount)
             {
                CharNode newChar = targetNode.addChar(lastCharID, charNode.getValue());
                if (newChar == null) {
                     return false;
                }
                lastCharID = newChar.getID();
                charNode.SetDeleted(true);
            }
            if (charNode.getValue() == '\n') {
                currentLines++;
            }
        }
        return true;
    }

    public CharID getLastCharID() {
        List<CharNode> chars = getChars();
        if (!chars.isEmpty()) {
            return chars.get(chars.size() - 1).getID();
        }

        return null;
    }

     public void deleteBlock() {
         setDeleted(true);
     }
    private int getTotalLineCount() {
        int lines = 1;
        for (CharNode charNode : charCRDT.getOrderedNodes()) {
            if (charNode.getValue() == '\n') {
                lines++;
            }
        }
        return lines;
    }

    private boolean isEmpty() {
        return charCRDT.getOrderedNodes().isEmpty();
    }
    
    public void addChild(BlockNode child) {
        children.add(child);
        children.sort(Comparator.comparing(BlockNode::getId));
    }

}

