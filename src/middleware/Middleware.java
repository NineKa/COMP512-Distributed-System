package middleware;

import Common.Server;
import Common.ServerThread;
import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;
import ResourceManager.ResourceManagerInfo;
import ResourceManager.ResourceManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
//String server = "localhost";
//int port = 1099;

public class Middleware extends Server{
    HashMap<ServerType, Set<ResourceManagerInfo>> serverMap;

    public Middleware(String host, int port){
        super(host, port);
        serverMap = new HashMap<>();
    }

    public static void main(String args[]) {
        Middleware m = new Middleware("localhost", 1099);
        m.init();
    }
    @Override
    public ServerThread createServerThread(Socket s) {
        return new MiddlewareThread(s);
    }
}




