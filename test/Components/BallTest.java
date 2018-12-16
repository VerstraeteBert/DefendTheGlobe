package Components;

import org.junit.Test;

import static org.junit.Assert.*;

public class BallTest {

    /**
     * if either vx or vy is 0 it is changed in move to prevent stale game
     * @throws Exception if fails
     */
    @Test
    public void move() throws Exception {
        Ball ball = new Ball();
        ball.setvX(5);
        double initX = ball.getEllipse().getCenterX();
        double initY = ball.getEllipse().getCenterY();
        double endX = initX + ball.getvX();
        double endY = initY + ball.getvY();

        ball.move();

        assertEquals(endX, ball.getEllipse().getCenterX(), 0.1);
        assertEquals(endY, ball.getEllipse().getCenterY(), 0.1);
    }

    @Test
    public void increaseSpeed() throws Exception {
        Ball ball1 = new Ball();
        Ball ball2 = new Ball();

        for (int i = 0; i < 20; i++) {
            ball1.increaseSpeed();
        }

        ball1.move();
        ball2.move();

        assertNotEquals(ball1.getEllipse().getCenterX(), ball2.getEllipse().getCenterX());
        assertNotEquals(ball1.getEllipse().getCenterY(), ball2.getEllipse().getCenterY());
    }

}