package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    //final Rectangle body;
    Color c = new Color(10673872);
    Color c2 = new Color();
    //移动速度
    float moveSpeed = 300;
    //当前关卡
    Level level;
    Texture texture;

    public Vector2 lastPos;
    public long lastShot = 0;
    private long reloadTime = 350;

    HashMap<Integer, Player> planes = new HashMap<>();
    ServerSocket ss;
    // 读取并且发送Object

    public Server() {
        //Initilise Player Here
//        lastPos = new Vector2(x, y);
//        body = new Rectangle(x, y, width, height);
        this.level = level;
        texture = new Texture(Gdx.files.internal("penguin_square.png"));
        //Start Server
        try {
            ss = new ServerSocket(9999);
            // 堵塞，直到连接接入
            System.out.println("Server Starts");
            while (true) {
                Socket s = ss.accept();
                // 一旦成功接入后创建飞机，并且将飞机放入Map中 Once Connected, put player into map
                System.out.println(s.getInetAddress().getHostAddress() + "已经接入");
                //Player p = new Player();
                //planes.put(s.getPort(), p);
                // 建立Object的交流通道

                ObjectOutputStream oo = new ObjectOutputStream(s.getOutputStream());

                //new Thread(new RecThread(s)).start();
                new Thread(new SendThread(oo)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendThread implements Runnable {
        ObjectOutputStream oos;

        public SendThread(ObjectOutputStream oos) {
            this.oos = oos;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    oos.writeObject(planes);
                    oos.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

//    private class RecThread implements Runnable {
//        BufferedReader bfr;
//        Socket s;
//
//        public RecThread(Socket s) {
//            try {
//                this.s = s;
//                this.bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            } catch (Exception e) {
//            }
//
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                try {
//                    int k;
//                    k = Integer.parseInt(bfr.readLine());
//                    int port = s.getPort();
//                    Plane p = planes.get(port);
//                    switch (k) {
//                        case KeyEvent.VK_UP:
//                            p.setY(p.getY() - speed);
//                            break;
//                        case KeyEvent.VK_DOWN:
//                            p.setY(p.getY() + speed);
//                            break;
//                        case KeyEvent.VK_LEFT:
//                            p.setX(p.getX() - speed);
//                            break;
//                        case KeyEvent.VK_RIGHT:
//                            p.setX(p.getX() + speed);
//                            break;
//                    }
//                    planes.put(port, p);
//                } catch (NumberFormatException | IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    public static void main(String[] args) {
        Server s = new Server();

    }

}