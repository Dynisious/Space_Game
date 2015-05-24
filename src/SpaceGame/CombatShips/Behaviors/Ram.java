package SpaceGame.CombatShips.Behaviors;

import static SpaceGame.Server.*;
import SpaceGame.Logger;
import SpaceGame.CombatShips.CombatShip;
import SpaceGame.EntityTypes.MobileEntity;
import SpaceGame.CombatShips.SituationInstant;

/**
 * <p>
 * Ram the closest enemy.</p>
 *
 * @author Dynisious 21/05/2015
 * @versions 0.0.1
 */
public class Ram implements BehaviorBase {

    @Override
    public boolean check(SituationInstant sit, CombatShip ship) {
        return (sit.closestID != -1); //There is a closest enemy Ship.
    }

    @Override
    public void behave(SituationInstant sit, CombatShip ship) {
        boolean charge = (int) (sit.directions.get(sit.closestID)
                / quarterCircle) == 0;
        MobileEntity entity = ship.getBaseShip().getMobility();
        MobileEntity target = sit.combatants.get(sit.closestID).getBaseShip().getMobility();
        double direction = entity.getPositionEntity().getDirection();
        double acel = (charge ? entity.getForwardAcceleration() : -entity.getReverseAcceleration()); //Get the acceleration the Ship can make.

        entity.getMobileEntity().changeXSpeed(Math.cos(direction) * acel);
        entity.getMobileEntity().changeYSpeed(Math.sin(direction) * acel);

        if (sit.directions.get(sit.closestID) > 0) { //The enemy is to the left.
            entity.getMobileEntity().changeXSpeed(Math.cos(
                    direction + quarterCircle)
                    * entity.getStrafeAcceleration());
            entity.getMobileEntity().changeYSpeed(Math.sin(
                    direction + quarterCircle)
                    * entity.getStrafeAcceleration());

            entity.getPositionEntity().setDirection(direction
                    + entity.getTurnSpeed());
        } else { //The enemy is to the right.
            entity.getMobileEntity().changeXSpeed(Math.cos(
                    direction - quarterCircle)
                    * entity.getStrafeAcceleration());
            entity.getMobileEntity().changeYSpeed(Math.sin(
                    direction - quarterCircle)
                    * entity.getStrafeAcceleration());

            entity.getPositionEntity().setDirection(direction
                    - entity.getTurnSpeed());
        }

        entity.getPositionEntity().x += entity.getMobileEntity().getXSpeed();
        entity.getPositionEntity().y += entity.getMobileEntity().getYSpeed();

        Logger.getInstance().write(
                ship.toString() + ": " + getClass().getSimpleName() + " dist="
                + (int) Math.hypot(
                        target.getPositionEntity().x - entity.getPositionEntity().x,
                        target.getPositionEntity().y - entity.getPositionEntity().y)
                + " charging=" + charge + " target="
                + sit.combatants.get(sit.closestID).toString());
    }
}
