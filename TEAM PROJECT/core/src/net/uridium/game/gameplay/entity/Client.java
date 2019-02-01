package net.uridium.game.gameplay.entity;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client {

    Socket s;
    // 这两个是用来读Object的，Reading Object From here
    ObjectInputStream oi;
    PrintStream ps;
    // 存储信息的HashMap Save All players information here
    HashMap<Integer, Player> players;

    public Client() {
        try {
            s = new Socket("127.0.0.1", 9999);
            OutputStream os = s.getOutputStream();
            InputStream is = s.getInputStream();
            ps = new PrintStream(os);
            oi = new ObjectInputStream(is);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.lauchFrame();
        // 从服务器端获得初始飞机的地址 Get the initilisation information from server
        this.reciveMap();
        //这里本来加了Key监听
    }

    public void lauchFrame() {

    }

    //这里要加入一个可以画固定东西的的

    public HashMap<Integer, Player> getMap() {
        return players;
    }

    public void send(String s) {
        // 为了传输Object不能把流把给切了,一定要多等一会儿把东西传完
        ps.println(s);
    }

    @SuppressWarnings("unchecked")
    public void reciveMap() {
        try {
            players = (HashMap<Integer, Player>) oi.readObject();
            //这里是要马上利用新有的消息重新画企鹅
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        //此处有一个线程可以持续的获得来自Server的信息更新

    }

}
