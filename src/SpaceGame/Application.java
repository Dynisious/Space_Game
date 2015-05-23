package SpaceGame;

import SpaceGame.EntityTypes.MobileEntity;
import SpaceGame.CombatShips.*;
import SpaceGame.CombatShips.Behaviors.*;
import SpaceGame.EntityTypes.*;
import SpaceGame.GameModules.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * A small space game for me to play with features in and edit as I go.</p>
 * <p>
 * Application is the outermost layer for the game where it will launch
 * from.</p>
 *
 * @author Dynisious 14/05/2015
 * @versions 0.0.1
 */
public final class Application {
    /**
     * <p>
     * One quarter of a full rotation in radians.</p>
     */
    public static final double quarterCircle = Math.PI / 2;
    /**
     * <p>
     * The tick event for the game.</p>
     */
    private Timer gameTick;
    /**
     * <p>
     * The ModuleManager for this game.</p>
     */
    private ModuleManager environment;
    /**
     * <p>
     * Keeps the application open as long as it is true.</p>
     */
    public boolean run = true;

    public Application(int interval, ModuleManager manager) {
        environment = manager;
        startExecution(interval);
    }

    /**
     * <p>
     * Halts the current execution of the game.</p>
     */
    public void stopExecution() {
        if (gameTick != null) {
            synchronized (gameTick) {
                gameTick.cancel();
                gameTick = null;
            }
        }
    }

