package middleware;

import Common.ServerThread;
import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;
import resourceManager.RMHashtable;
import resourceManager.RMImpl;
import resourceManager.ResourceManagerInfo;
import client.ClientSocket;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by emol on 10/1/17.
 */
class MiddlewareThread extends ServerThread {
    HashMap<ServerType, Set<ResourceManagerInfo>> resourceManagerInfo;
    HashMap<ResourceManagerInfo, ClientSocket> RMConnections;
    RMImpl RM;  // RM for customer

    public MiddlewareThread(Socket s,
                            HashMap<ServerType, Set<ResourceManagerInfo>> resourceManagerInfo,
                            HashMap<ResourceManagerInfo, ClientSocket> RMConnections,
                            RMHashtable m_itemHT) {
        super(s);
        this.resourceManagerInfo = resourceManagerInfo;
        this.RMConnections = RMConnections;
        this.RM = new RMImpl(m_itemHT);
    }

    public Reply processMsg(Msg m){
        Reply r = new Reply(false, null);
        // analyze client msg
        switch (m.cmd) {
            case addCars:
                // send to server
                ResourceManagerInfo rm = selectRM(ServerType.Car);
                ClientSocket cs = RMConnections.get(rm);
                r = cs.execute(m);
//                System.out.print(r);

                // forward result to client
                try {
                    this.out.writeObject(r);
                }catch (Exception e){
                    e.printStackTrace();
                }
                // maybe send reserve cmd to server
                break;
            case addRooms:
                break;
            case addFlight:
                break;

        }
        return r;
    }


    // maybe select server based on server load in future
    public ResourceManagerInfo selectRM(ServerType type){
        Set<ResourceManagerInfo> set = resourceManagerInfo.get(type);
        return (ResourceManagerInfo) set.toArray()[0];
    }


}

