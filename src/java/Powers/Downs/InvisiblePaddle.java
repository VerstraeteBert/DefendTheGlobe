package Powers.Downs;

import Components.Paddle;
import Game.Game;
import Powers.Effect;

import java.awt.*;
import java.util.Map;
import java.util.TimerTask;

public class InvisiblePaddle extends Effect {

    private Game game;

    private TimerTask task;
    private String description = "invisible paddle";

    public InvisiblePaddle(String type, int centerX, int centerY, int radiusX, int radiusY, Color color, Map<String, Integer> props) {
        super(type, centerX, centerY, radiusX, radiusY, color, props.get("effecttime"));
    }

    @Override
    public void activate(Game g) {
        this.game = g;
        g.getPaddles().forEach(paddle -> paddle.setShowing(false));
        setTask();
    }

    @Override
    public void deactivate(Game g) {
        System.out.println("called");
        g.getPaddles().forEach(paddle -> paddle.setShowing(true));
    }

    @Override
    public void setTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                deactivate(game);
            }
        };
        getTimer().schedule(task, getEffectTime());
    }
    public String getDescription(){
        return description;
    }

    public TimerTask getTask(){
        return task;
    }
}
