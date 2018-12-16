package Components;

import org.json.simple.JSONObject;

public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        j.put("x", x);
        j.put("y", y);
        return j;
    }
}
