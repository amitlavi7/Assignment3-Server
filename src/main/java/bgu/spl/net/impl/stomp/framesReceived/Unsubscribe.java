package bgu.spl.net.impl.stomp.framesReceived;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class Unsubscribe implements Frame<String> {

    private String id;
    private String destination;
    private int opCode = 5;

    public Unsubscribe(String id, String destination) {
        this.id = id;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public int getOpCode() {
        return opCode;
    }
}
