package SpaceGame.CombatShips.Behaviors;

import SpaceGame.CombatShips.CombatShip;
import SpaceGame.CombatShips.SituationInstant;

/**
 * <p>
 * Each behavior contains two methods; Check and Behave.</p>
 *
 * @author Dynisious 21/05/2015
 * @version 0.0.1
 */
public interface BehaviorBase {

    /**
     * <p>
     * Checks if the passed Ship is capable of performing the behave method and
     * returns true or false.</p>
     *
     * @param sit  The current situation the CombatShip is in.
     * @param ship The CombatShip attempting to behaving.
     *
     * @return A boolean indicating whether the CombatShip is capable of
         performing behave.
     */
    public boolean check(SituationInstant sit, CombatShip ship);

    /**
     * <p>
 Causes the passed CombatShip to perform this behavior.</p>
     *
     * @param sit  The current situation the CombatShip is in.
     * @param ship The CombatShip behaving.
     */
    public void behave(SituationInstant sit, CombatShip ship);

}
