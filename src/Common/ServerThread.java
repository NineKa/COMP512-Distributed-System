package Common;

import Interfaces.Msg;
import Interfaces.Reply;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by emol on 10/1/17.
 */

public abstract class ServerThread implements Runnable {
    Queue<Msg> msgs;    // msg history
    Msg currentMsg;
    Socket s;
    ObjectOutputStream out;
    ObjectInputStream in;

    public ServerThread(Socket s) {
        this.s = s;
        this.msgs = new LinkedList<Msg>();
        try{
            this.out = new ObjectOutputStream(this.s.getOutputStream());
            this.in = new ObjectInputStream(this.s.getInputStream());}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Msg currentMsg = (Msg) in.readObject();
                System.out.println(currentMsg);
                if (currentMsg != null){
                    msgs.add(currentMsg);
                    System.out.print(currentMsg.cmd);
                    Reply r = processMsg(currentMsg);
                    this.out.writeObject(r);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected abstract Reply processMsg(Msg m);
}
