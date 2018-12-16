package Components;

import org.junit.Test;

import static org.junit.Assert.*;

public class PaddleTest {
    @Test
    public void increasePaddleSpeed() throws Exception {
        Paddle paddle1 = new Paddle();
        Paddle paddle2 = new Paddle();

        paddle2.increasePaddleSpeed();

        paddle1.moveRight();
        paddle2.moveRight();

        assertTrue(paddle1.getLeftX() < paddle2.getLeftX());
    }

    @Test
    public void decreasePaddleSpeed() throws Exception {
        Paddle paddle1 = new Paddle();
        Paddle paddle2 = new Paddle();

        paddle2.decreasePaddleSpeed();

        paddle1.moveLeft();
        paddle2.moveLeft();

        assertTrue(paddle1.getLeftX() < paddle2.getLeftX());
    }

    @Test
    public void moveLeft() throws Exception {
        Paddle paddle = new Paddle();

        int initX = paddle.getLeftX();

        paddle.moveLeft();

        assertTrue(initX > paddle.getLeftX());
    }

    @Test
    public void moveRight() throws Exception {
        Paddle paddle = new Paddle();

        int initX = paddle.getLeftX();

        paddle.moveRight();

        assertTrue(initX < paddle.getLeftX());

    }

}