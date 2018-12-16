package Components;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void increaseScore() throws Exception {
        Player player = new Player();

        int initScore = player.getScore();

        player.increaseScore();

        assertEquals(initScore + 1, player.getScore());
    }

    @Test
    public void decreaseLives() throws Exception {
        Player player = new Player();

        int initLives = player.getLives();

        player.decreaseLives();

        assertEquals(initLives - 1, player.getLives());
    }

    @Test
    public void increaseLives() throws Exception {
        Player player = new Player();

        int initLives = player.getLives();

        player.increaseLives();

        assertEquals(initLives + 1, player.getLives());
    }

}