package Components;

import javafx.scene.shape.Arc;
import javafx.scene.shape.Shape;
import org.json.simple.JSONObject;

public class CurvedBrick extends Brick {

    private Arc outerArc;
    private Arc innerArc;

    private Shape shape;

    public CurvedBrick(Arc outerArc, Arc innerArc) {
        this.outerArc = outerArc;
        this.innerArc = innerArc;
        this.shape = Shape.subtract(outerArc, innerArc);
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

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
