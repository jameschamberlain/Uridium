import java.util.Scanner;

public class Main {

    /**
     * Grid in which the world is based
     */
    private static Grid grid;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        grid = getGrid(scan);
        placeObjects(scan);
        scan.close();
        grid.printGrid();
    }


    /**
     *
     * Creates a grid with the specification laid
     * out by the user
     *
     * @param scan The console input object
     * @return The grid
     */
    private static Grid getGrid(Scanner scan) {
        System.out.println("Enter a Grid size:");
        System.out.print("X: ");
        int x = scan.nextInt();
        System.out.print("Y: ");
        int y = scan.nextInt();
        return new Grid(x, y);
    }


    /**
     *
     * Places objects onto the grid,
     * including the start & end
     * positions as well as any obstacles
     * there may be
     *
     * @param scan The console input object
     */
    private static void placeObjects(Scanner scan) {
        // Add the start position to the grid
        System.out.println("Enter start position:");
        int x = grid.getValidX(scan);
        int y = grid.getValidY(scan);
        grid.addObject(ObjectType.START, x, y);

        // Add the end position to the grid
        System.out.println("Enter end position:");
        x = grid.getValidX(scan);
        y = grid.getValidY(scan);
        grid.addObject(ObjectType.END, x, y);

        // Add any obstacles to the grid
        System.out.println("How many obstacles?");
        int numOfObstacles = scan.nextInt();
        for (int i = 1; i <= numOfObstacles; i++) {
            System.out.println("Position of obstacle " + i + ":");
            x = grid.getValidX(scan);
            y = grid.getValidY(scan);
            grid.addObject(ObjectType.OBSTACLE, x, y);
        }
    }

}