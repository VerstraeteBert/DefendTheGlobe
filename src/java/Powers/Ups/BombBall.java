package Powers.Ups;

import Components.Ball;
import Game.Game;
import Powers.Effect;
import data.Repositories;

import java.awt.*;
import java.util.Map;
import java.util.TimerTask;

public class BombBall extends Effect {

    private Game game;
    private Ball bomb;

    private int radius;
    private String description = "ontploffing van bricks";
    private TimerTask task;

    public BombBall(String type, int centerX, int centerY, int radiusX, int radiusY, Color color, Map<String, Integer> props) {
        super(type, centerX, centerY, radiusX, radiusY, color);
        this.radius = props.get("radius");
        this.bomb = new Ball(getEllipse().getCenterX(), getEllipse().getCenterY(), this.radius, this.radius, Color.WHITE);
    }

    @Override
    public void activate(Game g) {
        this.game = g;
        g.brickContact(bomb, true);
    }

    @Override
    public void deactivate(Game g) {

    }

    @Override
    public void setTask() {

    }

    public String getDescription(){
        return description;
    }

    public TimerTask getTask(){
        return task;
    }
}
