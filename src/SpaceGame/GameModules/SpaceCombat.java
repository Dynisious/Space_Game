package SpaceGame.GameModules;

import SpaceGame.CombatShips.CombatShip;
import SpaceGame.CombatShips.CombatShipBase;
import SpaceGame.EntityTypes.LivingEntity;
import SpaceGame.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * <p>
 * The SpaceCombat module is responsible for combat between individual
 * EntityShips.</p>
 *
 * @author Dynisious 15/05/2015
 * @versions 0.0.1
 */
public final class SpaceCombat implements GameModule {
    /**
     * <p>
     * The maximum number of Ships that can be loaded at a time.</p>
     */
    private int maxLoadedShips;
    /**
     * <p>
     * Used to get the next ID if no ID's are available.</p>
     */
    private int nextID = 0;
    /**
     * <p>
     * A HashMap of combatants which each have their own index which they will
     * hold for their lifetime.</p>
     */
    private final HashMap<Integer, CombatShip> combatants;
    /**
     * <p>
     * The Array of CombatShipTemplates to base new Ships off.</p>
     */
    private CombatShipBase[] templates;
    /**
     * <p>
     * The IDs to be unloaded after each tick.</p>
     */
    private ArrayList<Integer> finalising = new ArrayList<>();
    /**
     * <p>
     * An ArrayList of CombatShip's pending loading.</p>
     */
    private ArrayList<CombatShip> pending = new ArrayList<>();

    /**
     * <p>
     * Creates a new Instance of the SpaceCombat GameModule.</p>
     *
     * @param initMaxLoadedShips The limit on how many CombatShips can be loaded
     *                           at one time.
     * @param initCombatants     The CombatShips to load in initially.
     * @param initTemplates      The CombatShipTemplates used to initialise new
     *                           CombatShips of a specific loaded type.
     */
    public SpaceCombat(int initMaxLoadedShips,
                       Collection<CombatShip> initCombatants,
                       CombatShipBase[] initTemplates) {
        templates = initTemplates;
        maxLoadedShips = initMaxLoadedShips;
        combatants = new HashMap<>(initMaxLoadedShips);
        for (CombatShip s : initCombatants) {
            loadShip(s);
        }
    }

    /**
     * <p>
     * Loads the passed Ship into the next available ID.</p>
     *
     * @param ship The Ship to load.
     *
     * @return True if the Ship was loaded. False if the limit on the number of
     *         loaded CombatShips was reached.
     *
     * @throws NullPointerException Thrown if Ship is null.
     */
    private boolean loadShip(CombatShip ship) throws NullPointerException {
        if (ship == null) {
            throw new NullPointerException("ERROR : Ship was a null value.");
        }
        if (ship.id != -1) { //This CombatShip has been loaded elswhere.
            throw new NullPointerException(
                    "ERROR : This Ship has already been loaded elsewhere.");
        }
        synchronized (combatants) {
            if (combatants.size() < maxLoadedShips) { //There's room for more Ship's
                combatants.put(++nextID, ship);
                ship.id = nextID;
                Logger.getInstance().write(ship.toString() + " loaded into "
                        + getClass().getSimpleName());
                return true;
            } else { //There's no room for more Ships.
                synchronized (pending) {
                    pending.add(ship);
                }
                Logger.getInstance().write(
                        ship.toString() + " could not load into "
                        + getClass().getSimpleName() + " ship limit("
                        + maxLoadedShips + ") reached. It is now pending.");
                return false;
            }
        }
    }

    /**
     * <p>
     * Queues the passed ID to be unloaded.</p>
     *
     * @param id The ID to unload.
     *
     * @return True if the Ship will be unloaded.
     */
    public boolean unloadShip(Integer id) {
        synchronized (combatants) {
            if (combatants.containsKey(id)) { //A CombatShip is at this index.
                synchronized (finalising) {
                    finalising.add(id);
                    Logger.getInstance().write(getClass().getSimpleName() + ": "
                            + combatants.get(id).toString() + " was unloaded.");
                }
                return true;
            } else { //This index is empty
                return false;
            }
        }
    }

    /**
     * <p>
     * unloads the Ships at the passed IDs from combatants.</p>
     *
     * @param ids The IDs to unload.
     */
    private void finaliseShips() {
        synchronized (finalising) {
            for (Integer i : finalising) {
                combatants.remove(i).getBaseShip().getLivingEntity().state = LivingEntity.LifeStates.Dead;
                synchronized (pending) {
                    if (!pending.isEmpty()) { //Load in the next pending CombatShip.
                        loadShip(pending.remove(0));
                    }
                }
            }
            finalising.clear(); //Clear the finalised IDs
        }
    }

    @Override
    public GameStates tick() {
        Logger.getInstance().write("---------------");

        GameStates returnState = GameStates.Null; //The state to return to the ModuleManager
        if (!combatants.isEmpty()) {
            synchronized (finalising) {
                int firstFaction = ((CombatShip) combatants.values().toArray()[0]).getFaction();
                for (int id : combatants.keySet()) {
                    if (combatants.get(id).getFaction() != firstFaction) { //There are enemies here.
                        returnState = GameStates.Combat;
                    }
                    combatants.get(id).tick(combatants, this);
                }
            }
        }

        finaliseShips(); //Remove all finalising IDs

        Logger.getInstance()
                .write(System.lineSeparator() + getClass().getSimpleName() + " has ticked.");
        return returnState;
    }
}
