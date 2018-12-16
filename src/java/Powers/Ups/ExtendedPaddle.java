package Powers.Ups;

import Components.Coordinate;
import Components.Paddle;
import Powers.Effect;
import Game.Game;
import data.Repositories;

import java.util.List;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;


/**
 * Created by tizianoplaiy on 06/12/2017.
 */
public class ExtendedPaddle extends Effect {
    private TimerTask task;
    private List<Paddle> paddles;
    private int widthToAdd;
    private String description = "extended paddle";

    public ExtendedPaddle(String type, int centerX, int centerY, int radiusX, int radiusY, Color color, Map<String, Integer> props) {
        super(type, centerX, centerY, radiusX, radiusY, color, props.get("effecttime"));
        this.widthToAdd = props.get("radius");

    }

    public void activate(Game g) {
        System.out.println("Activated.");
        paddles = g.getPaddles();
        this.setTask();
        super.getTimer().schedule(task, getEffectTime());
        paddles.forEach(paddle -> paddle.getRectangle().setWidth(paddle.getRectangle().getWidth() + widthToAdd));
    }

    public void deactivate(Game g) {
        System.out.println("Deactivated.");
    }

    public void setTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                paddles.forEach(paddle -> paddle.getRectangle().setWidth(paddle.getRectangle().getWidth() - widthToAdd));
            }
        };
    }

    public String getDescription() {
        return description;
    }

    public TimerTask getTask() {
        return task;
    }
}
