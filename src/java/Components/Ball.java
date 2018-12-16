package Components;

import Game.Game;
import javafx.scene.shape.Ellipse;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.Random;

public class Ball {

    public static final int DEFAULT_RADIUS = 5;
    public static final Color DEFAULT_COLOR = Color.BLUE;
    public static final double DEFAULT_VX = 0;
    public static final double DEFAULT_VY = -5;
    private Ellipse ellipse;

    private Color color;
    private int vX;
    private int vY;

    public Ball(double centerX, double centerY, double radiusX, double radiusY, Color color) {
        this.vX = (int) DEFAULT_VX;
        this.vY = (int) DEFAULT_VY;
        this.color = color;
        this.ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
    }

    public Ball() {
        this(Game.DEFAULT_WIDTH / 2 - DEFAULT_RADIUS, Game.DEFAULT_HEIGHT / 2 + Paddle.DEFAULT_WIDTH, DEFAULT_RADIUS, DEFAULT_RADIUS, DEFAULT_COLOR);
    }


    public Ball(int x, int y, int radius, Color color) {
        this(x, y, radius, radius, color);
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    public void setEllipse(Ellipse ellipse) {
        this.ellipse = ellipse;
    }

    public int getvX() {
        return vX;
    }

    public void setvX(int vX) {
        this.vX = vX;
    }

    public int getvY() {
        return vY;
    }

    public void setvY(int vY) {
        if (vY == 0) this.vY = -3;
        else this.vY = vY;
    }

    public int getRadius() {
        return (int) this.ellipse.getRadiusX();
    }

    public void setRadius(int radius) {
//        this.radius = radius;
        this.ellipse.setRadiusX(radius);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Coordinate getCoordinate() {
//        return coordinate;
        return null;
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("radius", this.ellipse.getRadiusX());

        JSONObject coordinate = new JSONObject();
        coordinate.put("x", this.ellipse.getCenterX());
        coordinate.put("y", this.ellipse.getCenterY());
        j.put("coordinate", coordinate);

        JSONObject jsonColor = new JSONObject();
        jsonColor.put("r", getColor().getRed());
        jsonColor.put("g", getColor().getGreen());
        jsonColor.put("b", getColor().getBlue());

        j.put("color", jsonColor);
        return j;
    }

    public void move() {
        if (getvX() == 0) {
            setvX((int) Math.round(Math.random() * 2 - 1));
        }
        if (getvY() == 0) {
            setvY((int) Math.round(Math.random() * 2 - 1));
        }
        this.ellipse.setCenterX(this.ellipse.getCenterX() + getvX());
        this.ellipse.setCenterY(this.ellipse.getCenterY() + getvY());
    }

    public void increaseSpeed() {
        if (Math.random() < 0.9) { // FIXME WHY 10%??
            return;
        }
        this.vX = Integer.signum(this.vX) * Math.abs(--this.vX);
        this.vY = Integer.signum(this.vY) * Math.abs(--this.vY);
    }
}
