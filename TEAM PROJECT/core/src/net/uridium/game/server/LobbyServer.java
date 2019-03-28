package net.uridium.game.server;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class LobbyServer {
    private ServerSocket ss;
    private Socket s;
    private BufferedReader bfr;
    private ObjectOutputStream oo;
    private PrintStream ps;
    private HashMap<String,int[]> rooms;
    private String instruction;
    private ArrayList<ObjectOutputStream> sendPaths;

    /**
     * Constructor of LobbyServer
     * @throws IOException
     */
    public LobbyServer() throws IOException {
        ss = new ServerSocket(ServerConstants.LOBBY_SERVER_PORT);
        rooms = new HashMap<String, int[]>();
        sendPaths = new ArrayList<>();
        new Thread(new Acceptor(ss)).start();

    }

    /**
     * Store a room into Hashmap and assign it an unique number
     * @param roomName
     * @return portNumber (To return a unique number for creating a server)
     */

    public int changeRoom(String roomName){

        boolean exist = false;
        int portNum = 0;

        //Check if the room exists
        for (String i : rooms.keySet()) {
            if (roomName.equals(i) ) {
                exist=true;
                System.out.println("This room already exist!!!!!!");
            }
        }
        //If the room exists already, just add 1 to the number of player
        if(exist){
            int[] r = rooms.get(roomName);
            if(r[1]!=2){
                portNum = r[0];
                r[1]++;
                rooms.put(roomName,r);
                System.out.println("Find the room");
            }
            else{
                System.out.println("The room is full");
            }
        }
        else{
            //Ensure the port number is unique
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
            rooms.put(roomName,new int[]{myRoom,1});
            portNum = myRoom;
            System.out.println("Not exist, "+roomName+" So,add a Room "+portNum);
            try {
                new Server(portNum);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return portNum;
    }

    /**
     * This inner class will keep waiting a new connections
     * Once a connection built up, a new Thread will starts
     * to keep listening request from clients, update roomdata
     * and broadcast to all clients.
     */
    public class Acceptor implements Runnable {
        private ServerSocket ss;

        public Acceptor(ServerSocket ss) {
            this.ss = ss;
        }

        /**
         * Listen to requests from clients
         * Update roomdata
         * Send to all clients
         */

        @Override
        public void run() {

            while (true) {
                try {
                    s = ss.accept();
                    System.out.println("Yeah! I find you! Connected");

                    bfr = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    oo = new ObjectOutputStream(s.getOutputStream());

                    //Once a client connected, send current room data immediately
                    oo.writeObject(rooms);
                    oo.flush();
                    oo.reset();

                    new Thread(()->{
                        //For keeping output and input channel open all the time.
                        BufferedReader myBfr = bfr;
                        ObjectOutputStream myOo = oo;
                        sendPaths.add(myOo);
                        while(true){
                            try {
                                String myString = myBfr.readLine();
                                dealWithInstruction(myString);
                                sendMessageToAll();
                                System.out.println(myString);
                                Thread.sleep(3);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Send data to all clients which connected to this lobby
     */
    public void sendMessageToAll(){
        //Send messages to all clients
        for(ObjectOutputStream p:sendPaths){
            try {
                p.writeObject(rooms);
                p.flush();
                p.reset();
                System.out.println("I am the lobby I have sent the thing");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deal with differnent instructions.
     * @param instruction
     */
    public void dealWithInstruction(String instruction){
        String[] instruction_tuple = instruction.split(" ");

        switch (instruction_tuple[0]){
            case "create":
                changeRoom(instruction_tuple[1]);
                break;
            case "join":
                changeRoom(instruction_tuple[1]);
                break;
            default:
        }
    }

    public static void main(String[] arg) {
        try {
            LobbyServer l = new LobbyServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
