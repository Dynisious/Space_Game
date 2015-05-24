package SpaceGame;

import SpaceGame.GameModules.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * Runs the game code and maintains the game environment.</p>
 *
 * @author Dynisious 24/05/2015
 * @versions 0.0.1
 */
public final class Server {
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

    public Server(ModuleManager manager) {
        environment = manager;
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
                            environment.tick();
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
    public synchronized void finalisation() {
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

}
