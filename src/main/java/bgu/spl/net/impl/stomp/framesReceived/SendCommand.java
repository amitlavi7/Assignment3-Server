package bgu.spl.net.impl.stomp.framesReceived;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class SendCommand implements Frame<String> {

    private String destination;
    private String body;
    private int opCode = 3;

    public SendCommand(String destination, String body) {
        this.destination = destination;
        this.body = body;
    }

    public String getDestination() {
        return destination;
    }

    public String getBody() {
        return body;
    }

    public int getOpCode() {
        return opCode;
    }
}
