package SpaceGame.CombatShips;

import SpaceGame.GameModules.SpaceCombat;
import SpaceGame.Logger;
import java.util.HashMap;
import SpaceGame.EntityTypes.LivingEntity.LifeStates;

/**
 * <p>
 * A Ship designed to be placed into a SpaceCombat module.</p>
 *
 * @author Dynisious 15/05/2015
 * @versions 0.0.1
 */
public final class CombatShip {
    /**
     * <p>
     * The CombatShipBase which this CombatShip is based on.</p>
     */
    private CombatShipBase baseShip;
    public CombatShipBase getBaseShip() {
        return baseShip;
    }
    /**
     * <p>
     * The Ship's ID inside the SpaceCombat module.</p>
     */
    public int id = -1;
    /**
     * <p>
     * The Ship's faction value.</p>
     */
    private int faction;
    public int getFaction() {
        return faction;
    }

    /**
     * <p>
     * Creates a new EntityShip with all it's values initialised.</p>
     *
     * @param initTypeBase The CombatShipBase which this CombatShip is based
     *                     on.
     * @param initFaction  The faction this Ship is aligned to.
     */
    public CombatShip(CombatShipBase initTypeBase, int initFaction) {
        baseShip = initTypeBase;
        faction = initFaction;
    }

    public void tick(HashMap<Integer, CombatShip> sitData,
                     SpaceCombat environment) {
        Logger.getInstance().write("");

        SituationInstant sit = new SituationInstant(sitData, this);
        String performedBehavior = baseShip.getBehavingEntity().behave(sit, this); //The String representing the name of the behavior that was performed.
        baseShip.getLivingEntity().checkCollisions(sit, baseShip.getMobility(),
                toString()); //Check for any collisions between this CombatShip and the others in environment.
        if (baseShip.getLivingEntity().state == LifeStates.Finalising) { //The CombatShip is out of health.
            environment.unloadShip(id); //Set this Ship to be unloaded from the environment.
        }
        Logger.getInstance().write(
                toString() + " ticked: behavior=" + performedBehavior + " X="
                + baseShip.getMobility().getPositionEntity().x + " Y="
                + baseShip.getMobility().getPositionEntity().y + " bearing="
                + String.format("%3.2f",
                        180 * baseShip.getMobility().getPositionEntity().getDirection() / Math.PI)
                + " speed=" + (int) Math.hypot(
                        baseShip.getMobility().getMobileEntity().getXSpeed(),
                        baseShip.getMobility().getMobileEntity().getYSpeed()) + " travel="
                + String.format("%3.2f", 180 * Math.atan2(
                                baseShip.getMobility().getMobileEntity().getYSpeed(),
                                baseShip.getMobility().getMobileEntity().getXSpeed()) / Math.PI)
                + " lock=" + baseShip.getBehavingEntity().lock + " faction="
                + faction + " hull=" + ((int) baseShip.getLivingEntity().healthPoints)
                + " state=" + baseShip.getLivingEntity().state);
    }

    @Override
    public String toString() {
        return getBaseShip().getTypeName() + id;
    }
}
