package Powers;

import Components.Ball;
import Components.Coordinate;
import Game.Game;
import javafx.scene.shape.Ellipse;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vande on 14/10/2017.
 */
public abstract class Effect extends Ball {

    protected static final int DEFAULT_EFFECT_TIME = 10000;

    private String type;
    private int vY;
    private int vX;
    private Color color;
    private int effectTime;
    private Timer timer = new Timer();

    public Effect (String type, int centerX, int centerY, int radiusX, int radiusY, Color color) {
        super(centerX, centerY, radiusX, radiusY, color);
        this.type = type;
        this.vX = 0;
        this.color = color;
    }
    public Effect (String type, int centerX, int centerY, int radiusX, int radiusY, Color color, int effectTime) {
        super(centerX, centerY, radiusX, radiusY, color);
        this.type = type;
        this.vX = 0;
        this.color = color;
        this.effectTime = effectTime;
    }

    public int getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(int effectTime) {
        this.effectTime = effectTime;
    }

    public String getType() {
        return type;
    }

    public int getvY() {
        return vY;
    }

    public void setvY(int vY) {
        this.vY = vY;
    }

    public int getvX() {
        return vX;
    }

    public void setvX(int vX) {
        this.vX = vX;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void move() {
        getEllipse().setCenterY(getEllipse().getCenterY() - vY);
    }

    public abstract void activate(Game g);
    public abstract void deactivate(Game g);
    public abstract void setTask();
    public abstract TimerTask getTask();
    public abstract String getDescription();


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (color != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("red", color.getRed());
            jsonObject.put("green", color.getGreen());
            jsonObject.put("blue", color.getBlue());
            json.put("color", jsonObject);
        }
        json.put("x", getEllipse().getCenterX());
        json.put("y", getEllipse().getCenterY());
        json.put("radius", getEllipse().getRadiusX());
        return json;
    }
}
