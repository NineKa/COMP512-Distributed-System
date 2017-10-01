package middleware;

import Common.ServerThread;
import Interfaces.Msg;
import Interfaces.Reply;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by emol on 10/1/17.
 */
class MiddlewareThread extends ServerThread {

    public MiddlewareThread(Socket s) {
        super(s);
    }

    public Reply processMsg(Msg m){

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

