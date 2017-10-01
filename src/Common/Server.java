package Common;

import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;
import ResourceManager.ResourceManagerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by emol on 10/1/17.
 */
public abstract class Server {
    private final ExecutorService threadPool; // FIXME: do not use fixed thread pool
    String host;
    int port;

    public Server(String host, int port){
        this.host = host;
        this.port = port;
        threadPool = Executors.newFixedThreadPool(5); // init thread pool // FIXME: do not use fixed thread pool
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
                            threadPool.submit(createServerThread(s));
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

    public abstract ServerThread createServerThread(Socket s);

}

