package net.uridium.game.gameplay.ai;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        // The grid size.
        Vector2 gridSize = new Vector2(5, 5);
        // The start coordinate.
        Vector2 start = new Vector2(1, 1);
        // The end coordinate.
        Vector2 end = new Vector2(4, 4);
        // The list of obstacles in the world.
        ArrayList<Vector2> obstacles = new ArrayList<>();
        obstacles.add(new Vector2(2, 2));
        obstacles.add(new Vector2(3, 3));
        obstacles.add(new Vector2(4, 3));
        // Constructor for a new pathfinder object
        Pathfinder pathfinder = new Pathfinder(gridSize, start, end, obstacles);
        // Find the path.
        ArrayList<Vector2> route = pathfinder.findPath();
        // Print the path.
        System.out.println(route);
    }

}
