package net.uridium.game.gameplay.ai;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        // The start coordinate.
        Vector2 start = new Vector2(6, 2);
        // The end coordinate.
        Vector2 end = new Vector2(12, 3);
        // The list of obstacles in the world.
        ArrayList<Vector2> obstacles = new ArrayList<>();
        obstacles.add(new Vector2(9, 0));
        obstacles.add(new Vector2(9, 1));
        obstacles.add(new Vector2(8, 2));
        obstacles.add(new Vector2(9, 2));
        obstacles.add(new Vector2(9, 4));
        obstacles.add(new Vector2(3, 5));
        obstacles.add(new Vector2(4, 5));
        obstacles.add(new Vector2(5, 5));
        obstacles.add(new Vector2(9, 5));
        obstacles.add(new Vector2(10, 5));
        obstacles.add(new Vector2(3, 6));
        obstacles.add(new Vector2(4, 6));
        obstacles.add(new Vector2(5, 6));
        // Constructor for a new pathfinder object
        Pathfinder pathfinder = new Pathfinder(obstacles);
        // Find the path.
        ArrayList<Vector2> route = pathfinder.findPath(start, end);
        // Print the path.
        System.out.println(route);
    }

}
