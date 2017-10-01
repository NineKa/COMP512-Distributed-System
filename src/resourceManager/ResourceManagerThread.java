package resourceManager;

import Common.ServerThread;
import Interfaces.Msg;
import Interfaces.Reply;

import java.net.Socket;

/**
 * Created by emol on 10/1/17.
 */
public class ResourceManagerThread extends ServerThread{
    public ResourceManagerThread(Socket s){
        super(s);
    }
    @Override
    protected Reply processMsg(Msg m) {
        Reply r = new Reply();
        r.isSuccess = true;
        return r;
    }
}
