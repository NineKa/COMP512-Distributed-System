package Interfaces;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by emol on 9/30/17.
 */
public class Msg implements Serializable {
    public CommandType cmd;
    public Vector arg;
}
