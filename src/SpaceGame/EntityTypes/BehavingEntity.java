package SpaceGame.EntityTypes;

import SpaceGame.CombatShips.Behaviors.BehaviorBase;
import SpaceGame.CombatShips.Behaviors.Null;
import SpaceGame.CombatShips.CombatShip;
import SpaceGame.CombatShips.SituationInstant;
/**
 * <p>
 * The values for an entity capable of behaving with AI.</p>
 *
 * @author Dynisious 23/05/2015
 * @versions 0.0.1
 */
public class BehavingEntity {
    /**
     * <p>
     * The minimum distance to allow a Ship to get.</p>
     */
    private int minDistance;
    public int getMinDistance() {
        return minDistance;
    }
    /**
     * <p>
     * The selected behavior in behaviors for this CombatShip if
     * behavior is locked.</p>
     */
    public int selected = 0;
    /**
     * <p>
     * An array of Behaviors which this Ship can perform in order of
     * preference.</p>
     */
    private BehaviorBase[] behaviors;
    /**
     * <p>
     * The default behavior to perform if no other behavior is available.</p>
     */
    private static final Null nullBehavior = new Null();
    /**
     * <p>
     * Whether the CombatShip can alter it's behavior depending on the
     * situation.</p>
     */
    public boolean lock;

    /**
     * <p>
     * Creates a new instance of a BehavingEntity with the given values.</p>
     *
     * @param initMinDistance The minimum distance this behavingEntity will let
     *                        other Entities be.
     * @param initBehaviors   The selection of behaviors this BehavingEntity can
     *                        perform.
     */
    public BehavingEntity(int initMinDistance, BehaviorBase[] initBehaviors) {
        minDistance = initMinDistance;
        behaviors = initBehaviors;
    }

    /**
     * <p>
     * Creates a new instance of a BehavingEntity with the given values.</p>
     *
     * @param initMinDistance The minimum distance this behavingEntity will let
     *                        other Entities be.
     * @param initSelected    The selected behavior to perform when lock it
     *                        true.
     * @param initBehaviors   The selection of behaviors this BehavingEntity can
     *                        perform.
     * @param initLock        Whether this BehavingEntity is capable of deciding
     *                        on it's own behavior or whether it will only
     *                        perform the selected behavior.
     */
    private BehavingEntity(int initMinDistance, int initSelected,
                           BehaviorBase[] initBehaviors,
                           boolean initLock) {
        minDistance = initMinDistance;
        selected = initSelected;
        behaviors = initBehaviors;
        lock = initLock;
    }

    /**
     * <p>
     * Returns a new instance of this BehavingEntity.</p>
     *
     * @param initSelected The selected behavior to perform if lock is true.
     * @param initLock     Whether the BehavingEntity is capable of deciding
     *                     it's own behavior or whether it will only perform the
     *                     selected behavior.
     *
     * @return The new instance of this BehavingEntity.
     */
    public BehavingEntity getInstance(int initSelected, boolean initLock) {
        return new BehavingEntity(minDistance, initSelected, behaviors, initLock);
    }

    /**
     * <p>
     * Performs a behavior based on the current situation.</p>
     *
     * @param sit  The current situation.
     * @param ship The CombatShip that is behaving.
     *
     * @return The name of the Behavior that was performed.
     */
    public String behave(SituationInstant sit, CombatShip ship) {
        if (lock) { //Go with the last selected behavior.
            if (behaviors[selected].check(sit, ship)) { //This behavior can be performed.
                behaviors[selected].behave(sit, ship);
                return behaviors[selected].getClass().getSimpleName();
            }
            nullBehavior.behave(sit, ship);
            return Null.class.getSimpleName();
        } else { //This CombatShip can change it's behavior.
            for (BehaviorBase b : behaviors) { //Loop through the selection of behaviors for this Ship in order of preference.
                if (b.check(sit, ship)) { //this behavior can be performed.
                    b.behave(sit, ship);
                    return b.getClass().getSimpleName();
                }
            }
            nullBehavior.behave(sit, ship);
            return Null.class.getSimpleName();
        }
    }

}
