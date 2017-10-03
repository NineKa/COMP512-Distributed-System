package middleware;

import Common.ServerThread;
import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;
import resourceManager.*;
import client.ClientSocket;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.*;

/**
 * Created by emol on 10/1/17.
 */
class MiddlewareThread extends ServerThread {
    HashMap<ServerType, Set<ResourceManagerInfo>> resourceManagerInfo;
    HashMap<ResourceManagerInfo, ClientSocket> RMConnections;
    RMImpl localRM;  // RM for customer

    public MiddlewareThread(Socket s,
                            HashMap<ServerType, Set<ResourceManagerInfo>> resourceManagerInfo,
                            HashMap<ResourceManagerInfo, ClientSocket> RMConnections,
                            RMHashtable m_itemHT) {
        super(s);
        this.resourceManagerInfo = resourceManagerInfo;
        this.RMConnections = RMConnections;
        this.localRM = new RMImpl(m_itemHT);
    }

    public Reply processMsg(Msg m){
        ResourceManagerInfo rm = null;
        ClientSocket cs = null;
        boolean isSuccess = false;
        Reply r;

        // analyze client msg
        // select RM
        switch (m.cmd) {
            case addCars:
                rm = selectRM(ServerType.Car);
                // TODO: maybe send reserve cmd to server
                break;

            case addRooms:
                rm = selectRM(ServerType.Room);
                break;

            case addFlight:
                rm = selectRM(ServerType.Flight);
                break;

            case newCustomer:
                int id = localRM.newCustomer(Integer.parseInt((String) m.arg.elementAt(1)));
                r = new Reply(true, new Vector<>());
                r.response.add(id);
                return r;

            case newcustomerid:
                isSuccess = localRM.newCustomer(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)));
                return new Reply(isSuccess, null);

            case deleteRooms:
                rm = selectRM(ServerType.Room);
                break;

            case deleteFlight:
                rm = selectRM(ServerType.Flight);
                break;

            case deleteCars:
                rm = selectRM(ServerType.Car);
                break;

            case deleteCustomer:
                isSuccess = localRM.deleteCustomer(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)));
                return new Reply(isSuccess, null);

            case queryCars:
                rm = selectRM(ServerType.Car);
                break;

            case queryCarsPrice:
                rm = selectRM(ServerType.Car);
                break;

            case queryFlight:
                rm = selectRM(ServerType.Flight);
                break;

            case queryFlightPrice:
                rm = selectRM(ServerType.Flight);
                break;

            case queryRooms:
                rm = selectRM(ServerType.Room);
                break;

            case queryRoomsPrice:
                rm = selectRM(ServerType.Room);
                break;

            case queryCustomerInfo:
                String bill = localRM.queryCustomerInfo(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)));
                r = new Reply(true, new Vector<>());
                r.response.add(bill);
                return r;

            case reserveCar:
                rm = selectRM(ServerType.Car);
                cs = RMConnections.get(rm);
                return localRM.customerReserve(
                        cs,
                        m
                );

            case reserveFlight:
                rm = selectRM(ServerType.Flight);
                cs = RMConnections.get(rm);
                return localRM.customerReserve(
                        cs,
                        m
                );

            case reserveRoom:
                rm = selectRM(ServerType.Room);
                cs = RMConnections.get(rm);
                return localRM.customerReserve(
                        cs,
                        m);

            case itinerary:
                HashMap<ServerType, ClientSocket> css = new HashMap<>();
                css.put(ServerType.Room, RMConnections.get(selectRM(ServerType.Room)));
                css.put(ServerType.Car, RMConnections.get(selectRM(ServerType.Car)));
                css.put(ServerType.Flight, RMConnections.get(selectRM(ServerType.Flight)));
                return localRM.customerReserveItinerary(css, m);
        }

        // if rm is not null (rm exists && not local rm)
            cs = RMConnections.get(rm);
            return cs.execute(m);

    }


    // maybe select server based on server load in future
    public ResourceManagerInfo selectRM(ServerType type){
        Set<ResourceManagerInfo> set = resourceManagerInfo.get(type);
        return (ResourceManagerInfo) set.toArray()[0];
    }


}

