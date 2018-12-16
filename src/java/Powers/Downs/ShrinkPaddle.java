package Powers.Downs;

import Components.Coordinate;
import Components.Paddle;
import Game.Game;
import Powers.Effect;

import java.util.List;
import java.awt.*;
import java.util.Map;
import java.util.TimerTask;

public class ShrinkPaddle extends Effect {

    private TimerTask task;
    private List<Paddle> paddles;
    private int widthToSubtract;
    private String description = "kleinere paddle";

    public ShrinkPaddle(String type, int centerX, int centerY, int radiusX, int radiusY, Color color, Map<String, Integer> props) {
        super(type, centerX, centerY, radiusX, radiusY, color, props.get("effecttime"));
        this.widthToSubtract = props.get("radius");

    }

    @Override
    public void activate(Game g) {
        System.out.println("Activated ShrinkPaddle..");
        paddles = g.getPaddles();
        paddles.forEach(paddle -> paddle.getRectangle().setWidth(paddle.getRectangle().getWidth() - widthToSubtract));
        this.setTask();
        super.getTimer().schedule(task, getEffectTime());
    }

    @Override
    public void deactivate(Game g) {
        System.out.println("Deactivated");
    }

    @Override
    public void setTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                paddles.forEach(paddle -> paddle.getRectangle().setWidth(paddle.getRectangle().getWidth() + widthToSubtract));
            }
        };
    }


    public TimerTask getTask() {
        return task;
    }


    public String getDescription() {
        return description;
    }
}
