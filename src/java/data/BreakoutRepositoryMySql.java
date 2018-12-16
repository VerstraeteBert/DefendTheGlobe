package data;

import Components.Brick;
import Powers.Effect;
import Powers.EffectFactory;
import com.mysql.jdbc.MySQLConnection;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import data.util.BCrypt;
import data.util.MySqlConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import util.BreakoutException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BreakoutRepositoryMySql implements BreakoutRepository {

    private static final String SQL_LOGIN_USER = "SELECT * FROM users WHERE username = ?";
    private static final String SQL_REGISTER_USER = "INSERT INTO users (username, email, hashword) VALUES(?, ?, ?)";
    private static final String SQL_GET_DEFAULT_LENGTH_AND_WIDTH_BRICK = "SELECT lengte, breedte FROM brick WHERE id = 1";
    private static final String SQL_GET_BRICKS = "SELECT lengte, breedte FROM brick";
    private static final String SQL_GET_EFFECT_BY_ID = "SELECT * FROM effects WHERE type = ?";
    private static final String SQL_GET_MOEILIJKHEIDSGRAAD_BY_MOEILIJKHEID = "SELECT * FROM moeilijkheidsgraad WHERE moeilijkheidsgraad = ?";
    private static final String SQL_GET_HIGHSCORES = "SELECT * FROM highscores hs" +
            " JOIN users_highscores uh on uh.highscore_id = hs.id" +
            " JOIN users u on u.id = uh.user_id";
    private static final String SLQ_CREATE_USER = "INSERT INTO users(username, email, hashword) VALUES(?, '', '')";
    private static final String SQL_ADD_HIGHSCORE = "INSERT INTO highscores(type, score, datum) VALUES(?, ?, ?)";
    private static final String SQL_ADD_USER_HIGHSCORE = "INSERT INTO users_highscores() VALUES(?, ?)";

    public boolean login(String username, String password) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_LOGIN_USER)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                if (rs.getString("hashword") == null) {
                    return false;
                } else {
                    String hashedPW = rs.getString("hashword");
                    return BCrypt.checkpw(password, hashedPW);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.toString());
            return false;
        }

    }

    public boolean register(String email, String username, String password) {
        try (Connection con = MySqlConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_REGISTER_USER)) {

            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashed);

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int[] getDefaultLengthAndWithBrick() {
        try (Connection conn = MySqlConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_GET_DEFAULT_LENGTH_AND_WIDTH_BRICK)) {
            if (!rs.next()) {
                throw new BreakoutException("Empty result!");
            }
            return new int[]{rs.getInt("lengte"), rs.getInt("breedte")};
        } catch (SQLException e) {
            throw new BreakoutException("Couldn't get brick!", e);
        }
    }

    public List<Brick> getBricksWithoutCoordinates() {
        List<Brick> bricks = new ArrayList<>();
        try (Connection conn = MySqlConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_GET_BRICKS)) {
            while (rs.next()) {
                bricks.add(new Brick(0, 0, rs.getInt("lengte"), rs.getInt("breedte"), Brick.DEFAULT_COLOR, Brick.DEFAULT_EFFECT));
            }
        } catch (SQLException e) {
            throw new BreakoutException("Couldn't get bricks!", e);
        }
        return bricks;
    }

    @Override
    public Map<String, Integer> getEffectPropertiesById(int id) {
        Map<String, Integer> effectProperties = new HashMap<>();
        try (Connection conn = MySqlConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_GET_EFFECT_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new BreakoutException("Empty result!");
                }
                effectProperties.put("radius", rs.getInt("radius"));
                effectProperties.put("effecttime", rs.getInt("effecttime"));
                effectProperties.put("isPowerup", rs.getInt("isPowerup"));
            }
        } catch (SQLException e) {
            throw new BreakoutException("Couldn't get effects!");
        }
        return effectProperties;
    }
    public Map<String, String> getMoeilijkheidPropertiesByGraad(String moeilijkheidsgraad){
        Map<String, String> moeilijkheidsProps = new HashMap<>();
        try(Connection conn = MySqlConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_GET_MOEILIJKHEIDSGRAAD_BY_MOEILIJKHEID)) {

            stmt.setString(1, moeilijkheidsgraad);

            try(ResultSet rs = stmt.executeQuery()){
                rs.next();
                moeilijkheidsProps.put("moeilijkheidsgraad", rs.getString("moeilijkheidsgraad"));
                moeilijkheidsProps.put("aantal_blokjes", rs.getString("aantal_blokjes"));
                moeilijkheidsProps.put("snelheid_bal", rs.getString("snelheid_bal"));
                moeilijkheidsProps.put("breedte_paddle", rs.getString("breedte_paddle"));
                return moeilijkheidsProps;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public JSONArray getHighscores() {
        JSONArray allHighscores = new JSONArray();
        try (Connection conn = MySqlConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_GET_HIGHSCORES)) {

            while(rs.next()) {
                String type = rs.getString("Type");
                String name = rs.getString("username");
                Date date = rs.getDate("Datum");
                int score = rs.getInt("Score");
                JSONObject highscore = new JSONObject();

//                System.out.println(type + name + date + score);

                highscore.put("type", type);
                highscore.put("name", name);
                highscore.put("date", date.toString());
                highscore.put("score", score);

                allHighscores.add(highscore);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            throw new BreakoutException("something went wrong");
        }
        return allHighscores;
    }

    @Override
    public void setHighscore(String name, String type, int score, java.util.Date date) {
        try (Connection conn = MySqlConnection.getConnection();
        PreparedStatement addUser = conn.prepareStatement(SLQ_CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            addUser.setString(1, name);
            addUser.executeUpdate();
            try (ResultSet addUserGeneratedKeys = addUser.getGeneratedKeys()) {
                addUserGeneratedKeys.next();
                int userid = addUserGeneratedKeys.getInt(1);
                try (PreparedStatement addHighscore = conn.prepareStatement(SQL_ADD_HIGHSCORE, Statement.RETURN_GENERATED_KEYS)) {
                    addHighscore.setString(1, type);
                    addHighscore.setInt(2, score);
                    addHighscore.setDate(3, new Date(date.getTime()));
                    addHighscore.executeUpdate();
                    try (ResultSet addHighscoreGeneratedKeys = addHighscore.getGeneratedKeys()) {
                        addHighscoreGeneratedKeys.next();
                        int highscoreId = addHighscoreGeneratedKeys.getInt(1);
                        try (PreparedStatement addUserHighscore = conn.prepareStatement(SQL_ADD_USER_HIGHSCORE)) {
                            addUserHighscore.setInt(1, userid);
                            addUserHighscore.setInt(2, highscoreId);
                            addUserHighscore.executeUpdate();

                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new BreakoutException("Couldn't add user", e);
        }
    }

    public static void main(String[] args) {
        new BreakoutRepositoryMySql().setHighscore("flupke", "Arcade", 123456, Calendar.getInstance().getTime());
    }
}
