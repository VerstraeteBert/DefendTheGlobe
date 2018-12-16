package Components;

import Powers.Effect;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;

    private int score;
    private int lives;
    private int bonus;
    private double paddleY;

    //FIXME PLAIY HEEFT DIT TOEGEVOEGD
    private List<Effect> effects = new ArrayList<>(); //LELIJK FIX DIT

    public Player() {
        this("");
    }

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.lives = 3;
        this.bonus = 0;
    }

    public Player(String name, double paddleY) {
        this.name = name;
        this.score = 0;
        this.lives = 3;
        this.bonus = 0;
        this.paddleY = paddleY;
    }

    public int increaseScore() {
        this.score = this.score + 1 + bonus;
        return this.score;
    }

    public void addEffect(Effect e) {
        //if (effects == null) effects = new ArrayList<>();
        effects.add(e);
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void decreaseLives() {
        this.lives--;
    }

    public void increaseLives() {
        this.increaseLives(1);
    }

    public void increaseLives(int amount) {
        this.lives += amount;
    }

    public int increaseBonus() {
        return ++bonus;
    }

    public int getScore() {
        return this.score;
    }

    public int getLives() {
        return this.lives;
    }

    public int getBonus() {
        return this.bonus;
    }

    public String getName() {
        return name;
    }

    public double getPaddleY() {
        return paddleY;
    }

    public JSONObject toJson() {
        JSONObject jsonPlayer = new JSONObject();
        jsonPlayer.put("lives", getLives());
        jsonPlayer.put("score", getScore());
        jsonPlayer.put("bonus", getBonus());
        //FIXME PLAIY HEEFT DIT GESCHREVEN
        if (effects.size() > 0) {
            JSONArray jsonEffects = new JSONArray();
            for (Effect e : effects) {
                jsonEffects.add(e.toJson());
            }
            jsonPlayer.put("powers", jsonEffects);
        }
        jsonPlayer.put("name", getName());
        return jsonPlayer;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
