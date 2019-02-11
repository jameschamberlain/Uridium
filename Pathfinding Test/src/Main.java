import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final long MEGABYTE = 1024L * 1024L;

    static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Tuple gridSize = new Tuple(5, 5);
        Point start = new Point(1, 1);
        Point end = new Point(4, 4);
        ArrayList<Point> obstacles = new ArrayList<>();
        obstacles.add(new Point(2, 2));
        obstacles.add(new Point(3, 3));
        obstacles.add(new Point(4, 3));
        Pathfinder pathfinder = new Pathfinder(gridSize, start, end, obstacles);
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

}