package net.uridium.game.server;

import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.LevelFactory;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.*;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The type Server.
 */
public class Server{
    //Here is for network
    private ServerSocket ss;
    private Socket s;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    /**
     * The Users.
     */
    CopyOnWriteArrayList<Sender> users;

    /**
     * The Levels.
     */
    ConcurrentHashMap<Integer, ServerLevel> levels;
    /**
     * The Current level.
     */
    ServerLevel currentLevel;
    /**
     * The Last update.
     */
    long lastUpdate;

    /**
     * The Game ended.
     */
    boolean gameEnded;

    /**
     * The Acceptor.
     */
    Thread acceptor;
    /**
     * The Level update.
     */
    Thread levelUpdate;

    /**
     * Overload the Constructor of Server
     * If there is no input. Assign server a default value
     *
     * @throws IOException the io exception
     */
    public Server() throws IOException {
        this(6666);
    }

    /**
     * Constructor of Server
     * Build up a server according to input.
     * Server contains two Threads.
     * One would keep listening client.
     * and the other one would keep sending data to clients that connected to this server
     *
     * @param port the port
     * @throws IOException the io exception
     */
    public Server(int port) throws IOException {
        try {
            ss = new ServerSocket(port);
        } catch (BindException e) {}

        init();
    }

    private void init() throws FileNotFoundException {
        levels = new ConcurrentHashMap<>();

        File f = new File("levels/level1.json");
        ServerLevel l = LevelFactory.buildLevelFromJSON(new Scanner(f).useDelimiter("\\A").next());
        currentLevel = l;
        levels.put(l.getID(), l);

        lastUpdate = System.currentTimeMillis();

        users = new CopyOnWriteArrayList<>();

        acceptor = new Thread(new Acceptor(ss));
        acceptor.start();
        levelUpdate = new Thread(this::levelUpdate);
        levelUpdate.start();

        gameEnded = false;
    }

