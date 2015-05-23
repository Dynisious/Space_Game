package SpaceGame.EntityTypes;

import static SpaceGame.Application.normaliseAngle;
import java.awt.Point;
/**
 * <p>
 * Holds the values necessary for a StaticEntity to exist in a SpaceCombat
 * GameModule.</p>
 *
 * @author Dynisious 22/05/2015
 * @versions 0.0.1
 */
public final class StaticEntity {
    /**
     * <p>
     * The Ship's X coordinate.</p>
     */
    public int x;
    /**
     * <p>
     * The Ship's Y coordinate.</p>
     */
    public int y;
    /**
     * <p>
     * The raw damage this StaticEntity does when it collides with
     * another.<p>
     */
    private double collideDamage;
    public double getCollideDamage() {
        return collideDamage;
    }
    /**
     * <p>
     * The StaticEntity's direction of face.</p>
     */
    private double direction;
    public double getDirection() {
        return direction;
    }
    public void setDirection(double val) {
        direction = normaliseAngle(val);
    }
    /**
     * <p>
     * The angle offset to the top right corner of this StaticEnitity's bounding
     * box.</p>
     */
    private double boundingAngle;
    public double getBoundingAngle() {
        return boundingAngle;
    }
    /**
     * <p>
     * The cord length to the top right corner of this StaticEntity's bounding
     * box.</p>
     */
    private int boundingLength;
    public int getBoundingLength() {
        return boundingLength;
    }
    /**
     * <p>
     * The coordinates of the top right corner of this StaticEntity's bounding
     * box.</p>
     */
    private Point topRight;
    public Point getTopRight() {
        return topRight;
    }
    /**
     * <p>
     * The coordinates of the bottom left corner of this StaticEntity's bounding
     * box.</p>
     */
    private Point bottomLeft;
    public Point getBottomLeft() {
        return bottomLeft;
    }

    /**
     * <p>
     * Creates a new instance of a StaticEntity.</p>
     *
     * @param initCollideDamage The raw damage this StaticEntity does when
     *                          it collides with another.
     * @param width             The width of the collision box of this
     *                          StaticEntity.
     * @param height            The height of the collision box of this
     *                          StaticEntity.
     */
    public StaticEntity(double initCollideDamage, int width, int height) {
        collideDamage = initCollideDamage;
        width /= 2;
        height /= 2;
        topRight = new Point(width, height);
        bottomLeft = new Point(-width, -height);
        boundingAngle = Math.atan(height / width);
        boundingLength = (int) Math.hypot(width, height);
    }

    /**
     * <p>
     * Creates a new instance of a StaticEntity.</p>
     *
     * @param initX              The x coordinate of this StaticEntity.
     * @param initY              The y coordinate of this StaticEntity.
     * @param initCollideDamage  The raw damage this StaticEntity does when
     *                           it collides with another.
     * @param initDirection      The direction of face.
     * @param initTopRight       The coordinates of the top right corner of this
     *                           StaticEntity's bounding box.
     * @param initBottomLeft     The coordinates of the bottom left corner of
     *                           this StaticEntity's bounding box.
     * @param initBoundingAngle  The angle to the top right corner of this
     *                           StaticEntity's bounding box.
     * @param initBoundingLength The length of the cord to the top right corner
     *                           of this StaticEntity's bounding box.
     */
    private StaticEntity(int initX, int initY, double initCollideDamage,
                         double initDirection, Point initTopRight,
                         Point initBottomLeft, double initBoundingAngle,
                         int initBoundingLength) {
        x = initX;
        y = initY;
        collideDamage = initCollideDamage;
        direction = initDirection;
        topRight = initTopRight;
        bottomLeft = initBottomLeft;
        boundingAngle = initBoundingAngle;
        boundingLength = initBoundingLength;
    }

    /**
     * <p>
     * Returns a new instance of this StaticEntity.
     *
     * @param initX         The x coordinate.
     * @param intY          The y coordinate.
     * @param initDirection The direction of face.
     *
     * @return The new instance.
     */
    public StaticEntity getInstance(int initX, int intY,
                                    double initDirection) {
        return new StaticEntity(initX, intY, collideDamage, initDirection,
                topRight, bottomLeft, boundingAngle, boundingLength);
    }
}
