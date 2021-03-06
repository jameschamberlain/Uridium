package net.uridium.game.gameplay;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import net.uridium.game.gameplay.entity.EnemySpawner;
import net.uridium.game.gameplay.entity.damageable.enemy.Enemy;
import net.uridium.game.gameplay.tile.*;
import net.uridium.game.server.ServerLevel;

import java.util.ArrayList;

import static net.uridium.game.gameplay.Level.TILE_HEIGHT;
import static net.uridium.game.gameplay.Level.TILE_WIDTH;

/**
 * Class to create server side levels from json files
 */
public class LevelFactory {

    /**
     * Create a ServerLevel object from a file handle to a json file
     * @param fileHandle The file handle of the json file
     * @return The level created
     */
    public static ServerLevel buildLevelFromFileHandle(FileHandle fileHandle) {
        String json = fileHandle.readString();
        return buildLevelFromJSON(json);
    }

    /**
     * Builds a server side level from json
     * @param json The json to create the level from
     * @return The level created
     */
    public static ServerLevel buildLevelFromJSON(String json) {
        // GET THE MAIN OBJECT FROM THE JSON FILE
        JsonValue level = new JsonReader().parse(json);

        // GET THE LEVEL ID
        int id = level.getInt("id");

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

        JsonValue jsonDoors = level.get("doors");
        for(JsonValue jsonDoor : jsonDoors.iterator()) {
            int x = jsonDoor.getInt("x");
            int y = jsonDoor.getInt("y");
            int dest = jsonDoor.getInt("dest");
            int entrance = jsonDoor.getInt("entrance");

            DoorTile door = (DoorTile) grid[x][y];
            door.setDest(dest);
            door.setEntrance(entrance);

            if(x == 0) door.setRot(90);
            else if(y == 0) door.setRot(180);
            else if(x == gridWidth - 1) door.setRot(270);
        }

        // GET THE PLAYER SPAWN
        JsonValue jsonEntrances = level.get("entrances");
        ArrayList<Vector2> entrances = new ArrayList<>();
        for(JsonValue entrance : jsonEntrances.iterator()) {
            int x = entrance.getInt("x");
            int y = entrance.getInt("y");

            entrances.add(new Vector2(x * TILE_WIDTH, y * TILE_HEIGHT));
        }

        JsonValue jsonEnemySpawners = level.get("spawners");
        ArrayList<EnemySpawner> enemySpawners = new ArrayList<>();
        if(jsonEnemySpawners.size > 0) {
            for (JsonValue enemySpawn : jsonEnemySpawners.iterator()) {
                int x = enemySpawn.getInt("x");
                int y = enemySpawn.getInt("y");
                JsonValue jsonSpawnerMonsterTypes = enemySpawn.get("types");
                ArrayList<Enemy.Type> types = new ArrayList<>();
                jsonSpawnerMonsterTypes.forEach(type -> types.add(Enemy.Type.valueOf(type.toString())));
                int numEnemies = enemySpawn.getInt("numEnemies");
                long spawnRate = enemySpawn.getLong("spawnRate");
                enemySpawners.add(new EnemySpawner(x, y, types, numEnemies, spawnRate));
            }
        }

        return new ServerLevel(id, grid, gridWidth, gridHeight, entrances, enemySpawners);
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
            default:
                return new GroundTile(x, y);
        }
    }
}
