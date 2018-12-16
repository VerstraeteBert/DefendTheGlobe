package Powers.Downs;

import Components.Coordinate;
import Components.Paddle;
import Game.Game;
import Powers.Effect;

import java.awt.*;
import java.util.Map;
import java.util.TimerTask;

public class Ceiling extends Effect {
    private Paddle ceiling;
    private Game g;
    private TimerTask task;
    private String description = "temporary ceiling";


    public Ceiling(String type, int centerX, int centerY, int radiusX, int radiusY, Color color, Map<String, Integer> props) {
        super(type, centerX, centerY, radiusX, radiusY, color, props.get("effecttime"));

    }

    public void activate(Game g) {
        this.g = g;
        System.out.println("Activated ceiling (Power-down)..");
        if (this.g.getCeiling() != null){
            this.g.getCeiling().getRectangle().setWidth(Game.DEFAULT_WIDTH);
            this.g.getCeiling().getRectangle().setX(0);

        }else{
        ceiling = new Paddle();
        ceiling.getRectangle().setX(0);
        ceiling.getRectangle().setY(300);
        ceiling.getRectangle().setWidth(Game.DEFAULT_WIDTH);
            g.setCeiling(ceiling);
            this.setTask();
            super.getTimer().schedule(task, getEffectTime(), getEffectTime());
        }


    }

    @Override
    public void deactivate(Game g) {
        System.out.println("Does nothing..");
    }

    @Override
    public void setTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                ceiling.getRectangle().setWidth(ceiling.getRectangle().getWidth() - Game.DEFAULT_WIDTH / 50);
                ceiling.getRectangle().setX(ceiling.getRectangle().getX() + Game.DEFAULT_WIDTH / 100);
                g.setCeiling(ceiling);
                if (ceiling.getRectangle().getWidth() <= 0){
                    task.cancel();
                    g.setCeiling(null);
                }
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
