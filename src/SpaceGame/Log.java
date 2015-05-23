package SpaceGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * <p>
 * The Log is used to write output to a log file.</p>
 *
 * @author Dynisious 21/05/2015
 * @versions 0.0.1
 */
public final class Log {
    private FileWriter writer;
    private boolean debugging;
    private boolean logging = true;

    /**
     * <p>
     * Create a new Log that writes to the specified Log file.</p>
     *
     * @param file  The file to use as the log.
     * @param debug True if logging gets printed to the console.
     */
    public Log(String file, boolean debug) {
        debugging = debug;
        try {
            File f = new File(file);
            if (f.exists()) {
                f.delete();
            }
            if (f.createNewFile()) {
                System.out.println("The Log file was created.");
            } else {
                logging = false;
                System.out.println(
                        "The Log file could not be created. Logging is now dissabled.");
            }
            writer = new FileWriter(file);
            LocalDateTime now = LocalDateTime.now();
            write("Space_Game : " + now.getMonth().toString() + " "
                    + now.getDayOfMonth() + ", " + now.getYear() + " "
                    + now.getHour() + ":" + now.getMinute() + System.lineSeparator());
        } catch (IOException ex) {
            System.out.println(
                    "ERROR : There was an error creating a Log. Path:" + file
                    + System.lineSeparator() + ex.getMessage());
            for (StackTraceElement s : ex.getStackTrace()) {
                System.out.println("    at " + s.toString());
            }
            try {
                Thread.currentThread().wait(3000);
            } catch (InterruptedException ex1) {
                //Ignore
            }
            System.exit(1);
        }
    }

    /**
     * <p>
     * Logs the passed String and prints it to the console if debugging it
     * active.</p>
     *
     * @param str The String to log.
     */
    public void write(String str) {
        if (debugging) {
            System.out.println(str);
        }
        if (logging) {
            try {
                writer.write(str + System.lineSeparator());
                writer.flush();
            } catch (IOException ex) {
                logging = false;
                System.out.println("ERROR : There was an error logging '" + str
                        + "'. Logging is now dissabled." + System.lineSeparator()
                        + ex.getMessage());
                for (StackTraceElement s : ex.getStackTrace()) {
                    System.out.println("    at " + s.toString());
                }
            }
        }
    }
}
