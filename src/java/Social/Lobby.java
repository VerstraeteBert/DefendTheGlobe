package Social;

import javax.websocket.Session;
import java.util.HashSet;
import java.util.Set;

public class Lobby {

    private static Lobby INSTANCE;

    private Set<User> activeUsers;

    private Lobby(Set<User> activeUsers) {
        this.activeUsers = activeUsers;
    }

    private Lobby() {
        this.activeUsers = new HashSet<>();
    }

    public static Lobby getInstance() { // control amount of instances
        if (INSTANCE == null) {
            INSTANCE = new Lobby();
        }
        return INSTANCE;
    }

    public User getActiveUserByName(String name) {
        for (User u : activeUsers) {
            if (u != null && u.getName().equals(name)) {
                return u;
            }
        }
        return null;
    }
    
    public void setNameBySession(Session session, String name) {
        activeUsers.forEach(user -> {
            if (user.getSession().getId().equals(session.getId())) {
                user.setName(name);
            }
        });
    }

    public void addPlayer(Session session, String name) {
        activeUsers.add(new User(session, name));
    }

    public void removePlayer(Session session) {
        for (User u : activeUsers) {
            if (u != null && u.getSession().equals(session)) {
                activeUsers.remove(u);
            }
        }
    }

    public Set<User> getActiveUsers() {
        return activeUsers;
    }
}
