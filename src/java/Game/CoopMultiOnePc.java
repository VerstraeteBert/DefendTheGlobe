package Game;

import Components.Ball;
import Components.Paddle;
import Components.Player;
import Powers.Effect;
import Social.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoopMultiOnePc extends Game {


    public CoopMultiOnePc() {
        super();
    }

    public void setup(List<String> users) {
        setupBricks(DEFAULT_ROW_COUNT);
        setupPaddles(users);
        setUpPlayers(users);
        setupBalls();
    }

}
