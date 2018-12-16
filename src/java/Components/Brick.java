package Components;

import Powers.Effect;
import javafx.scene.shape.Rectangle;
import org.json.simple.JSONObject;

import java.awt.*;

public class Brick {

    public static final Color DEFAULT_COLOR = Color.GREEN;
    public static final Effect DEFAULT_EFFECT = null;
    public static final int DEFAULT_HEIGHT = 200;
    public static final int DEFAULT_WIDTH = 500;

    private Rectangle rectangle;

    private Color color;
    private Effect effect;

    public Brick(double x, double y, double width, double height, Color color, Effect effect) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.color = color;
        this.effect = effect;
    }

    public Brick() {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLOR, DEFAULT_EFFECT);
    }

    public Brick(Coordinate coordinate, int dx, int dy, Color color) {
        this(coordinate.getX(), coordinate.getY(), dx, dy, color, DEFAULT_EFFECT);
    }

    public Brick(Coordinate coordinate, int dx, int dy, Color color, Effect effect) {
        this(coordinate.getX(), coordinate.getY(), dx, dy, color, effect);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

    public int getTopY() {
        return (int) getRectangle().getY();
    }

    public int getBottomY() {
        return getTopY() + (int) getRectangle().getHeight();
    }

    public int getLeftX() {
        return (int) getRectangle().getX();
    }

    public int getRightX() {
        return getLeftX() + (int) getRectangle().getWidth();
    }

    public Color getColor() {
        return color;
    }


    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("x", getRectangle().getX());
        json.put("y", getRectangle().getY());
        json.put("dx", getRectangle().getWidth());
        json.put("dy", getRectangle().getHeight());
        JSONObject j = new JSONObject();
        j.put("r", getColor().getRed());
        j.put("g", getColor().getGreen());
        j.put("b", getColor().getBlue());
        json.put("color", j);
        if (getEffect() != null) json.put("power", getEffect().toJson());

        return json;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
