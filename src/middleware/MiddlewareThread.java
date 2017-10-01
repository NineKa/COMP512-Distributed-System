package middleware;

import Common.ServerThread;
import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;
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

    public MiddlewareThread(Socket s,
                            HashMap<ServerType, Set<ResourceManagerInfo>> resourceManagerInfo,
                            HashMap<ResourceManagerInfo, ClientSocket> RMConnections) {
        super(s);
        this.resourceManagerInfo = resourceManagerInfo;
        this.RMConnections = RMConnections;
    }

    public Reply processMsg(Msg m){

        // analyze client msg
        switch (m.cmd) {
            case addCars:
                // send to server
                ResourceManagerInfo rm = selectRM(ServerType.Car);
                ClientSocket cs = RMConnections.get(rm);
                Reply r = cs.execute(m);
                System.out.print(r);

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
        Reply r = new Reply();
        r.isSuccess = true;
        return r;
    }


    // maybe select server based on server load in future
    public ResourceManagerInfo selectRM(ServerType type){
        Set<ResourceManagerInfo> set = resourceManagerInfo.get(type);
        return (ResourceManagerInfo) set.toArray()[0];
    }


}

