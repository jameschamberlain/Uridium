import java.awt.*;

public class Object {

    /**
     * Position of the object in the world
     */
    private Point position;

    /**
     *
     * Constructor for a new object
     *
     * @param position The position of the object
     */
    public Object(Point position) {
        this.position = position;
    }

    /**
     *
     * Gets the position of the object
     *
     * @return The position of the object
     */
    public Point getPosition() {
        return position;
    }

    /**
     *
     * Sets the position of the object
     *
     * @param position The new position of the object
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}