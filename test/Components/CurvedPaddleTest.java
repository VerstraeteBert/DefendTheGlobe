package Components;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CurvedPaddleTest {

    public Ball ball;

    public CurvedPaddle paddle;

    @Before
    public void setUp() throws Exception {
        paddle = new CurvedPaddle();
    }

    @Test
    public void moveLeft() throws Exception {
        setUp();
        paddle.moveLeft();
        Assert.assertEquals(-360/20, paddle.getOuterArc().getStartAngle(), 0.1);
        paddle.moveLeft();
        Assert.assertEquals(-360/20 * 2, paddle.getOuterArc().getStartAngle(), .01);
    }

    @Test
    public void moveRight() throws Exception {
        setUp();
        paddle.moveRight();
        Assert.assertEquals(360/20, paddle.getOuterArc().getStartAngle(), 0.1);
        paddle.moveRight();
        Assert.assertEquals(360/20 * 2, paddle.getOuterArc().getStartAngle(), 0.1);
    }

    @Test
    public void testPaddleContact() throws Exception {
        new CurvedBrickTest().testBrickContact();
    }

}