package net.uridium.game.server;

import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.LevelFactory;
import net.uridium.game.gameplay.entity.damageable.Player;
import net.uridium.game.server.msg.LevelData;
import net.uridium.game.server.msg.Msg;
import net.uridium.game.server.msg.PlayerMoveData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server{
    //Here is for network
    private ServerSocket ss;
    private Socket s;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    ArrayList<Sender> users;

    ServerLevel level;
    long lastUpdate;

    public Server() throws IOException {
        System.out.println("Server Starting ...");
        ss = new ServerSocket(9988);

        System.out.println("Creating Level ...");

        File f = new File("level1.json");
        level = LevelFactory.buildLevelFromJSON(new Scanner(f).useDelimiter("\\A").next());
        lastUpdate = System.currentTimeMillis();

        users = new ArrayList<>();

        new Thread(new Acceptor(ss)).start();
        new Thread(this::levelUpdate).start();
    }

    private void levelUpdate() {
        while (true) {
            float delta = System.currentTimeMillis() - lastUpdate;
            delta /= 1000;
            level.update(delta);
            lastUpdate = System.currentTimeMillis();

            ArrayList<Msg> newMsgs = new ArrayList<>();
            level.getMsgs().drainTo(newMsgs);
            if(newMsgs.size() > 0) {
                System.out.println("Msgs: " + newMsgs.size());
                for(Msg msg : newMsgs)
                    System.out.println("\t" + msg.getType().name());
            }

            for(Sender s : users)
                for (Msg msg : newMsgs)
                    s.getMsgQueue().add(msg);

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

                    playerID = level.getNextEntityID();
                    Vector2 playerSpawn = level.getNewPlayerSpawn();

                    Player player = new Player(playerID, playerSpawn);
                    level.addEntity(player);

                    LevelData levelData = level.getLevelData();
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
                                    level.getPlayer(playerID).setVelocity(new Vector2(0, 0));
                                    break;
                                case UP:
                                    level.getPlayer(playerID).setVelocity(0, 200);
                                    break;
                                case DOWN:
                                    level.getPlayer(playerID).setVelocity(0, -200);
                                    break;
                                case LEFT:
                                    level.getPlayer(playerID).setVelocity(-200, 0);
                                    break;
                                case RIGHT:
                                    level.getPlayer(playerID).setVelocity(200, 0);
                                    break;
                            }
                            break;
                        case PLAYER_SHOOT:
                            PlayerMoveData shootData = (PlayerMoveData) msg.getData();
                            level.createBullet(playerID, shootData.dir);
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

        @Override
        public void run() {
            boolean connected = true;

            while (connected) {
                try {
                    ArrayList<Msg> msgsToSend = new ArrayList<>();

                    if(msgs.drainTo(msgsToSend) > 0) {
                        System.out.println("Sent " + msgsToSend.size() + " message(s) to player " + playerID);
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