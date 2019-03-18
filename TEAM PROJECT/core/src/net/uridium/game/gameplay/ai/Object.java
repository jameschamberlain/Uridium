package net.uridium.game.gameplay.ai;

import com.badlogic.gdx.math.Vector2;

public class Object {

    /**
     * The type of object (e.g. start,
     * end, obstacle or path)
     */
    private ObjectType type;
    /**
     * Position of the object in the world
     */
    private Vector2 position;
    /**
     * The preceding object in the route
     */
    private Object precedPoint;
    /**
     * The cost of movement from the start node
     */
    private float g;
    /**
     * The cost of movement (Euclidean heuristic)
     * to the end node
     */
    private float h;
    private float f;
    /**
     * The symbol to be used when displaying
     * this object
     */
    private char symbol;

    /**
     * Constructor for a new object
     *
     * @param type     The type of the object
     * @param position The position of the object
     */
    Object(ObjectType type, Vector2 position) {
        this.type = type;
        this.position = position;
        this.precedPoint = null;
        switch (type) {
            case OBSTACLE:
                this.g = Integer.MAX_VALUE;
                this.h = -1;
                this.symbol = 'O';
                break;
            case START:
                this.g = 0;
                this.h = -1;
                this.symbol = 'S';
                break;
            case END:
                this.g = Integer.MAX_VALUE;
                this.h = 0;
                this.symbol = 'E';
                break;
            case PATH:
                this.g = Integer.MAX_VALUE;
                this.h = -1;
                this.symbol = '-';
                break;
        }
        this.f = this.g + this.h;
    }

    /**
     * Gets the position of the object
     *
     * @return The position of the object
     */
    Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the position of the object
     *
     * @param position The new position of the object
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Gets the type of the object
     *
     * @return The type of the object
     */
    ObjectType getType() {
        return type;
    }

    /**
     * Sets the type of the object
     *
     * @param type The new type of the object
     */
    void setType(ObjectType type) {
        this.type = type;
    }

    /**
     * Gets the preceding point of the route
     *
     * @return The preceding point
     */
    Object getPrecedPoint() {
        return precedPoint;
    }

    /**
     * Sets the preceding point of the object
     *
     * @param precedPoint The new preceding point of the object
     */
    void setPrecedPoint(Object precedPoint) {
        this.precedPoint = precedPoint;
    }

    /**
     * Gets the cost of movement from the start node
     *
     * @return The cost of movement
     */
    float getG() {
        return g;
    }

    /**
     * Sets the cost of movement from the start node
     *
     * @param g The new cost of movement
     */
    void setG(float g) {
        this.g = g;
    }

    /**
     * Gets the cost of movement to the end node
     *
     * @return The cost of movement
     */
    float getH() {
        return h;
    }

    /**
     * Sets the cost of movement to the end node
     *
     * @param h The new cost of movement
     */
    void setH(float h) {
        this.h = h;
    }

    /**
     * Gets the symbol to represent the object
     *
     * @return The symbol
     */
    char getSymbol() {
        return symbol;
    }

    void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    float getF() {
        return f;
    }

    void setF(float f) {
        this.f = f;
    }

    @Override
    public String toString() {
        System.out.println("hi");
        float x = position.x;
        float y = position.y;
        return "(" + x + ", " + y + ")";
    }
}