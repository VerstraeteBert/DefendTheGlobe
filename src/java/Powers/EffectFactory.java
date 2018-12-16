package Powers;

import Powers.Ups.BombBall;
import Powers.Downs.Ceiling;
import Powers.Downs.InvisiblePaddle;
import Powers.Downs.ShrinkPaddle;
import Powers.Ups.ExtendedPaddle;
import Powers.Ups.IncreasedBallSize;
import data.Repositories;

import java.awt.*;
import java.util.Map;

/**
 * Created by tizianoplaiy on 06/12/2017.
 */
public class EffectFactory {
//Powerups zijn onder 5 powerdown zijn boven 5..
//FIXME Dit is momenteel nog niet zo

    private final String UP = "powerup";
    private final String DOWN = "powerdown";

    private final Color powerup = Color.green;
    private final Color powerdown = Color.red;

    public Effect createEffect(int type, int centerX, int centerY, int radiusX, int radiusY) {
        Map<String, Integer> effectProps =  Repositories.getMysqlBreakoutRepository().getEffectPropertiesById(type);
        switch (type) {
            case 1:
                return new ExtendedPaddle(UP, centerX, centerY, radiusX, radiusY, powerup, effectProps);
            case 2:
                return new IncreasedBallSize(UP, centerX, centerY, radiusX, radiusY, powerup, effectProps);
            case 3:
                return new ShrinkPaddle(DOWN, centerX, centerY, radiusX, radiusY, powerdown, effectProps);
            case 4:
            return new InvisiblePaddle(DOWN, centerX, centerY, radiusX, radiusY, powerdown, effectProps);
            case 5:
                return new Ceiling(DOWN,  centerX, centerY, radiusX, radiusY, powerdown, effectProps);
            case 6:
                return new BombBall(UP, centerX, centerY, radiusX, radiusY, powerup, effectProps);
            default:
                return null;
        }
    }

}