    private void levelUpdate() {
        while (!gameEnded) {
            float delta = System.currentTimeMillis() - lastUpdate;
            delta /= 1000;
            currentLevel.update(delta);
            lastUpdate = System.currentTimeMillis();

            ArrayList<Msg> newMsgs = new ArrayList<>();
            currentLevel.getMsgs().drainTo(newMsgs);

            for(Sender s : users)
                for (Msg msg : newMsgs)
                    s.getMsgQueue().add(msg);

            int id;
            if((id = currentLevel.shouldChangeLevel()) != 0) {
                currentLevel.changedLevel();

                if(levels.get(id) == null) {
                    File f;
                    if(id == -1)
                        f = new File("levels/level_boss.json");
                    else
                        f = new File("levels/level" + id + ".json");

                    try {
                        ServerLevel newLevel = LevelFactory.buildLevelFromJSON(new Scanner(f).useDelimiter("\\A").next());

                        for(Player p : currentLevel.removePlayers()) {
                            p.setPosition(newLevel.getEntrance(currentLevel.getNextLevelEntrance()));
                            p.setVelocity(0, 0);
                            newLevel.addEntity(p);
                        }

                        currentLevel = newLevel;
                        currentLevel.setEnteredTime(System.currentTimeMillis());
                        levels.put(newLevel.getID(), currentLevel);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    ServerLevel newLevel = levels.get(id);

                    for(Player p : currentLevel.removePlayers()) {
                        if(p.getHealth() != 0) p.setPosition(newLevel.getEntrance(currentLevel.getNextLevelEntrance()));
                        p.setVelocity(0, 0);
                        newLevel.addEntity(p);
                    }

                    currentLevel = newLevel;
                    currentLevel.setEnteredTime(System.currentTimeMillis());
                }

                for(Sender s : users) {
                    LevelData levelData = currentLevel.getLevelData();
                    levelData.playerID = s.getPlayerID();
                    s.getMsgQueue().add(new Msg(Msg.MsgType.NEW_LEVEL, levelData));
                }
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gameEnded = currentLevel.isGameEnded();
        }

        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Keep waiting a new client to connect.
     * Once the connection built up.
     * The server to keep listening request from clients,
     * updating level data.
     * sending data to clients who connected to this server
     */
    public class Acceptor implements Runnable {
        private ServerSocket ss;

        /**
         * Instantiates a new Acceptor.
         *
         * @param ss the ss
         */
        public Acceptor(ServerSocket ss) {
            this.ss=ss;
        }

        @Override
        public void run() {
            System.out.println("Waiting for users ...");

            while (!gameEnded) {
                int playerID = 0;
                try {
                    s = ss.accept();
                    System.out.println("User connected ...");

                    oos = new ObjectOutputStream(s.getOutputStream());
                    ois = new ObjectInputStream(s.getInputStream());

                    playerID = currentLevel.getNumPlayers();
                    Vector2 playerSpawn = currentLevel.getEntrance(1);

                    Player player = new Player(playerID, playerSpawn, currentLevel.getAvailColour());
                    currentLevel.addEntity(player);

                    LevelData levelData = currentLevel.getLevelData();
                    levelData.playerID = playerID;
                    oos.writeObject(new Msg(Msg.MsgType.NEW_LEVEL, levelData));
                    oos.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Sender sender = new Sender(oos, s, playerID);
                Receiver receiver = new Receiver(ois, s, playerID);
                users.add(sender);

                new Thread(sender).start();
                new Thread(receiver).start();
            }

            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Keep receiving data from clients
     */
    public class Receiver implements Runnable {
        private ObjectInputStream ois;
        /**
         * The S.
         */
        Socket s;
        private int playerID;

        /**
         * Instantiates a new Receiver.
         *
         * @param ois      the ois
         * @param s        the s
         * @param playerID the player id
         */
        public Receiver(ObjectInputStream ois, Socket s, int playerID) {
            this.ois = ois;
            this.s = s;
            this.playerID = playerID;
        }

        /**
         * Gets player id.
         *
         * @return the player id
         */
        public int getPlayerID() {
            return playerID;
        }

        @Override
        public void run() {
            boolean connected = true;

            while (connected) {
                try {
                    Msg msg = (Msg) ois.readObject();

                    switch(msg.getType()) {
                        case PLAYER_MOVE:
                            PlayerMoveData moveData = (PlayerMoveData) msg.getData();

                            if(currentLevel.getPlayer(playerID).getHealth() == 0) break;

                            switch(moveData.dir) {
                                case STOP:
                                    currentLevel.getPlayer(playerID).setVelocity(new Vector2(0, 0));
                                    break;
                                case UP:
                                    currentLevel.getPlayer(playerID).setMovementDir(0, 1);
                                    break;
                                case DOWN:
                                    currentLevel.getPlayer(playerID).setMovementDir(0, -1);
                                    break;
                                case LEFT:
                                    currentLevel.getPlayer(playerID).setMovementDir(-1, 0);
                                    break;
                                case RIGHT:
                                    currentLevel.getPlayer(playerID).setMovementDir(1, 0);
                                    break;
                            }
                            break;
                        case PLAYER_SHOOT:
                            PlayerShootData shootData = (PlayerShootData) msg.getData();

                            if(currentLevel.getPlayer(playerID).getHealth() == 0) break;

                            if(currentLevel.getPlayer(playerID).canShoot()){
                                currentLevel.createBullet(playerID, shootData.angle);
                                currentLevel.getPlayer(playerID).shoot();
                            }
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("User Disconnected.");
                    connected = false;
                }
            }
        }
    }

    /**
     * Keep sending data to clients which connected to this server.
     */
    public class Sender implements Runnable {
        private ObjectOutputStream oos;
        private Socket s;
        private BlockingQueue<Msg> msgs;
        private int playerID;

        /**
         * Instantiates a new Sender.
         *
         * @param oos      the oos
         * @param s        the s
         * @param playerID the player id
         */
        public Sender(ObjectOutputStream oos, Socket s, int playerID) {
            this.oos = oos;
            this.s = s;
            this.playerID = playerID;

            msgs = new LinkedBlockingQueue<>();
        }

        /**
         * Gets player id.
         *
         * @return the player id
         */
        public int getPlayerID() {
            return playerID;
        }

        @Override
        public void run() {
            boolean connected = true;

            while (connected) {
                try {
                    ArrayList<Msg> msgsToSend = new ArrayList<>();

                    if(msgs.drainTo(msgsToSend) > 0) {
//                        System.out.println("Sent " + msgsToSend.size() + " message(s) to player " + playerID);
                        oos.writeObject(msgsToSend);
                        oos.reset();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    connected = false;
                }
            }

            System.out.println("User Disconnected.");
        }

        /**
         * Gets msg queue.
         *
         * @return the msg queue
         */
        public BlockingQueue<Msg> getMsgQueue() {
            return msgs;
        }
    }

    /**
     * The entry point of application.
     *
     * @param arg the input arguments
     */
    public static void main(String[] arg) {
        try {
            Server s = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}