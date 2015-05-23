package SpaceGame.CombatShips;

import static SpaceGame.Application.*;
import java.util.HashMap;

/**
 * <p>
 * A Class which contains all the information a CombatShip needs to be able
 * to decide on it's behavior and behave.</p>
 */
public final class SituationInstant {
    /**
     * The loaded combatants.
     */
    public HashMap<Integer, CombatShip> combatants;
    /**
     * The distances between this Ship and the loaded Ships.
     */
    public HashMap<Integer, Integer> distances;
    /**
     * The directions relative to this Ship to the loaded Ships.
     */
    public HashMap<Integer, Double> directions;
    /**
     * The speed along the x axis of all Ship's relative to this Ship.
     */
    public HashMap<Integer, Double> relXSpeed;
    /**
     * The speed along the Y axis of all Ship's relative to this Ship.
     */
    public HashMap<Integer, Double> relYSpeed;
    /**
     * The ID of the closest Ship.
     */
    public Integer closestID = -1;

    public SituationInstant(HashMap<Integer, CombatShip> initCombatants,
                            CombatShip ship) {
        combatants = (HashMap< Integer, CombatShip>) initCombatants.clone();
        combatants.remove(ship.id); //Remove this Ship from the combatants.
        distances = new HashMap<>(combatants.size());
        directions = new HashMap<>(combatants.size());
        relXSpeed = new HashMap<>(combatants.size());
        relYSpeed = new HashMap<>(combatants.size());

        int distance = Integer.MAX_VALUE; //The distance from this Ship to the closest Ship.
        for (CombatShip s : combatants.values()) { //Loop through each combatant.
            int relX = s.getBaseShip().getMobility().getPositionEntity().x
                    - ship.getBaseShip().getMobility().getPositionEntity().x;
            int relY = s.getBaseShip().getMobility().getPositionEntity().y
                    - ship.getBaseShip().getMobility().getPositionEntity().y;

            distances.put(s.id, (int) Math.hypot(relX, relY)); //Set the distance from this Ship to s.
            if (s.getFaction() != ship.getFaction()) { //These Ship's are not alligned.
                if (distances.get(s.id) < distance) { //This Ship is closer than the last closest.
                    distance = distances.get(s.id);
                    closestID = s.id;
                }
            }

            directions.put(s.id, normaliseAngle(Math.atan2(
                    relY, relX) - ship.getBaseShip().getMobility().getPositionEntity().getDirection())); //Set the angle from this Ship's face to s.
            relXSpeed.put(s.id,
                    s.getBaseShip().getMobility().getMobileEntity().getXSpeed()
                    - ship.getBaseShip().getMobility().getMobileEntity().getXSpeed()); //Set the speed of s on the x axis relative to this Ship.
            relYSpeed.put(s.id,
                    s.getBaseShip().getMobility().getMobileEntity().getYSpeed()
                    - ship.getBaseShip().getMobility().getMobileEntity().getYSpeed()); //Set the speed of s on the y axis relative to this Ship.
        }
    }
}
