package client;

import Interfaces.CommandType;
import Interfaces.Msg;
import Interfaces.Reply;

import java.io.*;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by emol on 9/30/17.
 */


public class ClientSocket {
    protected Socket s;
    ObjectOutputStream out;
    ObjectInputStream in;

    // server address
    String host;
    int port;

    public ClientSocket(String host, int port){
        this.host = host;
        this.port = port;
        try {
            s = new Socket(host, port);
            this.out = new ObjectOutputStream(s.getOutputStream());
            this.in = new ObjectInputStream(s.getInputStream());

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Reply execute(Msg m) {
        Reply r = null;
        try {
            out.writeObject(m);
            out.flush();
            r = (Reply) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }




    /**
     * try close this socket
     */
    public boolean close() {
        try {
            in.close();
            out.close();
            s.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
