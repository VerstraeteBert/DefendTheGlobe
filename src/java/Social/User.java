package Social;

import Game.*;

import javax.websocket.Session;
import java.io.IOException;

public class User {

    private Session session;
    private String name;

    private Game activeGame;

    public User(Session session, String name) {
        this.session = session;
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public String getName() {
        return name;
    }

    public Game getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(Game game) {
        this.activeGame = game;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setSession(Session session) {
        this.session = session;
    }

    public void sendGameToClient() throws IOException {
        session.getBasicRemote().sendText(activeGame.toJson().toJSONString());
    }
}
