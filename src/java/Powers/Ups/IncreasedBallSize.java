package Powers.Ups;

import Components.Ball;
import Components.Coordinate;
import Powers.Effect;
import Game.Game;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by tizianoplaiy on 13/12/2017.
 */
public class IncreasedBallSize extends Effect {
    TimerTask task;
    private List<Ball> balls;
    private int radiusToAdd;
    private String description = "increased ballsize";

    public IncreasedBallSize(String type, int centerX, int centerY, int radiusX, int radiusY, Color color, Map<String, Integer> props) {
        super(type, centerX, centerY, radiusX, radiusY, color, props.get("effecttime"));
        this.radiusToAdd = props.get("radius");

    }

    public void activate(Game g) {
        System.out.println("Activated Ball Size.");
        balls = g.getBalls();
        balls.forEach(ball -> {
            ball.getEllipse().setRadiusX(ball.getEllipse().getRadiusX() + radiusToAdd);
            ball.getEllipse().setRadiusY(ball.getEllipse().getRadiusY() + radiusToAdd);
        });

        this.setTask();
        super.getTimer().schedule(task, getEffectTime());
    }

    public void deactivate(Game g) {
        System.out.println("deactivated Ball Size.");
    }

    public void setTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                balls.forEach(ball -> {
                    ball.getEllipse().setRadiusX(ball.getEllipse().getRadiusX() - radiusToAdd);
                    ball.getEllipse().setRadiusY(ball.getEllipse().getRadiusY() - radiusToAdd);
                });
            }
        };
    }

    public String getDescription(){
        return description;
    }

    public TimerTask getTask(){
        return task;
    }
}
