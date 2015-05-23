package SpaceGame.EntityTypes;

/**
 * <p>
 Contains values for changing the velocity of a MovementEntity.</p>
 *
 * @author Dynisious 23/05/2015
 * @versions 0.0.1
 */
public class MobileEntity {
    /**
     * <p>
 Contains the values for this MobileEntity' velocity.</p>
     */
    MovementEntity mobileEntity;
    public MovementEntity getMobileEntity() {
        return mobileEntity;
    }
    /**
     * <p>
 The values for this MobileEntity' position.</p>
     */
    StaticEntity positionEntity;
    public StaticEntity getPositionEntity() {
        return positionEntity;
    }
    /**
     * <p>
     * The Ship's acceleration forward.</p>
     */
    private double forwardAcceleration;
    public double getForwardAcceleration() {
        return forwardAcceleration;
    }
    /**
     * <p>
     * The Ship's acceleration on the backwards.</p>
     */
    private double reverseAcceleration;
    public double getReverseAcceleration() {
        return reverseAcceleration;
    }
    /**
     * <p>
     * The Ship's acceleration side to side.</p>
     */
    private double strafeAcceleration;
    public double getStrafeAcceleration() {
        return strafeAcceleration;
    }
    /**
     * <p>
     * The radians a tick this can change it's face.</p>
     */
    private double turnSpeed;
    public double getTurnSpeed() {
        return turnSpeed;
    }

    /**
     * <p>
 Creates a new MobilitySytem with the given MovementEntity.</p>
     *
     * @param initPositionEntity      The values for this MobilitySystem as a
     *                                StaticEnity.
     * @param initForwardAcceleration The forward acceleration for this
     *                                MobiltySystem.
     * @param initReverseAcceleraion  The reverse acceleration for this
     *                                MobilitySystem.
     * @param initStrafeAcceleration  The strafe acceleration for this
     *                                MobilitySytem.
     * @param initTurnSpeed
     */
    public MobileEntity(StaticEntity initPositionEntity,
                        double initForwardAcceleration,
                        double initReverseAcceleraion,
                        double initStrafeAcceleration, double initTurnSpeed) {
        positionEntity = initPositionEntity;
        forwardAcceleration = initForwardAcceleration;
        reverseAcceleration = initReverseAcceleraion;
        strafeAcceleration = initStrafeAcceleration;
        turnSpeed = initTurnSpeed;
    }

    /**
     * <p>
 Creates a new MobilitySytem with the given MovementEntity.</p>
     *
     * @param initMobileEntity        The values for this MobilitySytems'
     *                                velocity.
     * @param initPositionEntity      The values for this MobiltySystems'
     *                                position.
     * @param initForwardAcceleration The forward acceleration for this
     *                                MobiltySystem.
     * @param initReverseAcceleraion  The reverse acceleration for this
     *                                MobilitySystem.
     * @param initStrafeAcceleration  The strafe acceleration for this
     *                                MobilitySytem.
     * @param initTurnSpeed           The number of radians a tick this can
     *                                change it's face.
     */
    private MobileEntity(MovementEntity initMobileEntity,
                         StaticEntity initPositionEntity,
                         double initForwardAcceleration,
                         double initReverseAcceleraion,
                         double initStrafeAcceleration, double initTurnSpeed) {
        mobileEntity = initMobileEntity;
        positionEntity = initPositionEntity;
        forwardAcceleration = initForwardAcceleration;
        reverseAcceleration = initReverseAcceleraion;
        strafeAcceleration = initStrafeAcceleration;
        turnSpeed = initTurnSpeed;
    }

    /**
     * <p>
     * Creates a new instance of this MobiltySystem with the given mobile and
     * static entity values.</p>
     *
     * @param initMobileEntity The values for this MobileEntity's velocity.
     * @param initX            The x coordinate of this MobileEntity.
     * @param initY            The y coordinate of this MobileEntity.
     * @param initDirection    The direction of face for this MobileEntity.
     *
     * @return The new instance of this MobileEntity.
     */
    public MobileEntity getInstance(MovementEntity initMobileEntity, int initX,
                                    int initY, double initDirection) {
        return new MobileEntity(initMobileEntity, positionEntity.getInstance(
                initX, initY, initDirection),
                forwardAcceleration, reverseAcceleration, strafeAcceleration,
                turnSpeed);
    }

}
