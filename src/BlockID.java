import java.util.Objects;

public class BlockID implements Comparable<BlockID> {
    private int userID;
    private long clock;

    public BlockID(int blockNumber, long clock) {
        this.userID = blockNumber;
        this.clock = clock;
    }

    public int getUserID() {
        return userID;
    }

    public long getClock() {
        return clock;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlockID other = (BlockID) obj;
        return userID == other.userID && clock == other.clock;
    }
    
    @Override
    public int compareTo(BlockID other) {
        if (this.clock != other.clock) {
            return Long.compare(this.clock, other.clock);
        }
        return Integer.compare(this.userID, other.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, clock);
    }

    @Override
    public String toString() {
        return "(" + userID + "," + clock + ")";
    }
}
