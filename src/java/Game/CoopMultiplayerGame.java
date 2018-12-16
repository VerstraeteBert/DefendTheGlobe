package Game;

import Components.Ball;
import Components.Paddle;
import Components.Player;
import Social.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class CoopMultiplayerGame extends Game {

    private List<User> users;

    private List<Player> players;
    private List<Ball> balls;
    private List<Paddle> paddles;
    private Map<Player, Paddle> playerPaddles;

    public CoopMultiplayerGame(User... users) {
        this.users = new ArrayList<>();
        this.players = new ArrayList<>();
        this.users.addAll(Arrays.asList(users));
        this.balls = new ArrayList<>();
        this.paddles = new ArrayList<>();
    }

    protected void setUpPlayers() {
        users.forEach(user -> players.add(new Player(user.getName())));
    }

    protected void setupPaddles() {
        paddles.add(new Paddle());
        paddles.add(new Paddle());
        this.playerPaddles = new HashMap<>(); // FIXME BADLY : DEPENDING ON ORDER, SHOULDNT RELY ON THAT
        playerPaddles.put(players.get(0), paddles.get(0));
        playerPaddles.put(players.get(1), paddles.get(1));
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();

        JSONArray jsonBalls = new JSONArray();
        this.balls.forEach(ball -> jsonBalls.add(ball.toJson()));
        j.put("balls", jsonBalls);

        JSONArray jsonPaddles = new JSONArray();
        this.paddles.forEach(paddle -> {
            jsonPaddles.add(paddle.toJson());
        });
        j.put("paddles", jsonPaddles);

        JSONArray jsonPlayers = new JSONArray();
        this.players.forEach(player -> jsonPlayers.add(player.toJson()));
        j.put("players", jsonPlayers);

        JSONArray jsonBricks = new JSONArray();
        super.getBricks().forEach(brick -> jsonBricks.add(brick.toJson()));
        j.put("bricks", jsonBricks);

        return j;
    }

    @Override
    public void moveBall() {
        moveBalls();
    }
    
    public void moveBalls() {
        getBalls().forEach(Ball::move);
    }

    @Override
    public void movePaddle(String action, String nameOfPlayerAskingUpdate) {
        playerPaddles.forEach((player, paddle) -> {
            if (player.getName().equals(nameOfPlayerAskingUpdate)) {
                switch (action) {
                    case "left":
                        paddle.moveLeft();
                        break;
                    case "right":
                        paddle.moveRight();
                        break;
                }
            }
        });

    }
    @Override
    protected boolean canvasContact(Ball ball) {
        if (bottomCanvasContact(ball)) {
            newBall(ball);
            // TODO GRAFULLY HANDLE DEATH
            if (getPointGainingPlayer() != null) {
                decreasePlayerLives();
                if (getPointGainingPlayer().getLives() == 0) {
                    setBricks(new ArrayList<>());

                    ball.setvX(0);
                    ball.setvY(0);

                    playerPaddles.replace(getPointGainingPlayer(), null);
                }
            }
            return true;
        }
        if (topCanvasContact(ball)) {
            ball.setvY(-ball.getvY());
            return true;
        }
        if (sideCanvasContact(ball)) {
            ball.setvX(-ball.getvX());
            return true;
        }
        return false;
    }
    @Override
    public void collision(Ball ball) {
        if (canvasContact(ball)) {
            return;
        }
        if (brickContact(ball)) {
//            player.increaseScore();
            return;
        }
        this.paddles.forEach(paddle -> {
            if (paddleContact(ball, paddle)) {
                increaseBallSpeed();
                return;
            }
        });
    }

    @Override
    public void increaseBallSpeed() {
        this.balls.forEach(Ball::increaseSpeed);
    }

//    @Override
//    protected Player getPlayers() {
//        return this.players.get(0);
//    }

    @Override
    protected void newBall(Ball previousBall) {
        this.balls.remove(previousBall);
        this.balls.add(new Ball());
    }

    @Override
    public void updateGame() {
        getBalls().forEach(this::collision);
    }

    public static void main(String[] args) {

    }
}
