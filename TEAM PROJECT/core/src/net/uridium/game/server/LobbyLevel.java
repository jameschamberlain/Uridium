package net.uridium.game.server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class LobbyLevel {
    private ServerSocket ss;
    private Socket s;
    private BufferedReader bfr;
    private String roomCode;
    private int portNum;
    private PrintStream ps;
    private HashMap<String,int[]> rooms;

    public LobbyLevel() throws IOException {
        ss = new ServerSocket(9966);
        rooms = new HashMap<String, int[]>();
        rooms.put("emp1",new int[]{9977, 0});
        rooms.put("emp2",new int[]{9933, 0});


        new Thread(() -> {

            while (true) {
                try {
                    portNum = 0;
                    System.out.println("Lobby Starts");
                    s = ss.accept();
                    ps = new PrintStream(s.getOutputStream());
                    bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    System.out.println("Connected");
                    roomCode = bfr.readLine();
                    System.out.println("Read");
                    boolean exist = false;

                    for (String i : rooms.keySet()) {
                        if (roomCode.equals(i) ) {
                            exist=true;
                            System.out.println("This room already exist!!!!!!");
                        }
                    }

                    if(exist){
                        int[] r = rooms.get(roomCode);
                        if(r[1]!=2){
                            portNum = r[0];
                            r[1]++;
                            rooms.put(roomCode,r);
                            System.out.println("Find the room");
                        }
                        else{
                            System.out.println("The room is full");
                        }
                    }
                    else{
                        int myRoom = (int)(Math.random() * 1000) + 3000;
                        while(rooms.values().contains(new int[] {myRoom,0})){
                            myRoom = (int)(Math.random() * 1000) + 3000;
                        }
                        while(rooms.values().contains(new int[] {myRoom,1})){
                            myRoom = (int)(Math.random() * 1000) + 3000;
                        }
                        while(rooms.values().contains(new int[] {myRoom,2})){
                            myRoom = (int)(Math.random() * 1000) + 3000;
                        }
                        rooms.put(roomCode,new int[]{myRoom,1});
                        portNum = myRoom;
                        System.out.println("Not exist,So,add a Room "+portNum);
                    }

                    System.out.println("Sent" + portNum);
                    ps.println(portNum);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();


    }

    public static void main(String[] arg) {
        try {
            LobbyLevel l = new LobbyLevel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
