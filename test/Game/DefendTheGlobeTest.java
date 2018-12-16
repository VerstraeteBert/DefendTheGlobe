package Game;

import Components.Ball;
import Components.CurvedBrick;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import org.junit.Before;
import org.junit.Test;

public class DefendTheGlobeTest {

    public Rectangle emptyRect;
    public Arc outerArc;
    public Arc innerArc;
    public CurvedBrick brick;

    public Ball ball;
    public Ball globe;

    @Before
    public void setUp() throws Exception {
        emptyRect = new Rectangle(0, 0, 0, 0);
        outerArc = new Arc(250, 250, 150, 150, 0, 45);
        innerArc = new Arc(245, 245, 140, 140, 0, 45);
        brick = new CurvedBrick(outerArc, innerArc);

//        ball = new Ball(100, 100, 10, 10);
//        globe = new Ball(250, 250, 25, 25);
    }

    @Test
    public void testGlobeContact() {
//        globe = new Ball(50, 50, 50, 50);
//        Assert.assertTrue(ball.intersects(globe.getBoundsInLocal()));
//        globe = new Ball(50, 50, 40, 40);
//        Assert.assertTrue(ball.intersects(globe.getBoundsInLocal()));
//        globe = new Ball(50, 50, 30, 30);
//        Assert.assertFalse(ball.intersects(globe.getBoundsInLocal()));
//        globe = new Ball(150, 150, 30, 30);
//        Assert.assertFalse(ball.intersects(globe.getBoundsInLocal()));
    }

    @Test
    public void testBrickContact() {
//        ball = new Ball(brick.getShape().getBoundsInLocal().getMinX(), brick.getShape().getBoundsInLocal().getMinY(), 10, 10);
//        Assert.assertTrue(ball.intersects(brick.getShape().getBoundsInLocal()));
//        ball = new Ball(brick.getShape().getBoundsInLocal().getMaxX(), brick.getShape().getBoundsInLocal().getMaxY(), 10, 10);
//        Assert.assertTrue(ball.intersects(brick.getShape().getBoundsInLocal()));
//        ball.setvY(10);
//        ball.move();
//        Assert.assertFalse(ball.intersects(brick.getShape().getBoundsInLocal()));
    }

}