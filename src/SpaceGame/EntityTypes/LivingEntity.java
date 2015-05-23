package SpaceGame.EntityTypes;

import SpaceGame.CombatShips.SituationInstant;
import SpaceGame.Logger;
/**
 * <p>
 * Contains the values for an Entity which is capable of dying.</p>
 *
 * @author Dynisious 23/05/2015
 * @versions 0.0.1
 */
public class LivingEntity {
    /**
     * <p>
     * The amount of damage this CombatShip can take before being destroyed.</p>
     */
    public double healthPoints;
    /**
     * <p>
     * The maximum health points for this type of CombatShip.</p>
     */
    private double maxHealthPoints;
    public double getMaxHealthPoints() {
        return maxHealthPoints;
    }
    /**
     * <p>
     * A double value representing what percentage of raw incoming damage
     * damages the CombatShip.</p>
     */
    private double defenseModifier;
    /**
     * <p>
     * The different life states that a LivingEntity can be in.</p>
     */
    public enum LifeStates {
        Alive, Finalising, Dead
    }
    /**
     * <p>
     * The current state of this LivingEntity.</p>
     */
    public LifeStates state = LifeStates.Alive;

    /**
     * <p>
     * Creates a new LivingEntity with the given values.</p>
     *
     * @param initHealthPoints    The starting health of this LivingEntity.
     * @param initMaxHealthPoints The maximum health of this livingEntity.
     * @param initDefenceModifier The defense modifier to apply to incoming
     *                            damage.
     */
    public LivingEntity(double initHealthPoints, double initMaxHealthPoints,
                        double initDefenceModifier) {
        healthPoints = initHealthPoints;
        maxHealthPoints = initMaxHealthPoints;
        defenseModifier = initDefenceModifier;
    }

    /**
     * <p>
     * Returns a new instance of this LivingEntity.</p>
     *
     * @return The new instance.
     */
    public LivingEntity getInstance() {
        return new LivingEntity(maxHealthPoints, maxHealthPoints,
                defenseModifier);
    }

    /**
     * <p>
     * Takes removes the passed damage from healthPoint after applying the
     * damage modifier.</p>
     *
     * @param damage    The raw damage that this CombatShip is taking.
     * @param logString The String to put as the source of the log output.
     */
    public void takeDamage(double damage, String logString) {
        double modifiedDamage = damage * defenseModifier;
        healthPoints -= modifiedDamage;
        if (healthPoints <= 0) { //This LivingEntity has died.
            state = LifeStates.Finalising;
        }
        Logger.getInstance().write(logString + " damage raw=" + damage
                + " modified=" + modifiedDamage + " health=" + healthPoints);
    }

    /**
     * <p>
     * Does damage for any collisions for this CombatShip.</p>
     *
     * @param sit       The current Situation this
     * @param entity    The StaticEnitity which is checking collisions which
     *                  this
     *                  LivingEntity is a part of.
     * @param logString The String to put as the source of any log outputs.
     */
    public void checkCollisions(SituationInstant sit, MobileEntity entity,
                                String logString) {
        logString += ": Collision";
        for (Integer i : sit.combatants.keySet()) {
            MobileEntity target = sit.combatants.get(i).getBaseShip().getMobility();
            if (entity.getPositionEntity().getBoundingLength() + target.getPositionEntity().getBoundingLength()
                    > sit.distances.get(i)) { //It is possible that these two are colliding.
                int xOffset = (int) (Math.cos(sit.directions.get(i))
                        * sit.distances.get(i)); //The offset of the target from entity in entity's space on the x axis.

                double direction = target.getPositionEntity().getDirection() - entity.getPositionEntity().getDirection(); //The direction of face of the target relative to the entity.
                int firstX = (int) (Math.cos(
                        target.getPositionEntity().getBoundingAngle() + direction) * target.getPositionEntity().getBoundingLength())
                        + xOffset; //Get the x cood of the top right corner of the targets bounding box in relation to entity.
                int secondX = (int) (Math.cos(
                        target.getPositionEntity().getBoundingAngle() + direction + Math.PI) * target.getPositionEntity().getBoundingLength())
                        + xOffset; //Get the x coord of the bottom left corner of the targets bounding box in relation to entity.
                firstX -= entity.getPositionEntity().getTopRight().x; //Get the difference between the targets top right x and entity's
                secondX -= entity.getPositionEntity().getBottomLeft().x; //Get the difference between the targets bottom left x and entity's.
                if (Math.signum(firstX) == Math.signum(secondX)) { //They haven't crossed on the x axis.
                    int yOffset = (int) (Math.sin(sit.directions.get(i))
                            * sit.distances.get(i)); //The offset of the target from entity in entity's space on the y axis.
                    int firstY = (int) (Math.sin(
                            target.getPositionEntity().getBoundingAngle() + direction) * target.getPositionEntity().getBoundingLength())
                            + yOffset; //Get the x cood of the top right corner of the targets bounding box in relation to entity.
                    int secondY = (int) (Math.sin(
                            target.getPositionEntity().getBoundingAngle() + direction + Math.PI) * target.getPositionEntity().getBoundingLength())
                            + yOffset; //Get the x coord of the bottom left corner of the targets bounding box in relation to entity.
                    firstY -= entity.getPositionEntity().getTopRight().y; //Get the difference between the targets top right y and entity's
                    secondY -= entity.getPositionEntity().getBottomLeft().y; //Get the difference between the targets bottom left y and entity's.                    

                    if (Math.signum(firstY) != Math.signum(secondY)) { //They collided.
                        takeDamage(target.getPositionEntity().getCollideDamage(),
                                logString); //This LivingEntity takes damage.
                        sit.combatants.get(i).getBaseShip().getLivingEntity().takeDamage(
                                entity.getPositionEntity().getCollideDamage(),
                                sit.combatants.get(i).toString() + ": Collision"); //The other LivingEntity takes damage.
                    }
                } else { //They collided.
                    takeDamage(target.getPositionEntity().getCollideDamage(),
                            logString); //This LivingEntity takes damage.
                    sit.combatants.get(i).getBaseShip().getLivingEntity().takeDamage(
                            entity.getPositionEntity().getCollideDamage(),
                            sit.combatants.get(i).toString() + ": Collision"); //The other LivingEntity takes damage.
                    
                    entity.getMobileEntity().setXSpeed(0);
                    entity.getMobileEntity().setYSpeed(0);
                    
                    target.getMobileEntity().setXSpeed(0);
                    target.getMobileEntity().setYSpeed(0);
                }
            }
        }
    }
    
}
