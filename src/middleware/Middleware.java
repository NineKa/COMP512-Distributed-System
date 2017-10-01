package middleware;

import Interfaces.CommandType;
import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.ServerSocket;

/**
 * Created by emol on 9/30/17.
 */
public class Middleware {
    HashMap<ServerType, Set> serverMap;
    private final ExecutorService threadPool; // FIXME: do not use fixed thread pool

    // address
    String server = "localhost";
    int port = 1099;


    public Middleware(){
        serverMap = new HashMap<>();
        threadPool = Executors.newFixedThreadPool(5); // init thread pool // FIXME: do not use fixed thread pool
    }
    public static void main(String args[]) {
        Middleware m = new Middleware();
        m.init();
    }

    public void init(){

        // new msg -> dispatch a thread to execute it
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port: " + port);
            Thread serverThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        // listen for incoming msg
                        while (true) {
                            // new msg (new client) -> dispatch a thread to execute it
                            Socket s = serverSocket.accept();
                            threadPool.submit(new MiddlewareThread(s));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            serverThread.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}



class MiddlewareThread implements Runnable {
    Queue<Msg> msgs;    // msg history
    Msg currentMsg;
    Socket s;
    ObjectOutputStream out;
    ObjectInputStream in;

    public MiddlewareThread(Socket s) {
        this.s = s;
        this.msgs = new LinkedList<Msg>();
        try{
        this.out = new ObjectOutputStream(this.s.getOutputStream());
        this.in = new ObjectInputStream(this.s.getInputStream());}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Msg currentMsg = (Msg) in.readObject();
                System.out.println(currentMsg);
                if (currentMsg != null){
                    msgs.add(currentMsg);
                    System.out.print(currentMsg.cmd);
                    Reply r = processMsg(currentMsg);
                    this.out.writeObject(r);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private Reply processMsg(Msg m){

        // analyze client msg
        switch (m.cmd) {
            case addCars:
                //                    serverMap.get();
                // send to server
                // maybe send reserve cmd to server
                // forward result to client
                break;
            case addRooms:
                break;
            case addFlight:
                break;

        }
        Reply r = new Reply();
        r.isSuccess = true;
        return r;
    }


}


