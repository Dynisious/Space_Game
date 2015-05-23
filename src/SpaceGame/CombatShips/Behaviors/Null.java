package SpaceGame.CombatShips.Behaviors;

import SpaceGame.CombatShips.CombatShip;
import SpaceGame.CombatShips.SituationInstant;

/**
 * <p>
 * Continue in a constant speed and direction.</p>
 *
 * @author Dynisious 21/05/2015
 * @versions 0.0.1
 */
public class Null implements BehaviorBase {

    @Override
    public boolean check(SituationInstant sit, CombatShip ship) {
        return true;
    }

    @Override
    public void behave(SituationInstant sit, CombatShip ship) {
        ship.getBaseShip().getMobility().getPositionEntity().x
        += ship.getBaseShip().getMobility().getMobileEntity().getXSpeed();
        ship.getBaseShip().getMobility().getPositionEntity().y
        += ship.getBaseShip().getMobility().getMobileEntity().getYSpeed();
    }
}
