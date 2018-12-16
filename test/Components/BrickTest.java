package Components;

import org.junit.Test;

import static org.junit.Assert.*;

public class BrickTest {
    @Test
    public void getTopY() throws Exception {
        Brick brick = new Brick();

        assertEquals(brick.getRectangle().getY(), brick.getTopY(), 0.1);
    }

    @Test
    public void getBottomY() throws Exception {
        Brick brick = new Brick();

        assertEquals(brick.getRectangle().getY() + brick.getRectangle().getHeight(), brick.getBottomY(), 0.1);
    }

    @Test
    public void getLeftX() throws Exception {
        Brick brick = new Brick();

        assertEquals(brick.getRectangle().getX(), brick.getLeftX(), 0.1);
    }

    @Test
    public void getRightX() throws Exception {
        Brick brick = new Brick();

        assertEquals(brick.getRectangle().getX() + brick.getRectangle().getWidth(), brick.getRightX(), 0.1);
    }

}