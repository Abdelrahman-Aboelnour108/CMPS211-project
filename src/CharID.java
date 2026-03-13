import java.util.Objects;

public class CharID {
    private int userID;
    private long clock;

    public CharID(int userID, long clock) {
        this.userID = userID;
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
        CharID other = (CharID) obj;
        return userID == other.userID && clock == other.clock;
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
