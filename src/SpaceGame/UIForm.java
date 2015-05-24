package SpaceGame;

import SpaceGame.CombatShips.Behaviors.*;
import SpaceGame.CombatShips.*;
import SpaceGame.EntityTypes.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.*;

/**
 *
 * @author Dynisious
 */
public class UIForm extends javax.swing.JFrame {
    private Application app;
    public Application getApp() {
        return app;
    }
    public int interval;

    /**
     * <p>
     * Creates new form UIForm</p>
     *
     * @param maxShips      The maximum number of Ships loaded into the game.
     * @param shipTemplates The templates used to base CombatShip objects off.
     * @param initInterval  The update interval for the game.
     */
    public UIForm(int maxShips, CombatShipBase[] shipTemplates) {
        setUndecorated(true);
        initComponents();
        app = new Application(maxShips, shipTemplates);
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
                Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                setLocation((size.width - getMinimumSize().width) / 2,
                        ((size.height - getMinimumSize().height) / 2) - 10);
            }
        });
    }

    JButton btnExitGame;
    JButton btnOpenServer;
    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnExitGame = new JButton();
        btnOpenServer = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setUndecorated(true);
        getContentPane().setLayout(null);

        btnExitGame.setFont(new java.awt.Font("Consolas", 1, 40));
        btnExitGame.setText("Exit Game");
        btnExitGame.setName("btnExitGame");
        getContentPane().add(btnExitGame);
        btnExitGame.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnExitGame.setBounds(425, 550, 450, 100);
        btnExitGame.setFocusable(true);
        btnExitGame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exitGame();
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        btnOpenServer.setFont(new java.awt.Font("Consolas", 1, 40));
        btnOpenServer.setText("Open Server");
        btnOpenServer.setName("btnOpenServer");
        getContentPane().add(btnOpenServer);
        btnOpenServer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnOpenServer.setBounds(425, 350, 450, 100);
        btnOpenServer.setFocusable(true);
        btnOpenServer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.startServer(interval);
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }
            @Override
            public void mouseReleased(MouseEvent e) {

            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        pack();
    }

    private void exitGame() {
        app.closeServer();
        dispose();
        System.exit(0);
    }

    /**
     * <p>
     * Initialise the application and it's default values and modules.</p>
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        boolean graphics = true; //Indicates whether this instance of the game has a GUI.

        UIForm ui = null; //The UIPanel for this instance of the game.
        Server server = null; //The Server instance to interact with if there is no graphics.
        int interval = 50;
        {
            boolean debugging = false;
            int maxShips = 400;
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
                            maxShips = Integer.valueOf(args[++i]);
                        } else if (args[i].equalsIgnoreCase("-noGraphics")) { //Create a Server instance without graphics.
                            graphics = false;
                        }
                    } catch (NumberFormatException ex) { //An incorrectly formatted number was entered.
                        //Ignore this.
                    }
                }
                Logger.setInstance(new Log("Log.log", debugging)); //Set the instance for the log and set debugging.
            } //</editor-fold>

            CombatShipBase[] combatShipTemplates = null;
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
                    Logger.getInstance().write("---Entity Ships---");
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

                        combatShipTemplates = ships.toArray(
                                new CombatShipBase[ships.size()]);

                        if (ships.isEmpty()) { //No EntityShips were loaded.
                            Logger.getInstance().write(
                                    "ERROR : No EntityShips were loaded.");
                            System.exit(1);
                        }
                    }
                } //</editor-fold>
            } //</editor-fold>

            if (graphics) {
                ui = new UIForm(maxShips, combatShipTemplates);
                ui.interval = interval;
            } else {
                server = new Application(maxShips, combatShipTemplates).startServer(
                        interval); //Create a new Server.
                server.startExecution(interval);
            }
        }

        Scanner scan = new Scanner(System.in);
        while (!(args = scan.nextLine().split(" "))[0].equalsIgnoreCase(
                "-exit")) { //Loop while exit has not been entered.
            try {
                if (args[0].equalsIgnoreCase("-help")) {
                    System.out.println(System.lineSeparator()
                            + String.format("%-20s", "-help") + ":\tPrints out information on all commands.\n"
                            + String.format("%-20s", "-exit") + ":\tCloses the application.\n"
                            + String.format("%-20s", "-pause") + ":\tPauses the execution of the applicarion.\n"
                            + String.format("%-20s", "-play") + ":\tResumes execution of the application.\n"
                            + String.format("%-20s", "-tick <interval>") + ":\tChanges the tick of the application.");
                } else if (args[0].equalsIgnoreCase("-pause")) {
                    if (graphics) {
                        ui.getApp().changeExecution(-1);
                    } else {
                        server.stopExecution();
                    }
                } else if (args[0].equalsIgnoreCase("-play")) {
                    if (graphics) {
                        ui.getApp().changeExecution(ui.interval);
                    } else {
                        server.startExecution(interval);
                    }
                } else if (args[0].equalsIgnoreCase("-tick")) {
                    if (graphics) {
                        ui.interval = Integer.valueOf(args[1]);
                        ui.getApp().changeExecution(ui.interval);
                    } else {
                        interval = Integer.valueOf(args[1]);
                        server.startExecution(interval);
                    }
                }
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                //Ignore this.
            }
        }

        if (graphics) {
            ui.getApp().closeServer();
            ui.exitGame();
        } else {
            server.finalisation();
        }
    }

}
