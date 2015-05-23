package SpaceGame.EntityTypes;

/**
 * <p>
 * Holds all the values necessary for a MovementEntity to exist in a
 * SpaceCombat CombatModule.</p>
 *
 * @author Dynisious 22/05/2015
 * @versions 0.0.1
 */
public final class MovementEntity {
    /**
     * <p>
     * The maximum speed of anything in the entire game.<p>
     */
    private static final double maxSpeed = 50;
    /**
     * <p>
     * The Ship's speed on the x axis.</p>
     */
    private double xSpeed;
    public double getXSpeed() {
        return xSpeed;
    }
    public void setXSpeed(double val) {
        xSpeed = val;
        checkSpeed();
    }
    public void changeXSpeed(double val) {
        xSpeed += val;
        checkSpeed();
    }
    /**
     * <p>
     * The Ship's speed on the y axis.</p>
     */
    private double ySpeed;
    public double getYSpeed() {
        return ySpeed;
    }
    public void setYSpeed(double val) {
        ySpeed = val;
        checkSpeed();
    }
    public void changeYSpeed(double val) {
        ySpeed += val;
        checkSpeed();
    }
    /**
     * <p>
     * Ensures this MovementEntity stays within the games max speed.</p>
     */
    private void checkSpeed() {
        double speed = Math.hypot(xSpeed, ySpeed);
        if (speed > maxSpeed) {
            speed = maxSpeed / speed;
            xSpeed *= speed;
            ySpeed *= speed;
        }
    }

    /**
     * <p>
     * Creates a new instance of a MobileShipEntity.</p>
     *
     * @param initXSpeed The speed on the x axis.
     * @param initYSpeed The speed on the y axis.
     */
    public MovementEntity(double initXSpeed, double initYSpeed) {
        xSpeed = initXSpeed;
        ySpeed = initYSpeed;
    }

}
