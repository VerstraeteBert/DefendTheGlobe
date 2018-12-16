package Game;

import Components.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefendTheGlobe extends Game {
    
    public static final int DEFAULT_BRICK_WIDTH = 25;
    public static final int DEFAULT_BRICK_HEIGHT = 50;

    private Ball theGlobe;

    public DefendTheGlobe() {

    }

    public void setup(List<String> players) {
        setupBricks(DEFAULT_ROW_COUNT);
        setupPaddles(players);
        setUpPlayers(players);
        setupBalls();
        setEffects(new ArrayList<>());
        setPointGainingPlayer(getPlayers().get(0));
    }

    @Override
    public void setupBalls() {
        List<Ball> balls = new CopyOnWriteArrayList<>();
        theGlobe = new Ball(250, 250, 40, 40, Color.BLACK);
        theGlobe.setvX(0);
        theGlobe.setvY(0);
        Ball ball = new Ball(250, 180, 5, 5, Color.RED);
        ball.setvX(-5);
        ball.setvY(-5);
        balls.add(ball);
        setBalls(balls);
    }

    @Override
    protected void setupPaddles(List<String> players) {
        List<Paddle> paddles = new ArrayList<>();
        paddles.add(new CurvedPaddle());
        setPaddles(paddles);
    }

    @Override
    public void setUpPlayers(List<String> users) {
        List<Player> players = new ArrayList<>();
        users.forEach(user -> players.add(new Player(user)));
        setPlayers(players);
    }

    @Override
    public void setupBricks(int rows) {
        List<Brick> bricks = new ArrayList<>(50);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < 6 + j; i++) {
                bricks.add(new Brick(new Coordinate(100 + (i * DEFAULT_BRICK_HEIGHT) - (j * DEFAULT_BRICK_WIDTH), 100 - (j * DEFAULT_BRICK_WIDTH)), DEFAULT_BRICK_HEIGHT, DEFAULT_BRICK_WIDTH, Color.GREEN));
                bricks.add(new Brick(new Coordinate(100 - DEFAULT_BRICK_WIDTH - j * DEFAULT_BRICK_WIDTH, 100 + (i * DEFAULT_BRICK_HEIGHT) - (j * DEFAULT_BRICK_WIDTH)), DEFAULT_BRICK_WIDTH, DEFAULT_BRICK_HEIGHT, Color.BLUE));
                bricks.add(new Brick(new Coordinate(100 + i * DEFAULT_BRICK_HEIGHT - (j * DEFAULT_BRICK_WIDTH), 400 - DEFAULT_BRICK_WIDTH + (j * DEFAULT_BRICK_WIDTH)), DEFAULT_BRICK_HEIGHT, DEFAULT_BRICK_WIDTH, Color.RED));
                bricks.add(new Brick(new Coordinate(400 + j * DEFAULT_BRICK_WIDTH, 100 + i * DEFAULT_BRICK_HEIGHT - (j * DEFAULT_BRICK_WIDTH)), DEFAULT_BRICK_WIDTH, DEFAULT_BRICK_HEIGHT, Color.YELLOW));
            }
        }
        setBricks(bricks);
    }

    @Override
    public void collision(Ball ball) {
        super.collision(ball);
        if (globeContact(ball)) {
            setupBalls();
            decreasePlayerLives();
            return;
        }
    }

    @Override
    protected boolean canvasContact(Ball ball) {
        if (bottomCanvasContact(ball) || topCanvasContact(ball)) {
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
    protected boolean paddleContact(Ball ball, Paddle paddle) {
        CurvedPaddle curvedPaddle = (CurvedPaddle) paddle;
        double distance = Math.sqrt(Math.pow(ball.getEllipse().getCenterX() - theGlobe.getEllipse().getCenterX(), 2) + Math.pow(ball.getEllipse().getCenterY() - theGlobe.getEllipse().getCenterY(), 2));

        if (distance < ball.getEllipse().getRadiusX() + curvedPaddle.getOuterArc().getRadiusX() && distance > ball.getEllipse().getRadiusX() + theGlobe.getEllipse().getRadiusX()) {
            if (ball.getEllipse().getBoundsInLocal().getMinX() >= curvedPaddle.getShape().getBoundsInLocal().getMinX() && ball.getEllipse().getBoundsInLocal().getMaxX() <= curvedPaddle.getShape().getBoundsInLocal().getMaxX()) {
                determineResultingVelocity(ball);
                return true;
            }
        }
        return false;
    }

    private void determineResultingVelocity(Ball ball) {
        if (ball.getEllipse().getCenterX() < Game.DEFAULT_WIDTH / 2) {
            if (ball.getEllipse().getCenterY() < Game.DEFAULT_HEIGHT / 2) {
                ball.setvY(- (int) (Math.random() * 4 + 2));
                ball.setvX(- (int) (Math.random() * 4 + 2));
            } else {
                ball.setvY(  (int) (Math.random() * 4 + 2));
                ball.setvX(- (int) (Math.random() * 4 + 2));
            }
        } else {
            if (ball.getEllipse().getCenterY() < Game.DEFAULT_HEIGHT / 2) {
                ball.setvY(- (int) (Math.random() * 4 + 2));
                ball.setvX(  (int) (Math.random() * 4 + 2));
            } else {
                ball.setvY(  (int) (Math.random() * 4 + 2));
                ball.setvX(  (int) (Math.random() * 4 + 2));
            }
        }
    }

    @Override
    protected void determineNewVelocity(Ball ball, Brick b) {
        if (Math.random() > 0.65) {
            double distanceX = ball.getEllipse().getCenterX() - Game.DEFAULT_WIDTH / 2;
            double distanceY = ball.getEllipse().getCenterY() - Game.DEFAULT_HEIGHT / 2;
            ball.setvX(-(int) (distanceX / 25));
            ball.setvY(-(int) (distanceY / 25));
        } else {
            super.determineNewVelocity(ball, b);
        }
    }

    private boolean globeContact(Ball ball) {
        double distance = Math.sqrt(Math.pow(ball.getEllipse().getCenterX() - theGlobe.getEllipse().getCenterX(), 2) + Math.pow(ball.getEllipse().getCenterY() - theGlobe.getEllipse().getCenterY(), 2));
        return distance < ball.getEllipse().getRadiusX() + theGlobe.getEllipse().getRadiusX();
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        if (getBalls() != null) {
            JSONArray balls = new JSONArray();
            getBalls().forEach(ball -> balls.add(ball.toJson()));
            jsonObject.put("balls", balls);
        }

        jsonObject.put("theGlobe", theGlobe.toJson());
        if (getPaddles() != null) {
            JSONArray paddlesArray = new JSONArray();
            getPaddles().forEach(paddle -> paddlesArray.add(paddle.toJson()));
            jsonObject.put("paddles", paddlesArray);
        }

        if (getBricks() != null) {
            JSONArray jsonBricks = new JSONArray();
            getBricks().forEach((brick -> jsonBricks.add(brick.toJson())));
            jsonObject.put("bricks", jsonBricks);
        }

        if (getPlayers() != null) {
            JSONArray playersArray = new JSONArray();
            getPlayers().forEach(player -> playersArray.add(player.toJson()));
            jsonObject.put("players", playersArray);
        }

        return jsonObject;
    }
}
