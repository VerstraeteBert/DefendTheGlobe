package Game;

import Components.*;
import Powers.Effect;
import Powers.EffectFactory;
import Social.User;
import data.Repositories;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.BreakoutException;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;
    public static final int DEFAULT_ROW_COUNT = 5;

    public static final int DEFAULT_BRICK_WIDTH = DEFAULT_WIDTH / 10;
    public static final int DEFAULT_BRICK_HEIGHT = DEFAULT_BRICK_WIDTH / 2;

    private List<User> users;

    private int numberOfBricks;
    private int speedBall;
    private int widthPaddle;
    private String difficulty;
    private List<Brick> bricks = new ArrayList<>();
    private List<Brick> availableBricks = new ArrayList<>();
    private List<Ball> balls;
    private List<Paddle> paddles;
    private List<Player> players;
    private List<Effect> effects;
    private Paddle ceiling;
    private Player pointGainingPlayer;


    public Game() {
    }

    public Game(String difficulty) {
        this.difficulty = difficulty;
    }

    public Game(User... users) {
        this.users = Arrays.asList(users);
    }

    //    public void setup(List<String> players) {
//
//        setupBricks(DEFAULT_ROW_COUNT);
//        setupPaddles(players);
//        setUpPlayers(players);
//        setupBalls();
//    }
    public void setup(List<String> players) {
        if (difficulty != null) {
            Map<String, String> moeilijkheid = Repositories.getMysqlBreakoutRepository().getMoeilijkheidPropertiesByGraad(this.difficulty);
            this.numberOfBricks = Integer.parseInt(moeilijkheid.get("aantal_blokjes"));
            this.speedBall = Integer.parseInt(moeilijkheid.get("snelheid_bal"));
            this.widthPaddle = Integer.parseInt(moeilijkheid.get("breedte_paddle"));

        }
        setupBricks(numberOfBricks / 10); //numberOfBricks / 10
        setupPaddles(players);
        setUpPlayers(players);
        setupBalls();
    }

    protected void setUpPlayers(List<String> users) {
        players = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            players.add(new Player(users.get(i), getPaddles().get(i).getRectangle().getY()));
        }
        pointGainingPlayer = this.players.get(players.size() - 1);
    }

    //integer amount aan onderstaande functie toevoegen?
    public void setupBalls() {
        balls = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 1; i++) {
            Ball ball = new Ball();
            ball.setvX(-getSpeedBall());
            ball.setvY(-getSpeedBall());
            balls.add(ball);

        }
    }

    private int getwidthPaddle() {
        if (this.widthPaddle == 0) {
            return 100;
        }
        return this.widthPaddle;
    }

    private int getSpeedBall() {
        if (this.speedBall == 0) {
            return -5;
        }
        return this.speedBall;
    }

    protected void setupPaddles(List<String> players) {
        paddles = new ArrayList<>();
        int spacing = 0;
        for (int i = 1; i <= players.size(); i++) {
            if (i != 1) spacing = 7;
            Paddle paddle = new Paddle();
            paddle.getRectangle().setWidth(getwidthPaddle());
            paddle.getRectangle().setY(paddle.getRectangle().getY() - i * 5 - spacing);
            paddles.add(paddle);
        }
    }

    public JSONObject toJson() {
        JSONObject j = new JSONObject();
        if (getBalls() != null) {
            JSONArray ballArray = new JSONArray();
            getBalls().forEach(ball -> ballArray.add(ball.toJson()));
            j.put("balls", ballArray);
        }
        if (getPaddles() != null) {
            JSONArray paddleArray = new JSONArray();
            getPaddles().forEach(paddle -> {
                if (paddle.isShowing()) {
                    paddleArray.add(paddle.toJson());
                }
            });
            j.put("paddles", paddleArray);
        }
        if (getPlayers() != null) {
            JSONArray playerArray = new JSONArray();
            getPlayers().forEach(player -> playerArray.add(player.toJson()));
            j.put("players", playerArray);
        }
        if (getBricks() != null) {
            JSONArray jsonBricks = new JSONArray();
            getBricks().forEach(brick -> jsonBricks.add(brick.toJson()));
            j.put("bricks", jsonBricks);
        }
        if (getEffects() != null) {
            JSONArray jsonEffects = new JSONArray();
            getEffects().forEach(effect -> {
                jsonEffects.add(effect.toJson());
            });
            j.put("effects", jsonEffects);
        }
        if (ceiling != null) j.put("ceiling", ceiling.toJson());
        return j;
    }

    public void setupBricks(int rows) {
        if (availableBricks.size() == 0) {
            try {
                availableBricks = Repositories.getMysqlBreakoutRepository().getBricksWithoutCoordinates();
            } catch (BreakoutException e) {
                availableBricks = Collections.singletonList(new Brick(0, 0, DEFAULT_BRICK_WIDTH, DEFAULT_BRICK_HEIGHT, Brick.DEFAULT_COLOR, Brick.DEFAULT_EFFECT));
            }
        }
        Random rand = new Random();
        EffectFactory ef = new EffectFactory();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 10; j++) {
                int r = rand.nextInt(256);
                int g = rand.nextInt(256);
                int b = rand.nextInt(256);

                Brick currBrick = availableBricks.get(rand.nextInt(availableBricks.size()));
                Brick brick = new Brick(currBrick.getRectangle().getX(), currBrick.getRectangle().getY(), currBrick.getRectangle().getWidth(), currBrick.getRectangle().getHeight(), new Color(r, g, b), null);

                double diff = DEFAULT_BRICK_WIDTH - brick.getRectangle().getWidth();

                brick.getRectangle().setX(j * DEFAULT_BRICK_WIDTH);
                brick.getRectangle().setY(DEFAULT_HEIGHT / 2 - i * DEFAULT_BRICK_HEIGHT);
                brick.setColor(new Color(r, g, b));

                if (Math.random() > 0.5) {
                    Effect e = ef.createEffect(rand.nextInt(6) + 1, j * DEFAULT_BRICK_WIDTH + DEFAULT_BRICK_WIDTH / 2, DEFAULT_HEIGHT / 2 - i * DEFAULT_BRICK_HEIGHT + 12, 5, 5);
                    brick.setEffect(e);
                }
                bricks.add(brick);
                if (diff != 0) {
                    bricks.add(new Brick(j * DEFAULT_BRICK_WIDTH + brick.getRectangle().getWidth(), DEFAULT_HEIGHT / 2 - i * DEFAULT_BRICK_HEIGHT, diff, DEFAULT_BRICK_HEIGHT, new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)), null));
                }
            }
        }
        effects = new CopyOnWriteArrayList<>();
    }

    private void increasePlayerScore() {
        if (pointGainingPlayer != null) pointGainingPlayer.increaseScore();
    }

    public void collision(Ball ball) {
        if (canvasContact(ball)) return;
        getPaddles().forEach(paddle -> {
            if (paddleContact(ball, paddle)) {
                increaseBallSpeed();
                getPlayers().forEach(player -> {
                    if (player.getPaddleY() == paddle.getRectangle().getY()) pointGainingPlayer = player;
                });
                return;
            }
        });
        if (brickContact(ball)) {
            increasePlayerScore();
            return;
        }
        if (getCeiling() != null) {
            if (paddleContact(ball, getCeiling())) {
                return;
            }

        }
    }

    protected void newBall(Ball previousBall) {
        getBalls().remove(previousBall);
        Ball ball = new Ball(getPaddles().get(0).getRectangle().getX() + getPaddles().get(0).getRectangle().getWidth() / 2, getPaddles().get(0).getRectangle().getY() - Ball.DEFAULT_RADIUS, Ball.DEFAULT_RADIUS, Ball.DEFAULT_RADIUS, Ball.DEFAULT_COLOR);
        ball.setvX(-getSpeedBall());
        ball.setvY(-getSpeedBall());
        getBalls().add(ball);
    }

    protected boolean canvasContact(Ball ball) {
        if (bottomCanvasContact(ball)) {
            newBall(ball);
            // TODO GRAFULLY HANDLE DEATH
            if (pointGainingPlayer != null) {
                decreasePlayerLives();
                if (pointGainingPlayer.getLives() == 0 && getPlayers().size() == 1) {
                    ball.setvX(0);
                    ball.setvY(0);
                }
                if (pointGainingPlayer.getLives() == 0 && getPlayers().size() > 1) {
                    getPaddles().removeIf((paddle -> paddle.getRectangle().getY() == pointGainingPlayer.getPaddleY()));
                }
            }
            return true;
        }
        if (topCanvasContact(ball)) {
            ball.setvY(-ball.getvY());
            ball.getEllipse().setCenterY(0 + ball.getEllipse().getRadiusY());
            return true;
        }
        if (sideCanvasContact(ball)) {
            if (ball.getEllipse().getCenterX() < DEFAULT_WIDTH / 2) {
                ball.getEllipse().setCenterX(0 + ball.getEllipse().getRadiusX());
            } else {
                ball.getEllipse().setCenterX(DEFAULT_WIDTH - ball.getEllipse().getRadiusX());
            }
            ball.setvX(-ball.getvX());
            return true;
        }
        return false;
    }

    protected void decreasePlayerLives() {
        if (pointGainingPlayer != null) pointGainingPlayer.decreaseLives();
    }

    protected boolean canvasContact(Effect e) {
        if (bottomCanvasContact(e)) {
            effects.remove(e);
            return true;
        }
        return false;
    }

    public boolean brickContact(Ball ball) {
        for (Brick b : getBricks()) {
            if (ball.getEllipse().intersects(b.getRectangle().getBoundsInLocal())) {
                determineNewVelocity(ball, b);
                brickIsHit(b);
                getBricks().remove(b);
                return true;
            }
        }
        return false;
    }

    public void brickContact(Ball ball, boolean idc) {
        bricks.removeIf((brick) -> ball.getEllipse().intersects(brick.getRectangle().getBoundsInLocal()));
    }

    protected void determineNewVelocity(Ball ball, Brick b) {
        if (sideBrickContact(ball, b)) {
            ball.setvX(-ball.getvX());
        } else {
            ball.setvY(-ball.getvY());
        }
    }

    protected boolean paddleContact(Ball ball, Paddle paddle) {
        if (paddle.getRectangle().getY() == 300 && ball.getEllipse().intersects(paddle.getRectangle().getBoundsInLocal())) {
            ball.setvY(-ball.getvY());
            ball.getEllipse().setCenterY(paddle.getRectangle().getY() + ball.getEllipse().getRadiusY() + 1);
            return true;
        }
        if (ball.getEllipse().intersects(paddle.getRectangle().getBoundsInLocal())) {
            int ballX = (int) ball.getEllipse().getCenterX();
            int leftPaddleX = paddle.getLeftX();
            int rightPaddleX = paddle.getRightX();
            int paddleDX = (int) paddle.getRectangle().getWidth();
            int stukje = paddleDX / 10;
            int angle = -10;
            for (int i = leftPaddleX; i < rightPaddleX; i += stukje) {
                if (i <= ballX && ballX <= i + stukje) {
                    ball.setvX(angle);
                } else {
                    angle += 2;
                }
            }
            ball.setvY(-ball.getvY());
            ball.getEllipse().setCenterY(paddle.getRectangle().getY() - ball.getEllipse().getRadiusY() - 1);

            return true;
        }
        return false;
    }

    public boolean paddleContact(Effect e, Paddle paddle, Game game) {
        if (e.getType().equalsIgnoreCase("powerup")) {

            if (e.getEllipse().intersects(paddle.getRectangle().getBoundsInLocal())) {
                activateEffect(e, game);
                //powerup geactiveerd

                return true;
            }
            if (isEffectBelowPaddle(e, paddle)) {
                effects.remove(e);
                return true;
            }
        }
        if (e.getType().equalsIgnoreCase("powerdown")) {
            if (e.getEllipse().intersects(paddle.getRectangle().getBoundsInLocal())) {
                effects.remove(e);
                return true;
            }
            if (isEffectBelowPaddle(e, paddle)) {
                activateEffect(e, game);
                return true;
                //powerdown geactiveerd
            }
        }
        return false;
    }

    private void activateEffect(Effect e, Game game) {
        effects.remove(e);
        e.activate(game);
    }

    private boolean isEffectBelowPaddle(Effect e, Paddle paddle) {
        return e.getEllipse().getCenterY() > paddle.getTopY() + 10;
    }

    protected boolean bottomCanvasContact(Ball ball) {
        return ball.getEllipse().getCenterY() + ball.getRadius() / 2 >= DEFAULT_HEIGHT;
    }

    protected boolean topCanvasContact(Ball ball) {
        return ball.getEllipse().getCenterY() - ball.getRadius() / 2 <= 0;
    }

    protected boolean sideCanvasContact(Ball ball) {
        return ball.getEllipse().getCenterX() - ball.getRadius() / 2 <= 0 || ball.getEllipse().getCenterX() + ball.getEllipse().getRadiusX() / 2 >= DEFAULT_WIDTH;
    }

    private boolean sideBrickContact(Ball ball, Brick brick) {
        return ball.getEllipse().getCenterX() < brick.getRectangle().getX() || ball.getEllipse().getCenterX() > brick.getRectangle().getX() + brick.getRectangle().getWidth();
    }

    public void brickIsHit(Brick brick) {
        if (brick.getEffect() != null) {
            brick.getEffect().setvY(-6);
            this.effects.add(brick.getEffect());
        }
    }

    public void increaseBallSpeed() {
//        ball.increaseSpeed();
    }

    public void moveBall() {
        //ball.move();
        getBalls().forEach(Ball::move);
    }

    public void movePaddle(Paddle paddle, String action) {
        switch (action) {
            case "left":
                paddle.moveLeft();
                break;
            case "right":
                paddle.moveRight();
                break;
        }
    }

    public void movePaddle(String action, String user) {
    }

    ;

    public List<Paddle> getPaddles() {
        return paddles;
        //return paddle;
    }

    public void moveEffect() {
        for (Effect e : effects) {
            e.move();
        }
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void setCeiling(Paddle ceiling) {
        this.ceiling = ceiling;
    }

    public void updateGame() {
        if (getBalls() != null) {
            getBalls().forEach(ball -> {
                if (ball.getvY() == 0) ball.setvY(-5);
                collision(ball);
            });
        }
        if (effects != null) {
            effects.forEach(effect -> {
                getPaddles().forEach(paddle -> {
                    paddleContact(effect, paddle, this);
                });
                canvasContact(effect);
            });
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public Paddle getCeiling() {
        return ceiling;
    }

    public Player getPointGainingPlayer() {
        return pointGainingPlayer;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setPaddles(List<Paddle> paddles) {
        this.paddles = paddles;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }

    public void setPointGainingPlayer(Player pointGainingPlayer) {
        this.pointGainingPlayer = pointGainingPlayer;
    }
}
