package Game;

import Components.Player;
import Social.User;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VersusMultiplayerGame extends Game {

    private List<User> users = new ArrayList<>();

    public VersusMultiplayerGame(User ...users) {
        super();
        this.users.addAll(Arrays.asList(users));
    }
}
