package swegame.state;
/**
 * Class representing a cell and its possible content.
 */

public enum Cell {
    /**
     * Represents the empty cell.
     */
    EMPTY,
    /**
     * Represents a red cell.
     */
    RED,
    /**
     * Represents a blue cell.
     */
    BLUE;

    /**
     * Returns the instance represented by the value specified.
     *
     * @param value the value representing an instance
     * @return the instance represented by the value specified
     * @throws IllegalArgumentException if the value specified does not
     * represent an instance
     */
    public static Cell of(int value) {
        if (value < 0 || value >= values().length) {
            throw new IllegalArgumentException();
        }
        return values()[value];
    }

    /**
     * Returns the integer value that represents this instance.
     *
     * @return the integer value that represents this instance
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Returns the string representation of the cell.
     */
    public String toString() {
        return Integer.toString(ordinal());
    }

}
