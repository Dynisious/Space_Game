package SpaceGame.CombatShips;

import SpaceGame.EntityTypes.MobileEntity;
import SpaceGame.EntityTypes.LivingEntity;
import SpaceGame.EntityTypes.MovementEntity;
import SpaceGame.EntityTypes.BehavingEntity;
/**
 * <p>
 * Holds the basic values for a type of CombatShip.</p>
 *
 * @author Dynisious 22/05/2015
 * @versions 1.0.1
 */
public class CombatShipBase {
    /**
     * <p>
     * The values for a CombatShip as a MovementEntity.</p>
     */
    private MobileEntity mobility;
    public MobileEntity getMobility() {
        return mobility;
    }
    /**
     * <p>
     * The values for this CombatShip as a LivingEntity.</p>
     */
    private LivingEntity livingEntity;
    public LivingEntity getLivingEntity() {
        return livingEntity;
    }
    /**
     * <p>
     * The values for this CombatShip as a BehavingEntity</p>
     */
    private BehavingEntity behavingEntity;
    public BehavingEntity getBehavingEntity() {
        return behavingEntity;
    }
    /**
     * <p>
     * The Ship's type name.</p>
     */
    private String typeName;
    public String getTypeName() {
        return typeName;
    }
    /**
     * <p>
     * How massive this Ship is.</p>
     */
    private double mass;
    public double getMass() {
        return mass;
    }

    /**
     * <p>
     * Creates a new CombatShipBase to base new CombatShip's off.</p>
     *
     * @param initMobility       The values for this type of CombatShip as a
     *                           mobile entity.
     * @param initLivingEntity   The values for this type of CombatShip as a
     *                           living entity.
     * @param initBehavingEntity The values for this type of CombatShip as a
     *                           behaving entity.
     * @param initTypeName       The name of this type of CombatShip.
     * @param initMass           The mass of this CombatShip.
     */
    public CombatShipBase(MobileEntity initMobility,
                          LivingEntity initLivingEntity,
                          BehavingEntity initBehavingEntity,
                          String initTypeName, double initMass) {
        mobility = initMobility;
        livingEntity = initLivingEntity;
        behavingEntity = initBehavingEntity;
        typeName = initTypeName;
        mass = initMass;
    }

    /**
     * <p>
     * Returns a new instance of this CombatShipBase.</p>
     *
     * @param mobileValues     The values for this CombatShipBase's velocity.
     * @param initX            The x coordinate of this CombatShipBase.
     * @param initY            The y coordinate of this CombatShipBase.
     * @param initDirection    The direction of face of this CombatShipBase.
     * @param selectedBehavior The behavior to perform if behavior is locked.
     * @param behaviorLocked   Whether the CombatShipBase is capable of deciding
     *                         it's own behavior or whether it is locked to it's
     *                         selected behavior.
     *
     * @return The new instance of this CombatShipBase.
     */
    public CombatShipBase getInstance(MovementEntity mobileValues,
                                      int initX, int initY, double initDirection,
                                      int selectedBehavior,
                                      boolean behaviorLocked) {
        return new CombatShipBase(
                mobility.getInstance(mobileValues, initX, initY, initDirection),
                livingEntity.getInstance(),
                behavingEntity.getInstance(selectedBehavior, behaviorLocked),
                typeName, mass);
    }

}
