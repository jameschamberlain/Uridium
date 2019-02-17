import java.awt.*;
import java.util.*;

class Pathfinder {


    /**
     * Base grid.
     */
    private Grid grid;
    /**
     * The 2D array of which acts as the coordinate
     * system for the world.
     */
    private Object[][] map;
    /**
     * The maximum x value in the grid.
     */
    private int maxX;
    /**
     * The maximum y value in the grid.
     */
    private int maxY;


    /**
     *
     * Constructor for a new pathfinding object.
     *
     * @param grid The grid for the world.
     */
    Pathfinder(Grid grid) {
        this.grid = grid;
        this.map = grid.getGrid();
        maxX = grid.getX() - 1;
        maxY = grid.getY() - 1;
    }


    Pathfinder(Tuple gridSize, Point start, Point end, ArrayList<Point> obstacles) {
        this.grid = new Grid(gridSize.getX(), gridSize.getY());
        grid.addObject(new Object(ObjectType.START, start));
        grid.addObject(new Object(ObjectType.END, end));
        for(Point obstacle : obstacles) {
            grid.addObject(new Object(ObjectType.OBSTACLE, obstacle));
        }
        this.map = grid.getGrid();
        maxX = grid.getX() - 1;
        maxY = grid.getY() - 1;
    }


    /**
     *
     * Uses the A* search algorithm to find a path
     * through the world from a start node to an
     * end node.
     *
     * @return An arraylist of coordinates for the route.
     */
    ArrayList<Object> findPath() {
        System.out.println(grid.toString());
        // Setup a list of visible paths.
        PriorityQueue<Object> paths = new PriorityQueue<Object>(10, new sortByF());
        Object currentNode = getStartNode();
        // Calculate the heuristic for every node in the world.
        calculateHeuristic();

        // Setup points surrounding the current node
        Point up = new Point(-1, -1);
        Point down = new Point(-1, -1);
        Point left = new Point(-1, -1);
        Point right = new Point(-1, -1);

        boolean hasReachedGoal = false;
        /*
         While the goal has not been reached continue to travel
         through the world.
          */
        while (!hasReachedGoal) {
            currentNode.setType(ObjectType.VISITED_PATH);
            /*
            Use a control flow to check the surrounding nodes for available paths
            while also making sure that the algorithm doesn't attempt to process a node
            that would be outside the grid.
             */
            if (currentNode.getPosition().x == 0) {
                if (currentNode.getPosition().y == 0) {
                    up.setLocation(0, 1);
                    down.setLocation(-1, -1);
                    left.setLocation(-1, -1);
                    right.setLocation(1, 0);
                }
                else if (currentNode.getPosition().y == maxY) {
                    up.setLocation(-1, -1);
                    down.setLocation(0, maxY - 1);
                    left.setLocation(-1, -1);
                    right.setLocation(1, maxY);
                }
                else {
                    up.setLocation(0, currentNode.getPosition().y + 1);
                    down.setLocation(0, currentNode.getPosition().y - 1);
                    left.setLocation(-1, -1);
                    right.setLocation(1, currentNode.getPosition().y);
                }
            }
            else if (currentNode.getPosition().x == maxX) {
                if (currentNode.getPosition().y == 0) {
                    up.setLocation(maxX, 1);
                    down.setLocation(-1, -1);
                    left.setLocation(maxX - 1, 0);
                    right.setLocation(-1, -1);
                }
                else if (currentNode.getPosition().y == maxY) {
                    up.setLocation(-1, -1);
                    down.setLocation(maxX, maxY - 1);
                    left.setLocation(maxX - 1, maxY);
                    right.setLocation(-1, -1);
                }
                else {
                    up.setLocation(maxX, currentNode.getPosition().y + 1);
                    down.setLocation(maxX, currentNode.getPosition().y - 1);
                    left.setLocation(maxX - 1, currentNode.getPosition().y);
                    right.setLocation(-1, -1);
                }
            }
            else {
                if (currentNode.getPosition().y == 0) {
                    up.setLocation(currentNode.getPosition().x, 1);
                    down.setLocation(-1, -1);
                    left.setLocation(currentNode.getPosition().x - 1, 0);
                    right.setLocation(currentNode.getPosition().x + 1, 0);
                }
                else if (currentNode.getPosition().y == maxY) {
                    up.setLocation(-1, -1);
                    down.setLocation(currentNode.getPosition().x, maxY - 1);
                    left.setLocation(currentNode.getPosition().x - 1, maxY);
                    right.setLocation(currentNode.getPosition().x + 1, maxY);
                }
                else {
                    up.setLocation(currentNode.getPosition().x, currentNode.getPosition().y + 1);
                    down.setLocation(currentNode.getPosition().x, currentNode.getPosition().y - 1);
                    left.setLocation(currentNode.getPosition().x - 1, currentNode.getPosition().y);
                    right.setLocation(currentNode.getPosition().x + 1, currentNode.getPosition().y);
                }
            }
            // For each surrounding valid node add it the list of paths.
            addNodeToPath(up, currentNode, paths);
            addNodeToPath(down, currentNode, paths);
            addNodeToPath(left, currentNode, paths);
            addNodeToPath(right, currentNode, paths);

            /*
            Get the first node from the list and set it as
            the current node. Then check if it is the end
            goal node. If it is then end the while loop.
            Otherwise go back through the loop.
             */
            currentNode = paths.poll();
            paths.remove(0);
            currentNode.setSymbol('Z');
            if (currentNode.getType() == ObjectType.END) {
                hasReachedGoal = true;
            }
        }
        // Setup and populate a list of the route found.
        ArrayList<Object> route = new ArrayList<>();
        while (currentNode.getPrecedPoint() != null) {
            route.add(currentNode);
            currentNode = currentNode.getPrecedPoint();
        }
        Collections.reverse(route);
        return route;
    }


