package SpaceGame.CombatShips.Behaviors;

import static SpaceGame.Application.*;
import SpaceGame.CombatShips.CombatShip;
import SpaceGame.CombatShips.CombatShipBase;
import SpaceGame.EntityTypes.MobileEntity;
import SpaceGame.CombatShips.SituationInstant;
import SpaceGame.Logger;

/**
 * <p>
 * Check for any potential collisions to evade (set the closest as closestID)
 * then accelerate and turn away from the closest ID.</p>
 *
 * @author Dynisious 21/05/2015
 * @versions 0.0.1
 */
public class Evade implements BehaviorBase {

    @Override
    public boolean check(SituationInstant sit, CombatShip ship) {
        boolean colliding = false; //Indicates whether this is a collision to avoid.
        int dist = Integer.MAX_VALUE; //The distance to the closest known collision.
        for (Integer i : sit.combatants.keySet()) {
            if (sit.distances.get(i) < dist) { //This Ship is closer than the last checked.
                CombatShipBase target = sit.combatants.get(i).getBaseShip();

                if ((sit.distances.get(i) - target.getBehavingEntity().getMinDistance()
                        - ship.getBaseShip().getBehavingEntity().getMinDistance())
                        < (10 * (Math.hypot(sit.relXSpeed.get(i),
                                sit.relYSpeed.get(i))
                        + (ship.getBaseShip().getMobility().getReverseAcceleration() * 5)))) { //The collision will happen before this Ship can slow to a stop if they're heading towards each other.
                    double margin = Math.abs(Math.atan2(
                            target.getBehavingEntity().getMinDistance()
                            + ship.getBaseShip().getBehavingEntity().getMinDistance(),
                            sit.distances.get(i))); //Get the absolute margin of difference to allow in radians.
                    double vector = Math.abs(normaliseAngle(Math.atan2(
                            sit.relYSpeed.get(i), sit.relXSpeed.get(i))
                            - ship.getBaseShip().getMobility().getPositionEntity().getDirection())); //Get the absolute direction of travel between these ships relative to this Ship's facing.

                    if (margin > vector) { //The absloute vector is less than the margin, these two are heading towards an intersection.
                        colliding = true;
                        sit.closestID = i; //Set the closest ID to be this ID because the stop AI will avoid the closest ID.
                        dist = sit.distances.get(i); //Set the new closest distance.
                    }
                }
            }
        }
        return colliding;
    }

    @Override
    public void behave(SituationInstant sit, CombatShip ship) {
        MobileEntity entity = ship.getBaseShip().getMobility();
        MobileEntity target = sit.combatants.get(sit.closestID).getBaseShip().getMobility();
        boolean toLeft = sit.directions.get(sit.closestID) > 0;
        double direction = entity.getPositionEntity().getDirection();

        if (Math.floor(sit.directions.get(sit.closestID) / quarterCircle) == 0) { //The closest ID is to the front.
            entity.getMobileEntity().changeXSpeed(-Math.cos(direction)
                    * entity.getReverseAcceleration());
            entity.getMobileEntity().changeYSpeed(-Math.sin(direction)
                    * entity.getReverseAcceleration());
        } else { //The closest ID is to the back.
            entity.getMobileEntity().changeXSpeed(Math.cos(direction)
                    * entity.getForwardAcceleration());
            entity.getMobileEntity().changeYSpeed(Math.sin(direction)
                    * entity.getForwardAcceleration());
        }

        entity.getPositionEntity().setDirection(
                direction + (toLeft ? -entity.getTurnSpeed()
                        : entity.getTurnSpeed())); //Turn away from the closest ID.

        direction += toLeft ? quarterCircle : -quarterCircle; //Offset the direction to how the strafe engines will fire.
        entity.getMobileEntity().changeXSpeed(Math.cos(direction)
                * entity.getStrafeAcceleration());
        entity.getMobileEntity().changeYSpeed(Math.sin(direction)
                * entity.getStrafeAcceleration());

        Logger.getInstance().write(
                ship.toString() + ": " + getClass().getSimpleName() + " dist="
                + ((int) Math.hypot(
                        target.getPositionEntity().x - entity.getPositionEntity().x,
                        target.getPositionEntity().y - entity.getPositionEntity().y))
                + " evadeing=" + sit.combatants.get(sit.closestID).toString());
    }
}
