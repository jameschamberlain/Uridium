import java.awt.*;
import java.util.*;

public class Pathfinder {


    /**
     * Base grid
     */
    private Grid grid;
    private Object[][] map;
    private int maxX;
    private int maxY;
    private HashMap<String, Point> surroundingNodes = new HashMap<>();


    public Pathfinder(Grid grid) {
        this.grid = grid;
        this.map = grid.getGrid();
        maxX = grid.getX() - 1;
        maxY = grid.getY() - 1;
        setupSurroundingNodes();
    }


    ArrayList<Object> findPath() {
        //TreeMap<Point, Object> sortedPaths = new TreeMap<Point, Object>(customComparator);
        TreeMap<Float, Object> sortedPaths = new TreeMap<>(floatComparator);
        ArrayList<Object> route = new ArrayList<>();
        Object currentNode = getStartNode();
        calculateHeuristic();
        boolean hasReachedGoal = false;
        while (!hasReachedGoal) {
            currentNode.setType(ObjectType.VISITED_PATH);
            System.out.println(currentNode.getPosition());
            grid.printGrid();
            if (currentNode.getPosition().x == 0) {
                if (currentNode.getPosition().y == 0) {
                    surroundingNodes.replace("up", new Point(0, 1));
                    surroundingNodes.replace("down", new Point(-1, -1));
                    surroundingNodes.replace("left", new Point(-1, -1));
                    surroundingNodes.replace("right", new Point(1, 0));
                }
                else if (currentNode.getPosition().y == maxY) {
                    surroundingNodes.replace("up", new Point(-1, -1));
                    surroundingNodes.replace("down", new Point(0, maxY - 1));
                    surroundingNodes.replace("left", new Point(-1, -1));
                    surroundingNodes.replace("right", new Point(1, maxY));
                }
                else {
                    surroundingNodes.replace("up", new Point(0, currentNode.getPosition().y + 1));
                    surroundingNodes.replace("down", new Point(0, currentNode.getPosition().y - 1));
                    surroundingNodes.replace("left", new Point(-1, -1));
                    surroundingNodes.replace("right", new Point(1, currentNode.getPosition().y));
                }
            }
            else if (currentNode.getPosition().x == maxX) {
                if (currentNode.getPosition().y == 0) {
                    surroundingNodes.replace("up", new Point(maxX, 1));
                    surroundingNodes.replace("down", new Point(-1, -1));
                    surroundingNodes.replace("left", new Point(maxX - 1, 0));
                    surroundingNodes.replace("right", new Point(-1, -1));
                }
                else if (currentNode.getPosition().y == maxY) {
                    surroundingNodes.replace("up", new Point(-1, -1));
                    surroundingNodes.replace("down", new Point(maxX, maxY - 1));
                    surroundingNodes.replace("left", new Point(maxX - 1, maxY));
                    surroundingNodes.replace("right", new Point(-1, -1));
                }
                else {
                    surroundingNodes.replace("up", new Point(maxX, currentNode.getPosition().y + 1));
                    surroundingNodes.replace("down", new Point(maxX, currentNode.getPosition().y - 1));
                    surroundingNodes.replace("left", new Point(maxX - 1, currentNode.getPosition().y));
                    surroundingNodes.replace("right", new Point(-1, -1));
                }
            }
            else {
                if (currentNode.getPosition().y == 0) {
                    surroundingNodes.replace("up", new Point(currentNode.getPosition().x, 1));
                    surroundingNodes.replace("down", new Point(-1, -1));
                    surroundingNodes.replace("left", new Point(currentNode.getPosition().x - 1, 0));
                    surroundingNodes.replace("right", new Point(currentNode.getPosition().x + 1, 0));
                }
                else if (currentNode.getPosition().y == maxY) {
                    surroundingNodes.replace("up", new Point(-1, -1));
                    surroundingNodes.replace("down", new Point(currentNode.getPosition().x, maxY - 1));
                    surroundingNodes.replace("left", new Point(currentNode.getPosition().x - 1, maxY));
                    surroundingNodes.replace("right", new Point(currentNode.getPosition().x + 1, maxY));
                }
                else {
                    surroundingNodes.replace("up", new Point(currentNode.getPosition().x, currentNode.getPosition().y + 1));
                    surroundingNodes.replace("down", new Point(currentNode.getPosition().x, currentNode.getPosition().y - 1));
                    surroundingNodes.replace("left", new Point(currentNode.getPosition().x - 1, currentNode.getPosition().y));
                    surroundingNodes.replace("right", new Point(currentNode.getPosition().x + 1, currentNode.getPosition().y));
                }
            }
            for (String node : surroundingNodes.keySet()) {
                int xVal = surroundingNodes.get(node).x;
                int yVal = surroundingNodes.get(node).y;
                if (!(xVal == -1) && !(yVal == -1)) {
                    float newG = currentNode.getG() + 1;
                    Object object = map[yVal][xVal];
                    if (!(object.getType() == ObjectType.VISITED_PATH) && !(object.getType() == ObjectType.OBSTACLE)) {
                        if (object.getG() > newG) {
                            object.setG(newG);
                            object.setF(object.getG() + object.getH());
                            object.setPrecedPoint(currentNode);
                            //sortedPaths.put(object.getPosition(), object);
                            sortedPaths.put(object.getF(), object);
                        }
                    }
                }
            }
            currentNode = sortedPaths.pollLastEntry().getValue();
            currentNode.setSymbol('Z');
            if (currentNode.getType() == ObjectType.END) {
                hasReachedGoal = true;
            }
        }

        System.out.println("We did it!");
        return route;
    }


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


    private float calcPythag(Point point1, Point point2) {
        float xSquare = (point2.x - point1.x) * (point2.x - point1.x);
        float ySquare = (point2.y - point1.y) * (point2.y - point1.y);
        float addition = xSquare + ySquare;
        return (float) Math.sqrt(addition);
    }


    private void setupSurroundingNodes() {
        surroundingNodes.put("up", new Point(-1, -1));
        surroundingNodes.put("down", new Point(-1, -1));
        surroundingNodes.put("left", new Point(-1, -1));
        surroundingNodes.put("right", new Point(-1, -1));
    }


    Comparator<Map.Entry<Point, Object>> customComparator = new Comparator<Map.Entry<Point, Object>>() {
        @Override
        public int compare(Map.Entry<Point, Object> o1, Map.Entry<Point, Object> o2) {
            return Float.compare(o1.getValue().getF(), o2.getValue().getF());
        }
    };

    Comparator<Float> floatComparator = new Comparator<Float>() {
        @Override
        public int compare(Float o1, Float o2) {
            return Float.compare(o1, o2);
        }
    };

}
