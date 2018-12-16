package WebSocket;

import Game.*;
import Social.Lobby;
import Social.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.BreakoutException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/ws")
public class WebSocket {

    private JSONParser parser = new JSONParser();
    private JSONObject data = new JSONObject();
    private Timer timer = new Timer();
    private TimerTask task;

    private Game activeGame;

    private String activeGameType;
    private String moeilijkheidsGraad;
    /**
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket.onOpen");
        Lobby.getInstance().addPlayer(session, "illegal User");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            this.data = (JSONObject) parser.parse(message);
        } catch (ParseException e) {
            this.data = new JSONObject();
        }
        String action = (String) data.get("action");
        switch (action) {
            case "game":
                String type = (String) data.get("type");
                JSONArray usersArray = (JSONArray) data.get("players");
                List<String> users = new ArrayList<>();
                for (int i = 0; i < usersArray.size(); i++) {
                    JSONObject user = (JSONObject) usersArray.get(i);
                    users.add((String) user.get("name"));
                }
                switch (type) {
                    case "Defend":
                        instanceGameMode("defend", users);
                        task = setTask(message, session, getActiveGame());
                        timer.scheduleAtFixedRate(task, 33L, 33L);
                        break;
                    case "Arcade":
                        instanceGameMode("arcade", users);
                        task = setTask(message, session, getActiveGame());
                        timer.scheduleAtFixedRate(task, 33L, 33L);
                        break;
                    case "Coop":
                        instanceGameMode("coop", users);
                        task = setTask(message, session, getActiveGame());
                        timer.scheduleAtFixedRate(task, 33L, 33L);
                        break;
                    case "update":
                        for (int i = 0; i < getActiveGame().getPaddles().size(); i++) {
                            JSONObject player = (JSONObject) usersArray.get(i);
                            String move = (String) player.get("move");
                            activeGame.movePaddle(activeGame.getPaddles().get(i), move);
                            if (task == null && (move.equalsIgnoreCase("left") || move.equalsIgnoreCase("right"))) {
                                setTask(message, session, activeGame);
                                timer.scheduleAtFixedRate(task, 0L, 33L);
                            }
                        }
                        break;
                    default:
                        System.out.println("Nothing has to happen here..");
                }
                break;
            case "pause":
                System.out.println("WebSocket.onMessage.pauseGame");
                if (task != null) {
                    task.cancel();
                }
                break;
            case "resume":
                System.out.println("WebSocket.onMessage.resumeGame");
                if (getActiveGame() != null) task = this.setTask(message, session, getActiveGame());
                timer.scheduleAtFixedRate(task, 33L, 33L);
                break;
            case "stop":
                System.out.println("WebSocket.onMessage.stopGame");
                if (task != null) {
                    task.cancel();
                }
                this.activeGame = null;
                break;
            case "newMessage":
                this.handleNewMessage(message, session);
                break;
            case "getActiveUsers":
                JSONObject answer = new JSONObject();
                JSONArray activeUsers = new JSONArray();
                for (User p : Lobby.getInstance().getActiveUsers()) {
                    activeUsers.add(p.getName());
                }
                answer.put("action", "activePlayers");
                answer.put("playerList", activeUsers);
                try {
                    session.getBasicRemote().sendText(answer.toJSONString());
                } catch (IOException e) {
                    throw new BreakoutException("Can't send all users");
                }
                break;
        }
    }

    private TimerTask setTask(String message, Session session, Game activeGame) {
        task = new TimerTask() {
            @Override
            public void run() {
                int currLives = 0;
                if (activeGameType.equalsIgnoreCase("arcade")) {
                    currLives = activeGame.getPlayers().get(0).getLives();
                }
                activeGame.moveBall();
                activeGame.moveEffect();
                activeGame.updateGame();
                if (activeGameType.equalsIgnoreCase("arcade") && currLives > activeGame.getPlayers().get(0).getLives() && currLives != 1) {
                    task.cancel();
                    task = null;
                    return ;
                }
                try {
                    session.getBasicRemote().sendText(activeGame.toJson().toJSONString());
                } catch (Exception e) {
                    task.cancel();
                    System.err.println("WebSocket.setTask.run");
                    System.err.println(e.getMessage());
                }
                if (activeGame.getBricks().size() == 0) {
                    System.out.println("no bricks remaining");
                    int currScore = activeGame.getPlayers().get(0).getScore();
                    task.cancel();
                    instanceGameMode("arcade", Collections.singletonList(activeGame.getPlayers().get(0).getName()));
                    activeGame.getPlayers().get(0).setScore(currScore);
                    setTask("", session, getActiveGame());
                }
            }
        };
        return task;
    }

    private void instanceGameMode(String gameType, List<String> players) {
        this.activeGameType = gameType;
        if (gameType.equalsIgnoreCase("arcade")) {
            if (activeGame == null || activeGame.getBricks().size() == 0) {
                activeGame = new Game((String) data.get("difficulty"));
                activeGame.setup(players);
            }
        } else if (gameType.equalsIgnoreCase("Coop")) {
            if (activeGame == null) {
                activeGame = new CoopMultiOnePc();
                activeGame.setup(players);
            }
        } else if (gameType.equalsIgnoreCase("Defend")) {
            if (activeGame == null) {
                activeGame = new DefendTheGlobe();
                activeGame.setup(players);
            }
        }

    }


    private void createNewMulti(String message, Session session) throws ParseException {
        JSONObject jsonObject = (JSONObject) parser.parse(message);

        String myName = (String) jsonObject.get("myName");
        String enemyName = (String) jsonObject.get("enemyName");

        User myUser = Lobby.getInstance().getActiveUserByName(myName);
        User enemyUser = Lobby.getInstance().getActiveUserByName(enemyName);

        Game game = new CoopMultiplayerGame(myUser, enemyUser);

        myUser.setActiveGame(game);
        enemyUser.setActiveGame(game);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                game.updateGame();
                try {
                    myUser.sendGameToClient();
                    enemyUser.sendGameToClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0L, 33L);
    }

    private void handleNewMessage(String message, Session session) {
        for (Session s : session.getOpenSessions()) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.err.println("Error in WebSocket.handleNewMessage");
                System.err.println(e.getMessage());
            }
        }
    }

    private Game getActiveGame() {
        return activeGame;
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket.onClose");
        Lobby.getInstance().removePlayer(session);
    }
}
