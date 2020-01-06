package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class SendCommand implements Command<String> {

    private String destination;
    private String body;

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

    @Override
    public Serializable execute(String arg) {
        return null;
    }
}
