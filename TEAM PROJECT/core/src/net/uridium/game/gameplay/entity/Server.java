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
    private HashMap<Integer, Vector2> players;


    public Server() throws IOException {

        try {
            ss = new ServerSocket(9988);
            System.out.println("Server Starts");
            s=ss.accept();
            System.out.println("Connecnted");
            oos = new ObjectOutputStream(s.getOutputStream());
            bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String str = bfr.readLine();
            String xy[] = str.split(" ");

            Float x = Float.parseFloat(xy[0]);
            Float y = Float.parseFloat(xy[1]);
            Vector2 player = new Vector2(x,y);
            players = new HashMap<Integer, Vector2>();
            players.put(s.getPort(),player);
            System.out.println(players.get(s.getPort()).x+"......."+players.get(s.getPort()).y);

        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Sender(oos)).start();
        new Thread(new Receiver(bfr)).start();



    }

    public class Receiver implements Runnable{
        private BufferedReader bfr;

        public Receiver(BufferedReader bfr){
            this.bfr = bfr;
        }
        @Override
        public void run() {
            while (true){
                try {
                    String str =  bfr.readLine();
                    if(str.equals("up")){
                        players.get(0).x+=10;
                    }
                    else if(str.equals("down")){
                        players.get(0).x-=10;
                    }
                    Thread.sleep(10);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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
                    oos.writeObject(players);
                    oos.reset();
                    Thread.sleep(10);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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