    /**
     * <p>
     * Begins execution of the game.</p>
     *
     * @param interval The interval between each tick in milliseconds.
     */
    public void startExecution(int interval) {
        if (interval > 0) {
            stopExecution();
            gameTick = new Timer("Space_Game: Game Tick");
            synchronized (gameTick) {
                gameTick.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if (run) {
                                environment.tick();
                            } else {
                                finalisation();
                            }
                        } catch (Exception ex) {
                            String out = System.lineSeparator() + "ERROR : There was an exception while executing the game code."
                                    + System.lineSeparator() + "    " + ex.getMessage();
                            for (StackTraceElement s : ex.getStackTrace()) {
                                out += System.lineSeparator() + "        at " + s.toString();
                            }
                            Logger.getInstance().write(out);
                            System.out.println(out);
                            System.exit(1);
                        }
                    }
                }, 0, interval);
            }
        }
    }

    /**
     * <p>
     * Finalises this instance of application.</p>
     */
    private synchronized void finalisation() {
        stopExecution();
        notifyAll();
    }

    /**
     * <p>
     * Returns a angle that is between -PI and PI</p>
     *
     * @param dir The angle to put within the bounds.
     *
     * @return The normalised angle.
     */
    public static double normaliseAngle(double dir) {
        dir = Math.IEEEremainder(dir, Math.PI * 2);
        if (Math.signum(dir) == 1) {
            if (dir > Math.PI) { //Invert the angle.
                dir -= 2 * Math.PI;
            }
        } else if (Math.signum(dir) == -1) {
            if (dir < -Math.PI) { //Invert the angle.
                dir += 2 * Math.PI;
            }
        }
        return dir;
    }

    /**
     * <p>
     * Initialise the application and it's default values and modules.</p>
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        int interval = 50;
        boolean debugging = false;
        int SpaceCombat_maxLoadedShips = 400;
        /*<editor-fold defaultstate="collapsed" desc="Command Line Arguments">*/ {
            System.out.println("---Process Commandline---");
            for (int i = 0; i < args.length; i++) {
                try {
                    if (args[i].equalsIgnoreCase("-path")) { //Set the working directory for this instance of the application.
                        System.setProperty("user.dir", args[++i]);
                        System.out.println("Application path set.");
                    } else if (args[i].equalsIgnoreCase("-debug")) { //Set whether log output gets written to the console.
                        debugging = true;
                    } else if (args[i].equalsIgnoreCase("-tick")) { //Set the interval between each tick in this instance of the game.
                        interval = Integer.valueOf(args[++i]);
                    } else if (args[i].equalsIgnoreCase("-maxShips")) { //Set the interval between each tick in this instance of the game.
                        SpaceCombat_maxLoadedShips = Integer.valueOf(args[++i]);
                    }
                } catch (NumberFormatException ex) { //An incorrectly formatted number was entered.
                    //Ignore this.
                }
            }
            Logger.setInstance(new Log("Log.log", debugging)); //Set the instance for the log and set debugging.
        } //</editor-fold>

        Application app; //The Application instance for the game.
        /*<editor-fold defaultstate="collapsed" desc="Initialise Game">*/ {
            System.out.println("---Start Game Environment---");
            HashMap<GameStates, GameModule> modules = new HashMap<>(1);
            /*<editor-fold defaultstate="collapsed" desc="Load Files and Directories">*/ {
                Logger.getInstance().write("---Files and Folders---");
                /*<editor-fold defaultstate="collapsed" desc="Working Directory">*/ {
                    File f = new File(System.getProperty("user.dir"));
                    if (!f.exists()) { //The working directory does not exist.
                        Logger.getInstance().write(
                                "The working directory will be created");
                        if (!f.mkdirs()) { //The working directory could not be created.
                            Logger.getInstance().write(
                                    "ERROR : The working directory could not be created.");
                            System.exit(1);
                        }
                    }
                } //</editor-fold>
                /*<editor-fold defaultstate="collapsed" desc="Entity Ships">*/ {
                    Logger.getInstance().write(
                            System.lineSeparator() + "---Entity Ships---");
                    String folder = "Entity Ships";
                    File f = new File(folder);
                    if (!f.exists()) { //There is no folder.
                        Logger.getInstance().write("The " + folder
                                + " folder does not exist. It will be created now.");
                        if (!f.mkdir()) { //The directory was not created.
                            Logger.getInstance().write(
                                    "ERROR : The " + folder + " folder could not be created.");
                        }
                        System.exit(1);
                    } else { //There is a folder.
                        String[] fields = new String[]{"mass", "forward",
                            "reverse", "strafe", "turnSpeed", "minDistance",
                            "hullpoints", "behaviors", "behaviorLock",
                            "collisionDamage", "defenceModifier", "width",
                            "height"}; //The Strings for the different fields.
                        ArrayList<CombatShipBase> ships = new ArrayList<>(
                                f.listFiles().length); //An ArrayList of different EntityShips from their files.
                        for (File ship : f.listFiles()) {
                            if (ship.getName().contains(".entityship")) { //This is an entityShip.
                                HashMap<String, Object> shipVals = new HashMap<>(
                                        fields.length); //A HashMap of the values that are read from the file.
                                try (FileReader reader = new FileReader(ship)) {
                                    char[] chrs = new char[(int) ship.length()]; //chrs holds all the chars in the file 'ship'.
                                    reader.read(chrs);
                                    String lines = String.valueOf(chrs);
                                    lines = lines.replaceAll(" ",
                                            "");
                                    lines = lines.replaceAll(
                                            "//" + System.lineSeparator(), ""); //Remove all spaces and concatinate all inline line breaks ("\\_\\_\n_\r represents a line break to be removed.).
                                    for (String line : lines.split(
                                            System.lineSeparator())) { //Loop through each line in the file.
                                        if (!line.startsWith("#")) { //The sharp indicates a comment.
                                            String[] vals = line.split("="); //Split the line into it's key and value pair.
                                            //<editor-fold defaultstate="collapsed" desc="Assign Values">
                                            try {
                                                if (vals[0].equalsIgnoreCase(
                                                        fields[0])) { //The mass is specified.
                                                    shipVals.put(fields[0],
                                                            Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[1])) { //The forward acceleration is specified.
                                                    shipVals.put(fields[1],
                                                            Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[2])) { //The reverse acceleration is specidied.
                                                    shipVals.put(fields[2],
                                                            Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[3])) { //The strafe acceleration is specified.
                                                    shipVals.put(fields[3],
                                                            Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[4])) { //The turnspeed is specified.
                                                    shipVals.put(fields[4],
                                                            Math.PI / Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[5])) { //The minimum distance the AI will close with another Ship.
                                                    shipVals.put(fields[5],
                                                            Integer.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[6])) { //The hullpoints have been specified.
                                                    shipVals.put(fields[6],
                                                            Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[7])) { //The behaviors are specified.
                                                    HashMap<String, BehaviorBase> behaviors = new HashMap<>(
                                                            3);
                                                    behaviors.put(
                                                            Ram.class.getSimpleName().toLowerCase(),
                                                            new Ram());
                                                    behaviors.put(
                                                            Evade.class.getSimpleName().toLowerCase(),
                                                            new Evade());
                                                    behaviors.put(
                                                            Null.class.getSimpleName().toLowerCase(),
                                                            new Null());

                                                    ArrayList<SpaceGame.CombatShips.Behaviors.BehaviorBase> selection
                                                                                                            = new ArrayList<>(
                                                                    behaviors.size()); //This will be the selection of behaviours the CombatShip can perform.
                                                    for (String str : vals[1].split(
                                                            ",")) { //Loop through each specified behaviour
                                                        str = str.toLowerCase();
                                                        if (behaviors.get(str) != null) { //There is a behavior at this String.
                                                            selection.add(
                                                                    behaviors.get(
                                                                            str));
                                                        }
                                                    }
                                                    BehaviorBase[] temp = (BehaviorBase[]) selection.toArray(
                                                            new BehaviorBase[selection.size()]);
                                                    shipVals.put(fields[7], temp); //Save the behaviors.
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[8])) { //The behaviorLock is specified.
                                                    if (vals[1].equalsIgnoreCase(
                                                            "true")) {
                                                        shipVals.put(fields[8],
                                                                true);
                                                    } else if (vals[1].equalsIgnoreCase(
                                                            "false")) {
                                                        shipVals.put(fields[8],
                                                                false);
                                                    }
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[9])) { //The collision damage is specified.
                                                    shipVals.put(fields[9],
                                                            Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[10])) { //The defence modifier is specified.
                                                    shipVals.put(fields[10],
                                                            Double.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[11])) {
                                                    shipVals.put(fields[11],
                                                            Integer.valueOf(
                                                                    vals[1]));
                                                } else if (vals[0].equalsIgnoreCase(
                                                        fields[12])) {
                                                    shipVals.put(fields[12],
                                                            Integer.valueOf(
                                                                    vals[1]));
                                                }
                                            } catch (IndexOutOfBoundsException ex) { //The index does not exist in this line.
                                                //Ignore this.
                                            }
                                            //</editor-fold>
                                        }
                                    }
                                    if (shipVals.size() == fields.length) { //A values for all fields were specified.
                                        ships.add(new CombatShipBase(
                                                new MobileEntity(
                                                        new StaticEntity(
                                                                (Double) shipVals.get(
                                                                        fields[9]),
                                                                (Integer) shipVals.get(
                                                                        fields[11]),
                                                                (Integer) shipVals.get(
                                                                        fields[12])),
                                                        (Double) shipVals.get(
                                                                fields[1]),
                                                        (Double) shipVals.get(
                                                                fields[2]),
                                                        (Double) shipVals.get(
                                                                fields[3]),
                                                        (Double) shipVals.get(
                                                                fields[4])),
                                                new LivingEntity(
                                                        (Double) shipVals.get(
                                                                fields[6]),
                                                        (Double) shipVals.get(
                                                                fields[6]),
                                                        (Double) shipVals.get(
                                                                fields[10])),
                                                new BehavingEntity(
                                                        (Integer) shipVals.get(
                                                                fields[5]),
                                                        (BehaviorBase[]) shipVals.get(
                                                                fields[7])),
                                                ship.getName().replace(
                                                        ".entityship", ""),
                                                (Double) shipVals.get(
                                                        fields[0])
                                        )); //Create a new CombatShipBase from the read values.
                                        Logger.getInstance().write(
                                                ship.getName() + " loaded successfully.");
                                    } else {
                                        Logger.getInstance().write(
                                                "ERROR : The file " + ship.getName()
                                                + " did not specify enough fields and will not be loaded."
                                                + System.lineSeparator() + "Fields specified:");
                                        for (String str : shipVals.keySet()) {
                                            Logger.getInstance().write(str);
                                        }
                                    }
                                } catch (IOException ex) {
                                    Logger.getInstance().write(
                                            "ERROR : The file '" + ship.getName()
                                            + "' could not be read. It's Ship will not be loaded.");
                                }
                            }
                        }

                        CombatShipBase[] templates = ships.toArray(
                                new CombatShipBase[ships.size()]); //The Array of CombatShipTemplates to put the Templates into from ships.
                        ArrayList<CombatShip> combatants = new ArrayList<>(
                                SpaceCombat_maxLoadedShips);
                        for (int i = 0; i < 2; i++) {
                            for (int e = 0; e < 6; e++) {
                                combatants.add(new CombatShip(
                                        templates[(int) (templates.length * Math.random())].getInstance(
                                                new MovementEntity(
                                                        0, 0),
                                                (int) (2000 * Math.random()),
                                                (int) (2000 * Math.random()),
                                                0, 0, false),
                                        i));
                            }
                        }

                        if (ships.isEmpty()) { //No EntityShips were loaded.
                            Logger.getInstance().write(
                                    "ERROR : No EntityShips were loaded.");
                            System.exit(1);
                        }
                        modules.put(
                                GameStates.Combat,
                                new SpaceCombat(
                                        SpaceCombat_maxLoadedShips,
                                        combatants, templates)
                        );
                    }
                } //</editor-fold>
            } //</editor-fold>

            app = new Application(
                    interval,
                    new ModuleManager(
                            modules,
                            GameStates.Combat)
            );
        }//</editor-fold>

        Scanner scan = new Scanner(System.in);
        while (!(args = scan.nextLine().split(" "))[0].equalsIgnoreCase("-exit")) { //Loop while exit has not been entered.
            try {
                if (args[0].equalsIgnoreCase("-help")) {
                    System.out.println(System.lineSeparator()
                            + String.format("%-20s", "-help") + ":\tPrints out information on all commands.\n"
                            + String.format("%-20s", "-exit") + ":\tCloses the application.\n"
                            + String.format("%-20s", "-pause") + ":\tPauses the execution of the applicarion.\n"
                            + String.format("%-20s", "-play") + ":\tResumes execution of the application.\n"
                            + String.format("%-20s", "-tick <interval>") + ":\tChanges the tick of the application.");
                } else if (args[0].equalsIgnoreCase("-pause")) {
                    app.stopExecution();
                } else if (args[0].equalsIgnoreCase("-play")) {
                    app.startExecution(interval);
                } else if (args[0].equalsIgnoreCase("-tick")) {
                    interval = Integer.valueOf(args[1]);
                    app.startExecution(interval);
                }
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                //Ignore this.
            }
        }
        app.run = false;
    }

}
