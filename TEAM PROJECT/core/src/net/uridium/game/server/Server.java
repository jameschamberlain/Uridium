package net.uridium.game.server;

import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.LevelFactory;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.screen.GameOver;
import net.uridium.game.screen.SettingsScreen;
import net.uridium.game.server.msg.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static net.uridium.game.screen.UridiumScreenManager.getUSMInstance;

public class Server{
    //Here is for network
    private ServerSocket ss;
    private Socket s;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    CopyOnWriteArrayList<Sender> users;

    ConcurrentHashMap<Integer, ServerLevel> levels;
    ServerLevel currentLevel;
    long lastUpdate;

    public Server(int port) throws IOException {
        System.out.println("Server Starting ...");
        ss = new ServerSocket(port);

        System.out.println("Creating Level ...");

        levels = new ConcurrentHashMap<>();

        File f = new File("level1.json");
        ServerLevel l = LevelFactory.buildLevelFromJSON(new Scanner(f).useDelimiter("\\A").next());
        currentLevel = l;
        levels.put(l.getID(), l);

        lastUpdate = System.currentTimeMillis();

        users = new CopyOnWriteArrayList<>();

        new Thread(new Acceptor(ss)).start();
        new Thread(this::levelUpdate).start();
    }

    public Server() throws IOException {
        this(6666);
    }

    private void levelUpdate() {
        while (true) {
            float delta = System.currentTimeMillis() - lastUpdate;
            delta /= 1000;
            currentLevel.update(delta);
            lastUpdate = System.currentTimeMillis();

            ArrayList<Msg> newMsgs = new ArrayList<>();
            currentLevel.getMsgs().drainTo(newMsgs);
//            if(newMsgs.size() > 0) {
//                System.out.println("Msgs: " + newMsgs.size());
//                for(Msg msg : newMsgs)
//                    System.out.println("\t" + msg.getType().name());
//            }

            for(Sender s : users)
                for (Msg msg : newMsgs)
                    s.getMsgQueue().add(msg);

            int id;
            if((id = currentLevel.shouldChangeLevel()) != -1) {
                currentLevel.changedLevel();

                if(levels.get(id) == null) {
                    File f = new File("level" + id + ".json");
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
                        p.setPosition(newLevel.getEntrance(currentLevel.getNextLevelEntrance()));
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
        }
    }

    public class Acceptor implements Runnable {
        private ServerSocket ss;

        public Acceptor(ServerSocket ss) {
            this.ss=ss;
        }

        @Override
        public void run() {
            System.out.println("Waiting for users ...");

            while (true) {
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
        }
    }

    public class Receiver implements Runnable {
        private ObjectInputStream ois;
        Socket s;
        private int playerID;

        public Receiver(ObjectInputStream ois, Socket s, int playerID) {
            this.ois = ois;
            this.s = s;
            this.playerID = playerID;
        }

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

    public class Sender implements Runnable {
        private ObjectOutputStream oos;
        private Socket s;
        private BlockingQueue<Msg> msgs;
        private int playerID;

        public Sender(ObjectOutputStream oos, Socket s, int playerID) {
            this.oos = oos;
            this.s = s;
            this.playerID = playerID;

            msgs = new LinkedBlockingQueue<>();
        }

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

        public BlockingQueue<Msg> getMsgQueue() {
            return msgs;
        }
    }

    public static void main(String[] arg) {
        try {
            Server s = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}