    /**
     *
     * Get the start node form the grid.
     *
     * @return The start node.
     */
    private Object getStartNode() {
        for (int i = grid.getY() - 1; i >= 0; i--) {
            for (int j = 0; j < grid.getX(); j++) {
                if (map[i][j].getType() == ObjectType.START) {
                    return map[i][j];
                }
            }
        }
        return null;
    }


    /**
     * Calculate the heuristic value for each
     * node in the grid.
     */
    private void calculateHeuristic() {
        Object endNode = null;
        for (int i = grid.getY() - 1; i >= 0; i--) {
            for (int j = 0; j < grid.getX(); j++) {
                if (map[i][j].getType() == ObjectType.END) {
                    endNode = map[i][j];
                }
            }
        }
        for (int i = grid.getY() - 1; i >= 0; i--) {
            for (int j = 0; j < grid.getX(); j++) {
                if (map[i][j].getType() == ObjectType.START || map[i][j].getType() == ObjectType.PATH) {
                    float euclidian = calcPythag(map[i][j].getPosition(), endNode.getPosition());
                    map[i][j].setH(euclidian);
                }
            }
        }
    }


    /**
     *
     * Helper for calculating the heuristic.
     * Calculates pythagoras for two grid points.
     *
     * @param point1 A node from the grid.
     * @param point2 A node from the grid.
     *
     * @return The result of the pythagoras calculation.
     */
    private float calcPythag(Point point1, Point point2) {
        float xSquare = (point2.x - point1.x) * (point2.x - point1.x);
        float ySquare = (point2.y - point1.y) * (point2.y - point1.y);
        float addition = xSquare + ySquare;
        return (float) Math.sqrt(addition);
    }


    /**
     *
     * Adds nodes the the path priority queue.
     *
     * @param point The location of the point to be added.
     * @param currentNode The current node.
     * @param paths The paths priority queue
     */
    private void addNodeToPath(Point point, Object currentNode, PriorityQueue<Object> paths) {
        int xVal = point.x;
        int yVal = point.y;
        if (!(xVal == -1) && !(yVal == -1)) {
            float newG = currentNode.getG() + 1;
            Object object = map[yVal][xVal];
            if (!(object.getType() == ObjectType.VISITED_PATH) && !(object.getType() == ObjectType.OBSTACLE)) {
                if (object.getG() > newG) {
                    object.setG(newG);
                    object.setF(object.getG() + object.getH());
                    object.setPrecedPoint(currentNode);
                    paths.add(object);
                }
            }
        }
    }

}


/**
 * A custom comparator to oder objects based on the f value.
 * f = g + h.
 */
class sortByF implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        return Float.compare(o1.getF(), o2.getF());
    }
}