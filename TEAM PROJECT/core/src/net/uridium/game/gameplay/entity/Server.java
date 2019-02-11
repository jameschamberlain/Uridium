package net.uridium.game.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import net.uridium.game.gameplay.Tile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static net.uridium.game.Uridium.GAME_HEIGHT;
import static net.uridium.game.Uridium.GAME_WIDTH;

public class Server {

    private ServerSocket ss;
    private Socket s;
    private BufferedReader bfr;
    private ObjectOutputStream oos;
    private myPackage aPackage;
    private HashMap<Integer, Vector2> players;


    public Server() throws IOException {
        ss = new ServerSocket(9988);
        System.out.println("Server Starts");
        while(true){
            try {
                s=ss.accept();
                aPackage = new myPackage();
                System.out.println("Connecnted");
                oos = new ObjectOutputStream(s.getOutputStream());
                bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String str = bfr.readLine();
                String xy[] = str.split(" ");

                Float x = Float.parseFloat(xy[0]);
                Float y = Float.parseFloat(xy[1]);
                Vector2 player = new Vector2(x,y);

                aPackage.setPlayers(s.getPort(),player);

            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(new Sender(oos)).start();
            new Thread(new Receiver(bfr,s)).start();
        }

    }

    public class Receiver implements Runnable{
        private BufferedReader bfr;
        Socket s;

        public Receiver(BufferedReader bfr,Socket s){
            this.bfr = bfr;
            this.s = s;
        }
        @Override
        public void run() {
            while (true){
                try {
                    String str =  bfr.readLine();
                    String xy[] = str.split(" ");

                    Float x = Float.parseFloat(xy[0]);
                    Float y = Float.parseFloat(xy[1]);
                    Vector2 player = new Vector2(x,y);
                    System.out.println("Server Receive:"+xy[0]+"------"+xy[1]);
                    aPackage.setPlayers(s.getPort(),player);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Sender implements Runnable{
        private ObjectOutputStream oos;

        public Sender(ObjectOutputStream oos){
            this.oos = oos;
        }
        @Override
        public void run() {
            while(true){
                try {
                    oos.writeObject(aPackage);
                    System.out.println("A package sent");
                    oos.reset();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main (String[] arg) {
        try {
            Server s = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}