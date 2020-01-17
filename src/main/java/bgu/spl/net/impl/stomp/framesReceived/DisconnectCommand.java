package bgu.spl.net.impl.stomp.framesReceived;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class DisconnectCommand implements Frame<String> {

    private String receipt;
    private int opCode = 2;

    public DisconnectCommand(String receipt) {
        this.receipt = receipt;
    }

    public String getReceipt() {
        return receipt;
    }

    public int getOpCode() {
        return opCode;
    }

}
