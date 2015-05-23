package SpaceGame.GameModules;

import SpaceGame.Logger;
import java.util.HashMap;

/**
 * <p>
 * Manages the separate GameModules in the game.</p>
 *
 * @author Dynisious 21/05/2015
 * @versions 0.0.1
 */
public final class ModuleManager {
    private HashMap<GameStates, GameModule> modules; //The Array of GameModules which the game runs.
    private GameStates state; //The currently running GameModule.

    /**
     * <p>
     * Creates a new ModuleManager</p>
     *
     * @param initModules The HashMap of GameModules which this game can run.
     * @param initState   The initial GameModule in modules to run.
     */
    public ModuleManager(HashMap<GameStates, GameModule> initModules,
                         GameStates initState) {
        modules = initModules;
        state = initState;
    }

    /**
     * <p>
     * Runs the per tick update for the active GameModule.</p>
     */
    public void tick() {
        state = modules.get(state).tick();
        if (state == GameStates.Null) {
            Logger.getInstance().write(
                    System.lineSeparator() + "The game has no continuation.");
            System.exit(1);
        }
    }
    
}
