package middleware;

import Interfaces.CommandType;
import Interfaces.Msg;
import Interfaces.ServerType;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by emol on 9/30/17.
 */
public class Middleware{
    HashMap<ServerType, Set> serverMap = new HashMap<>();

    // thread pool to listen incoming msg

    public void init(){
        // init thread pool

        // listen for incoming msg

            // new msg -> dispatch a thread to execute it
    }

    public class MiddlewareThread implements Runnable{
        Msg msg;
        Socket s;
        @Override
        public void run(){
            // analyze client msg
            switch (msg.cmd){
                case AddCar:
//                    serverMap.get();
                    // send to server
                    // maybe send reserve cmd to server
                    // forward result to client
            }

        }
    }

}
