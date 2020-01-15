package bgu.spl.net.impl.stomp.framesReceived;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class Unsubscribe implements Frame<String> {

    //TODO: add topic that you need to erase from

    private String id;
    private String receipt;
    private int opCode = 5;

    public Unsubscribe(String id, String receipt) {
        this.id = id;
        this.receipt = receipt;
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
