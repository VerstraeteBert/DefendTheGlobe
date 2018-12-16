package Social;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by vande on 2/12/2017.
 */
public class LobbyTest {

    @Before
    public void setUp() throws Exception {
        Lobby.getInstance().addPlayer(null, "TestUser");
    }

    @Test
    public void getActiveUserByName() throws Exception {
        User testUser = Lobby.getInstance().getActiveUserByName("TestUser");
        Assert.assertNotNull(testUser);
    }

    @Test
    public void getActiveUsers() throws Exception {
        Assert.assertEquals(1, Lobby.getInstance().getActiveUsers().size());
        Lobby.getInstance().addPlayer(null, "TestUser2");
        Lobby.getInstance().addPlayer(null, "TestUser3");
        Assert.assertEquals(3, Lobby.getInstance().getActiveUsers().size());
    }

}