import java.util.List;

public interface SessionUsersListener {
    void onUsersChanged(List<String> users);
}