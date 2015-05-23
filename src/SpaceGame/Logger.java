package SpaceGame;

/**
 * <p>
 * Singleton allowing access to the log should one be needed.</p>
 *
 * @author Dynisious 21/05/2015
 * @versions 0.0.1
 */
public final class Logger {
    private static Log log = null;

    private Logger() {
        //No instantiasation.
    }

    /**
     * <p>
     * Gets the current instance of the log.</p>
     *
     * @return The current Log.
     */
    public static Log getInstance() {
        return log;
    }

    /**
     * <p>
     * Sets the current instance of the Log.</p>
     *
     * @param val The instance to set.
     */
    public static void setInstance(Log val) {
        log = val;
    }
}
