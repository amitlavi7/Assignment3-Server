package bgu.spl.net.impl.stomp.framesReceived;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class SubscribeCommand implements Frame<String> {

    private String destination;
    private String id;
    private String receipt;
    private int opCode = 4;

    public SubscribeCommand(String destination, String id, String receipt) {
        this.destination = destination;
        this.id = id;
        this.receipt = receipt;
    }

    public String getDestination() {
        return destination;
    }

    public String getId() {
        return id;
    }

    public String getReceipt() {
        return receipt;
    }

    public int getOpCode() {
        return opCode;
    }

}
