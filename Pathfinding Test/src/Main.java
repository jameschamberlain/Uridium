import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /**
     * Grid in which the world is based
     */
    private static Grid grid;

    private static final long MEGABYTE = 1024L * 1024L;

    static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        grid = new Grid(5, 5);
        placeObjects();
        grid.printGrid();
        Pathfinder pathfinder = new Pathfinder(grid);
        ArrayList<Object> route = pathfinder.findPath();
        System.out.println(route);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory in bytes: " + memory);
        System.out.println("Used memory in megabytes: " + bytesToMegabytes(memory));

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime);

    }


    /**
     * Places objects onto the grid,
     * including the start & end
     * positions as well as any obstacles
     * there may be
     */
    private static void placeObjects() {
        // Add the start position to the grid
        Object startNode = new Object(ObjectType.START, new Point(1, 1));
        grid.addObject(startNode);

        // Add the end position to the grid
        Object endNode = new Object(ObjectType.END, new Point(4, 4));
        grid.addObject(endNode);

        // Add any obstacles to the grid
        Object obstacle = new Object(ObjectType.OBSTACLE, new Point(2, 2));
        grid.addObject(obstacle);
        obstacle = new Object(ObjectType.OBSTACLE, new Point(3, 3));
        grid.addObject(obstacle);
        obstacle = new Object(ObjectType.OBSTACLE, new Point(4, 3));
        grid.addObject(obstacle);
    }

}