package SpaceGame;

import SpaceGame.CombatShips.CombatShip;
import SpaceGame.CombatShips.CombatShipBase;
import SpaceGame.EntityTypes.MovementEntity;
import SpaceGame.GameModules.GameModule;
import SpaceGame.GameModules.GameStates;
import SpaceGame.GameModules.ModuleManager;
import SpaceGame.GameModules.SpaceCombat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>
 * A small space game for me to play with features in and edit as I go.</p>
 * <p>
 *
 * @author Dynisious 14/05/2015
 * @versions 2.0.2
 */
public final class Application {
    private Server server;
    private int maxShips;
    private CombatShipBase[] shipTemplates;

    /**
     * <p>
     * Initialises the application.</p>
     *
     * @param initMaxShips      The maximum number of Ships loaded into the
     *                          game.
     * @param initShipTemplates The templates used to base CombatShip objects
     *                          off.
     */
    public Application(int initMaxShips, CombatShipBase[] initShipTemplates) {
        maxShips = initMaxShips;
        shipTemplates = initShipTemplates;
    }

    public Server startServer(int interval) {
        Logger.getInstance().write("---Start Game Environment---");
        HashMap<GameStates, GameModule> modules = new HashMap<>(1);
        { //Space Combat Module 
            ArrayList<CombatShip> combatants = new ArrayList<>(
                    maxShips);
            SpaceCombat combat = new SpaceCombat(maxShips,
                    combatants, shipTemplates);

            for (int i = 0; i < 2; i++) {
                for (int e = 0; e < 6; e++) {
                    combat.loadShip(new CombatShip(
                            combat.getTemplates()[(int) (combat.getTemplates().length
                            * Math.random())].getInstance(
                                    new MovementEntity(
                                            0, 0),
                                    (int) (20000 * Math.random()),
                                    (int) (20000 * Math.random()),
                                    0, 0, false), i
                    ));
                }
            }

            modules.put(GameStates.Combat, combat);
        }

        server = new Server(new ModuleManager(modules, GameStates.Combat));
        server.startExecution(interval);
        return server;
    }

    public void changeExecution(int interval) {
        if (server != null) {
            if (interval > 0) {
                server.startExecution(interval);
            } else {
                server.stopExecution();
            }
        }
    }

    public void closeServer() {
        if (server != null) {
            server.finalisation();
        }
    }

}
