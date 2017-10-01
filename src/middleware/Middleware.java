package middleware;

import Common.Server;
import Common.ServerThread;
import Interfaces.ServerType;
import resourceManager.RMHashtable;
import resourceManager.ResourceManagerInfo;
import client.ClientSocket;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by emol on 9/30/17.
 */
//String server = "localhost";
//int port = 1099;

public class Middleware extends Server{
    HashMap<ServerType, Set<ResourceManagerInfo>> resourceManagerInfo;
    HashMap<ResourceManagerInfo, ClientSocket> RMConnections;

    // middleware also stores customer info (so middleware is also a RM for customer)
    protected RMHashtable m_itemHT; // all data relevant to it

    public Middleware(String host, int port){
        super(host, port);
        this.m_itemHT = new RMHashtable();

        resourceManagerInfo = new HashMap<>();
        // TODO: get resourceManager data from user input during startup
        ResourceManagerInfo r1 = new ResourceManagerInfo(ServerType.Car, "localhost", 1100);
        Set<ResourceManagerInfo> r1set = new HashSet<>();
        r1set.add(r1);
        resourceManagerInfo.put(ServerType.Car, r1set);
        ResourceManagerInfo r2 = new ResourceManagerInfo(ServerType.Room, "localhost", 1101);
        Set<ResourceManagerInfo> r2set = new HashSet<>();
        r2set.add(r2);
        resourceManagerInfo.put(ServerType.Room, r2set);
        ResourceManagerInfo r3 = new ResourceManagerInfo(ServerType.Flight, "localhost", 1102);
        Set<ResourceManagerInfo> r3set = new HashSet<>();
        r3set.add(r3);
        resourceManagerInfo.put(ServerType.Flight, r3set);
        //--------------

        // initialize connection with servers
        // FIXME: should each serverthread has its own socket with rm, or share this socket?
        RMConnections = new HashMap<>();
        for (Set<ResourceManagerInfo> set : resourceManagerInfo.values()){
            for (ResourceManagerInfo r : set){
                ClientSocket cs = new ClientSocket(r.host, r.port);
                RMConnections.put(r, cs);
            }
        }
    }

    public static void main(String args[]) {
        Middleware m = new Middleware("localhost", 1099);
        m.init();
    }
    @Override
    public ServerThread createServerThread(Socket s) {
        return new MiddlewareThread(s, resourceManagerInfo, RMConnections, m_itemHT);
    }
}



