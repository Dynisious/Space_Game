package SpaceGame.GameModules;

/**
 * <p>
 * A GameModule is a particular section of the game to be run at any one time. A
 * GameModule has one method:tick().</p>
 *
 * @author Dynisious 15/05/2015
 * @versions 0.0.1
 */
public interface GameModule {

    /**
     * <p>
     * Runs the operations that need to happen each tick of the game.</p>
     *
     * @return The enumerator for the GameModule to run next tick.
     */
    public abstract GameStates tick();
}
