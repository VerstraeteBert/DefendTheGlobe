package data;

import Components.Brick;
import org.json.simple.JSONArray;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BreakoutRepository {

    boolean login(String username, String password);

    boolean register(String email, String username, String password);

    JSONArray getHighscores();

    int[] getDefaultLengthAndWithBrick();

    List<Brick> getBricksWithoutCoordinates();

    Map<String, Integer> getEffectPropertiesById(int id);

    void setHighscore(String name, String type, int score, Date date);

    Map<String, String> getMoeilijkheidPropertiesByGraad(String moeilijkheidsgraad);
}
