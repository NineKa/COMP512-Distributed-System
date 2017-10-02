package resourceManager;

import Common.ServerThread;
import Interfaces.Msg;
import Interfaces.Reply;
import Interfaces.ServerType;
import client.ClientSocket;

import java.net.Socket;
import java.util.*;

/**
 * Created by emol on 10/1/17.
 */
public class ResourceManagerThread extends ServerThread {
    //    protected RMHashtable m_itemHT;
    protected RMImpl RM;    // actual RM functions

    public ResourceManagerThread(Socket s, RMHashtable m_itemHT) {
        super(s);
//        this.m_itemHT = m_itemHT;
        this.RM = new RMImpl(m_itemHT);
    }

    @Override
    protected Reply processMsg(Msg m) {
        boolean isSuccess = false;
        Vector<Object> response = new Vector<>();
        switch (m.cmd) {
            case addCars:
                isSuccess = RM.addCars(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2),
                        Integer.parseInt((String) m.arg.elementAt(3)),
                        Integer.parseInt((String) m.arg.elementAt(4)));
                break;
            case addRooms:
                isSuccess = RM.addRooms(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2),
                        Integer.parseInt((String) m.arg.elementAt(3)),
                        Integer.parseInt((String) m.arg.elementAt(4)));
                break;
            case addFlight:
                isSuccess = RM.addFlight(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)),
                        Integer.parseInt((String) m.arg.elementAt(3)),
                        Integer.parseInt((String) m.arg.elementAt(4)));
                break;

            case deleteRooms:
                isSuccess = RM.deleteRooms(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2));
                break;

            case deleteFlight:
                isSuccess = RM.deleteFlight(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)));
                break;

            case deleteCars:
                isSuccess = RM.deleteCars(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2));
                break;

            case queryCars:
                response.add(RM.queryCars(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2)));
                isSuccess = true;
                break;

            case queryCarsPrice:
                response.add(RM.queryCarsPrice(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2)));
                isSuccess = true;
                break;

            case queryFlight:
                response.add(RM.queryFlight(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2))));
                isSuccess = true;
                break;

            case queryFlightPrice:
                response.add(RM.queryFlightPrice(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2))));
                isSuccess = true;
                break;

            case queryRooms:
                response.add(RM.queryRooms(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2)));
                isSuccess = true;
                break;

            case queryRoomsPrice:
                response.add(RM.queryRoomsPrice(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        (String) m.arg.elementAt(2)));
                isSuccess = true;
                break;

            case reserveCar:
                return RM.reserveCar(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)),
                        (String) m.arg.elementAt(3));

            case reserveFlight:
                return RM.reserveFlight(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)),
                        Integer.parseInt((String) m.arg.elementAt(3)));

            case reserveRoom:
                return RM.reserveRoom(
                        Integer.parseInt((String) m.arg.elementAt(1)),
                        Integer.parseInt((String) m.arg.elementAt(2)),
                        (String) m.arg.elementAt(3));


        }



        Reply r = new Reply(isSuccess, response);
        // TODO: maybe send reserve cmd to server
        return r;
    }


}
