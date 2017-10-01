package resourceManager;

import Interfaces.ServerType;

/**
 * Created by emol on 9/30/17.
 * Server infomation
 */
public class ResourceManagerInfo {
    public ServerType type;
    public String host;
    public int port = 1100;

    public ResourceManagerInfo(ServerType type, String host, int port){
        this.type = type;
        this.host = host;
        this.port = port;
    }
}
