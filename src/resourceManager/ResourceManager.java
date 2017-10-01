package resourceManager;

import Common.Server;
import Common.ServerThread;
import Interfaces.ServerType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by emol on 9/30/17.
 */
public class ResourceManager extends Server {
    ResourceManagerInfo info;

    public ResourceManager(ResourceManagerInfo info){
        super(info.host, info.port);
        this.info = info;
    }


    public static void main(String args[]) {
        // user is responsible for specify host, port, type of the server
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String command = "";
        Vector arguments = new Vector();

        String host;
        int port;
        String serverType;

        if (args.length != 3) {
            System.out.println("Usage: java ResourceManager [host] [port] [serverType]");
            System.exit(1);
        }
        else {
            host = args[0];
            port = Integer.parseInt(args[1]);
            serverType = args[2];
            ServerType t = Enum.valueOf(ServerType.class, serverType);
            ResourceManagerInfo info = new ResourceManagerInfo( t, host, port);
            ResourceManager m = new ResourceManager(info);
            m.init();
        }
    }

    @Override
    public ServerThread createServerThread(Socket s) {
        return new ResourceManagerThread(s);
    }
}
