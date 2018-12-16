package Components;

import Game.Game;

import java.awt.*;

public class Paddle extends Brick implements Movable {

    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 5;

    private boolean isShowing;

    private int vX;

    public Paddle() {
        super(new Coordinate(Game.DEFAULT_WIDTH / 2 - DEFAULT_WIDTH / 2, Game.DEFAULT_HEIGHT - 50), DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.BLACK);
        this.vX = 10;
        this.isShowing = true;
    }

    public void increasePaddleSpeed() {
        vX++;
    }

    public void decreasePaddleSpeed() {
        vX--;
    }

    @Override
    public void moveLeft() {
        getRectangle().setX(Math.max(this.getRectangle().getX() - vX, 0));
    }

    @Override
    public void moveRight() {
        getRectangle().setX(Math.min(this.getRectangle().getX() + vX, Game.DEFAULT_WIDTH - getRectangle().getWidth()));
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }
}
