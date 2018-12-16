package Components;

import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CurvedBrickTest {

    public Rectangle emptyRect;
    public Arc outerArc;
    public Arc innerArc;
    public CurvedBrick brick;

    public Ball ball;

    @Before
    public void setUp() throws Exception {
        emptyRect = new Rectangle(0, 0, 0, 0);
        outerArc = new Arc(250, 250, 150, 150, 0, 45);
        innerArc = new Arc(245, 245, 140, 140, 0, 45);
        brick = new CurvedBrick(outerArc, innerArc);
    }

    @Test
    public void testOuterArcNotEmpty() {
        Assert.assertNotEquals(emptyRect.getBoundsInLocal(), outerArc.getBoundsInLocal());
    }

    @Test
    public void testInnerArcNotEmpty() {
        Assert.assertNotEquals(emptyRect.getBoundsInLocal(), innerArc.getBoundsInLocal());
    }

    @Test
    public void testShapeNotEmpty() throws Exception {
        Assert.assertNotEquals(emptyRect.getBoundsInLocal(), brick.getShape().getBoundsInLocal());
    }

    @Test
    public void testStartAngle() {
        Assert.assertEquals(0, outerArc.getStartAngle(), 0.01);
    }

    @Test
    public void testExtentAngle() {
        Assert.assertEquals(45, outerArc.getLength(), 0.01);
    }

    @Test
    public void testMaxX() {
        Assert.assertEquals(outerArc.getBoundsInLocal().getMaxX(), brick.getShape().getBoundsInLocal().getMaxX(), 0.1);
    }

    @Test
    public void testMinY() {
        Assert.assertEquals(outerArc.getBoundsInLocal().getMinY(), brick.getShape().getBoundsInLocal().getMinY(), 0.1);
    }

    @Test
    public void testBrickContact() throws Exception {
        setUp();
        ball = new Ball((int) brick.getShape().getBoundsInLocal().getMinX(), (int) brick.getShape().getBoundsInLocal().getMinY(), 10, 10, null);
        Assert.assertTrue(ball.getEllipse().intersects(brick.getShape().getBoundsInLocal()));
        ball = new Ball((int) brick.getShape().getBoundsInLocal().getMaxX(), (int) brick.getShape().getBoundsInLocal().getMaxY(), 10, 10, null);
        Assert.assertTrue(ball.getEllipse().intersects(brick.getShape().getBoundsInLocal()));
        ball.setvY(10);
        ball.move();
        ball.move();
        Assert.assertFalse(ball.getEllipse().intersects(brick.getShape().getBoundsInLocal()));
    }
}