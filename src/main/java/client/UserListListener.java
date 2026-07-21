package client;

import java.util.List;

public interface UserListListener {

    void onUserListReceived(List<String> users);

}