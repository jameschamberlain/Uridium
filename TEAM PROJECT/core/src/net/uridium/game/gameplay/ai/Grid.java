package net.uridium.game.gameplay.ai;

import com.badlogic.gdx.math.Vector2;

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
    private Object[][] grid;

    /**
     * Constructor for a new grid object
     *
     * @param x The size of the grid in the x-axis
     * @param y The size of the grid in the y-axis
     */
    Grid(int x, int y) {
        this.x = x;
        this.y = y;
        this.grid = new Object[y][x];
        setupGrid();
    }

    int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    /**
     * Adds objects to the grid, including
     * the start position, end position, and
     * any obstacles
     *
     * @param object The object to be placed
     */
    void addObject(Object object) {
        int xCoord = (int) object.getPosition().x;
        int yCoord = (int) object.getPosition().y;
        grid[yCoord][xCoord] = object;
    }

    private void setupGrid() {
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                grid[i][j] = new Object(ObjectType.PATH, new Vector2(j, i));
            }
        }
    }

    /**
     * Resets the grid to a state of all paths,
     * hence there are no objects on the grid
     */
    void resetGrid() {
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                if (grid[i][j].getSymbol() == 'S' || grid[i][j].getSymbol() == 'E' || grid[i][j].getSymbol() == 'Z') {
                    grid[i][j] = new Object(ObjectType.PATH, new Vector2(j, i));
                }
            }
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix;
        for (int i = y - 1; i >= 0; i--) {
            prefix = "";
            for (int j = 0; j < x; j++) {
                stringBuilder.append(prefix);
                prefix = " ";
                stringBuilder.append(grid[i][j].getSymbol());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
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


    /**
     * Get the grid
     *
     * @return The grid
     */
    Object[][] getGrid() {
        return grid;
    }
}