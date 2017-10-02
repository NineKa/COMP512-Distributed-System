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
        Reply r = new Reply(false, null);
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
        }



        r = new Reply(isSuccess, null);
        // TODO: maybe send reserve cmd to server
        return r;
    }


}
