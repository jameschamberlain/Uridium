package net.uridium.game.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.entity.Bullet;
import net.uridium.game.gameplay.entity.Player;
import net.uridium.game.gameplay.entity.myPackage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Server{
    //Here is for network
    private ServerSocket ss;
    private Socket s;
    private BufferedReader bfr;
    private ObjectOutputStream oos;
    private static myPackage aPackage;

    //Here is for data
    public int gridWidth;
    public int gridHeight;

    public static final float TILE_WIDTH = 64;
    public static final float TILE_HEIGHT = 64;

    ArrayList<String> rows;
    public Tile[][] grid;

    float xOffset;
    float yOffset;

    Player player;
    Vector2 playerSpawn;

    Texture enemyTexture;

    HashMap<Integer, Vector2> players;
    ArrayList<Bullet> bullets;
    ArrayList<Bullet> bulletsToRemove;
    ArrayList<Rectangle> enemies;
    ArrayList<Rectangle> enemiesToRemove;

    //For read data from txt
    FileInputStream inputStream;
    BufferedReader bufferedReader;

    public Server() throws IOException {

        ss = new ServerSocket(9988);
        System.out.println("Server Starts");
        //Texture texture = new Texture(Gdx.files.internal("penguin_square.png"));
        System.out.println("get the photo");
        initLevel();
        new Thread(new accepter(ss)).start();

    }

    public class accepter implements Runnable {
        private ServerSocket ss;

        public accepter(ServerSocket ss) {
            this.ss=ss;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    s = ss.accept();
                    aPackage = new myPackage();
                    System.out.println("Connecnted");
                    oos = new ObjectOutputStream(s.getOutputStream());
                    bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String str = bfr.readLine();
                    String xy[] = str.split(" ");

                    Float x = Float.parseFloat(xy[0]);
                    Float y = Float.parseFloat(xy[1]);
                    Vector2 player = new Vector2(x, y);

                    aPackage.setPlayers(s.getPort(), player);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(new Sender(oos)).start();
                new Thread(new Server.Receiver(bfr, s)).start();
            }
        }
    }

    public class Receiver implements Runnable {
        private BufferedReader bfr;
        Socket s;

        public Receiver(BufferedReader bfr, Socket s) {
            this.bfr = bfr;
            this.s = s;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String str = bfr.readLine();
                    String xy[] = str.split(" ");

                    Float x = Float.parseFloat(xy[0]);
                    Float y = Float.parseFloat(xy[1]);
                    Vector2 player = new Vector2(x, y);
                    System.out.println("Server Receive:" + xy[0] + "------" + xy[1]);
                    aPackage.setPlayers(s.getPort(), player);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Sender implements Runnable {
        private ObjectOutputStream oos;

        public Sender(ObjectOutputStream oos) {
            this.oos = oos;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    oos.writeObject(aPackage);
                    oos.reset();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Player createPlayer() {
        return null;
    }

    public void initLevel() {

        bullets = new ArrayList<>();
        bulletsToRemove = new ArrayList<>();
        enemies = new ArrayList<>();
        enemiesToRemove = new ArrayList<>();

        rows = new ArrayList<>();

        try {
            inputStream = new FileInputStream("C:\\Users\\a\\Desktop\\TEAM PROJECT\\uridium\\TEAM PROJECT\\core\\assets\\level1.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                rows.add(line);
            }

            //close
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gridHeight = rows.size();
        gridWidth = rows.get(0).length();

        grid = new Tile[gridWidth][gridHeight];

        xOffset = GAME_WIDTH - (gridWidth * TILE_WIDTH);
        xOffset /= 2;
        yOffset = GAME_HEIGHT - (gridHeight * TILE_HEIGHT);
        yOffset /= 2;

        String row;
        boolean isObstacle = false;
        for (int j = 0; j < gridHeight; j++) {
            row = rows.get(j);
            for (int i = 0; i < gridWidth; i++) {
                char c = row.charAt(i);
                isObstacle = false;

                switch (c) {
                    case 'W':
                        isObstacle = true;
                        break;
                    case 'O':
                        isObstacle = true;
                        break;
                    case 'D':
                        break;
                    case 'P':
                        playerSpawn = new Vector2(xOffset + i * TILE_WIDTH, yOffset + j * TILE_HEIGHT);
                        System.out.println("Before Send");
                        System.out.println(playerSpawn.x + " " + playerSpawn.y);
                        System.out.println("After Send");
                    default:
                        break;
                }
                grid[i][gridHeight - 1 - j] = new Tile(i, gridHeight - 1 - j, xOffset, yOffset, isObstacle);
            }
        }
        //player = new Player(playerSpawn.x - 27.5f, playerSpawn.y - 27.5f, 55, 55);

    }

    public boolean checkPlayerCollisions() {
        Rectangle playerBody = player.getBody();
        Rectangle overlap = new Rectangle();

        Tile tile;
        Rectangle obstacle;
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                tile = grid[i][j];

                if (tile.isObstacle) {
                    obstacle = tile.getBody();

                    if (Intersector.intersectRectangles(playerBody, obstacle, overlap)) {
                        Rectangle playerBodyOldX = new Rectangle(player.lastPos.x, playerBody.y, playerBody.width, playerBody.height);
                        if (!overlap.overlaps(playerBodyOldX))
                            player.goToLastXPos();

                        Rectangle playerBodyOldY = new Rectangle(playerBody.x, player.lastPos.y, playerBody.width, playerBody.height);
                        if (!overlap.overlaps(playerBodyOldY))
                            player.goToLastYPos();

                        return true;
                    }
                }
            }
        }

        return false;
    }


    public static void main(String[] arg) {
        try {
            Server s = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}