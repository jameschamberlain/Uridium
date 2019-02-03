import java.util.Scanner;

public class Grid {

    /**
     * Size of the grid in the x-axis
     */
    private int x;
    /**
     * Size of the grid in the y-axis
     */
    private int y;
    /**
     * Base grid
     */
    private ObjectType[][] grid;

    /**
     *
     * Constructor for a new grid object
     *
     * @param x The size of the grid in the x-axis
     * @param y The size of the grid in the y-axis
     */
    Grid(int x, int y) {
        this.x = x;
        this.y = y;
        this.grid = new ObjectType[y][x];
        resetGrid();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    /**
     *
     * Adds objects to the grid, including
     * the start position, end position, and
     * any obstacles
     *
     * @param type The type of object to be placed
     * @param x The x-coordinate of the object
     * @param y The y-coordinate of the object
     */
    void addObject(ObjectType type, int x, int y) {
        switch (type) {
            case START:
                grid[y][x] = ObjectType.START;
                break;
            case END:
                grid[y][x] = ObjectType.END;
                break;
            case OBSTACLE:
                grid[y][x] = ObjectType.OBSTACLE;
                break;
            default:
                break;
        }
    }


    /**
     * Resets the grid to a state of all paths,
     * hence there are no objects on the grid
     */
    private void resetGrid() {
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                grid[i][j] = ObjectType.PATH;
            }
        }
    }


    /**
     * Prints the grid
     */
    void printGrid() {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix;
        for (int i = y-1; i >= 0; i--) {
            prefix = "";
            for (int j = 0; j < x; j++) {
                ObjectType type = grid[i][j];
                switch (type) {
                    case PATH:
                        stringBuilder.append(prefix);
                        prefix = " ";
                        stringBuilder.append("-");
                        break;
                    case START:
                        stringBuilder.append(prefix);
                        prefix = " ";
                        stringBuilder.append("S");
                        break;
                    case END:
                        stringBuilder.append(prefix);
                        prefix = " ";
                        stringBuilder.append("E");
                        break;
                    case OBSTACLE:
                        stringBuilder.append(prefix);
                        prefix = " ";
                        stringBuilder.append("O");
                        break;
                    default:
                        break;
                }
            }
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder);
    }


    /**
     *
     * Checks that the x-coordinate entered
     * by the user is valid
     *
     * @param scan The console input object
     * @return The x-coordinate
     */
    int getValidX(Scanner scan) {
        boolean isValidX = false;
        // Dummy value
        int x = -1;
        while (!isValidX) {
            System.out.print("X: ");
            x = scan.nextInt();
            if (x >= this.x || x < 0) {
                System.out.println("Out of range");
            }
            else {
                isValidX = true;
            }

        }
        return x;
    }


    /**
     *
     * Checks that the y-coordinate entered
     * by the user is valid
     *
     * @param scan The console input object
     * @return The y-coordinate
     */
    int getValidY(Scanner scan) {
        boolean isValidY = false;
        // Dummy value
        int y = -1;
        while (!isValidY) {
            System.out.print("Y: ");
            y = scan.nextInt();
            if (y >= this.y || y < 0) {
                System.out.println("Out of range");
            }
            else {
                isValidY = true;
            }

        }
        return y;
    }

}