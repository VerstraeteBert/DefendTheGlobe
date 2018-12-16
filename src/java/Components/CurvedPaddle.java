package Components;

import javafx.scene.shape.Arc;
import javafx.scene.shape.Shape;
import org.json.simple.JSONObject;

public class CurvedPaddle extends Paddle {

    private Arc outerArc;
    private Arc innerArc;

    private Shape shape;

    private int quadrant;

    public CurvedPaddle() {
        this(new Arc(250, 250, 55, 55, 0, 90), new Arc(250, 250, 45, 45, 0, 90));
    }

    public CurvedPaddle(Arc outerArc, Arc innerArc) {
        this.outerArc = outerArc;
        this.innerArc = innerArc;
        this.shape = Shape.subtract(outerArc, innerArc);
        this.quadrant = 4;
    }

    @Override
    public void moveLeft() {
        outerArc.setStartAngle(outerArc.getStartAngle() - 360 / 20);
        innerArc.setStartAngle(innerArc.getStartAngle() - 360 / 20);
        shape = Shape.subtract(outerArc, innerArc);
        if (quadrant == 4) {
            quadrant = 1;
        } else {
            quadrant++;
        }
    }

    @Override
    public void moveRight() {
        outerArc.setStartAngle(outerArc.getStartAngle() + 360 / 20);
        innerArc.setStartAngle(innerArc.getStartAngle() + 360 / 20);
        shape = Shape.subtract(outerArc, innerArc);
        if (quadrant == 1) {
            quadrant = 4;
        } else {
            quadrant--;
        }
    }

    public int getQuadrant() {
        return quadrant;
    }

    public Arc getOuterArc() {
        return outerArc;
    }

    public Arc getInnerArc() {
        return innerArc;
    }

    public Shape getShape() {
        return shape;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", getOuterArc().getCenterX());
        jsonObject.put("y", getOuterArc().getCenterY());
        jsonObject.put("radius", getOuterArc().getRadiusX());
        jsonObject.put("start", Math.toRadians(getOuterArc().getStartAngle()));
        jsonObject.put("extent", Math.toRadians(getOuterArc().getLength()));
        return jsonObject;
    }
}
