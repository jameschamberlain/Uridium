package net.uridium.game.gameplay.ai;

public class Tuple {

    /**
     * Stores the left element of the tuple.
     */
    private final int x;
    /**
     * Stores the right element of the tuple.
     */
    private final int y;

    /**
     *
     * Constructor for a a new tuple.
     *
     * @param x The left element of the tuple.
     * @param y The right element of the tuple.
     */
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * Gets the left element of the tuple.
     *
     * @return The left element of the tuple.
     */
    public int getX() {
        return x;
    }

    /**
     *
     * Gets the right element of the tuple.
     *
     * @return The right element of the tuple.
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
