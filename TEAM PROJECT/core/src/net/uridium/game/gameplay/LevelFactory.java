package net.uridium.game.gameplay;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import net.uridium.game.gameplay.tile.*;

import java.util.ArrayList;
import java.util.logging.FileHandler;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;

public class LevelFactory {

    public static Level buildLevelFromFileHandle(FileHandle fileHandle) {
        String json = fileHandle.readString();
        return buildLevelFromJSON(json);
    }

    /**
     * Builds a game level from json
     * @param json The json to create the level from
     * @return The level created
     */
    public static Level buildLevelFromJSON(String json) {
        // GET THE MAIN OBJECT FROM THE JSON FILE
        JsonValue level = new JsonReader().parse(json);

        // GET THE GRID FROM THE JSON FILE
        JsonValue jsonGrid = level.get("grid");

        // GET THE WIDTH AND HEIGHT OF THE GRID
        int gridWidth = jsonGrid.getInt("width");
        int gridHeight = jsonGrid.getInt("height");

        // GET THE ARRAY OF ROWS FROM THE GRID OBJECT
        ArrayList<String> rows = new ArrayList<>();
        jsonGrid.get("rows").iterator().forEachRemaining(jsonValue -> rows.add(jsonValue.toString()));

        // TURN ROWS INTO 2D ARRAY OF TILES
        Tile[][] grid = new Tile[gridWidth][gridHeight];

        String row;
        char c;
        int k;
        for(int j = 0; j < gridHeight; j++) {
            row = rows.get(j);
            for(int i = 0; i < gridWidth; i++) {
                c = row.charAt(i);
                k = gridHeight - 1 - j;
                grid[i][k] = getTileFromChar(c, i, k);
            }
        }

        // GET THE PLAYER SPAWN
        JsonValue playerSpawn = level.get("playerSpawn");
        int playerSpawnX =  playerSpawn.getInt("x");
        int playerSpawnY = playerSpawn.getInt("y");

        return new Level(grid, gridWidth, gridHeight, new Vector2((playerSpawnX + 0.5f) * TILE_WIDTH, (playerSpawnY + 0.5f) * TILE_HEIGHT));
    }

    /**
     * Generates a tile depending on the character given, different characters map to different types of tiles
     * @param c The char
     * @param x The x coordinate of the tile in the grid
     * @param y The y coordinate of the tile in the grid
     * @return The tile which maps to the given char
     */
    private static Tile getTileFromChar(char c, int x, int y) {
        // DEPENDING ON THE CHAR, RETURNS THE CORRESPONDING TILE
        switch(c) {
            case 'W':
                return new WallTile(x, y);
            case 'O':
                return new CrateTile(x, y);
            case 'D':
                return new DoorTile(x, y);
            case 'E':
                return new enemySpawnTile(x, y);
            default:
                return new GroundTile(x, y);
        }
    }
}
