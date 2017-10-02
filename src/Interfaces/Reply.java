package Interfaces;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by emol on 9/30/17.
 */
public class Reply implements Serializable{
    public boolean isSuccess;
    public  Vector response;

    public Reply(boolean isSuccess, Vector response){
        this.isSuccess = isSuccess;
        this.response = response;
    }


    //---------- reply response format ----------------
    public static final HashMap<CommandType, ArgType[]> replyFormat;
    static
    {
        replyFormat = new HashMap<>();
        replyFormat.put(CommandType.addCars, null);
        replyFormat.put(CommandType.addFlight, null);
        replyFormat.put(CommandType.addRooms, null);
        replyFormat.put(CommandType.newCustomer, new ArgType[] { ArgType.INT });    // cid
        replyFormat.put(CommandType.newcustomerid, null);
        replyFormat.put(CommandType.deleteCars, null);
        replyFormat.put(CommandType.deleteFlight, null);
        replyFormat.put(CommandType.deleteRooms, null);
        replyFormat.put(CommandType.deleteCustomer, null);
    }
}



