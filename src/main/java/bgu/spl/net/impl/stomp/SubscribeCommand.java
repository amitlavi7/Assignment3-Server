package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class SubscribeCommand implements Command<String> {

    private String destination;
    private String id;
    private String receipt;

    public String getDestination() {
        return destination;
    }

    public String getId() {
        return id;
    }

    public String getReceipt() {
        return receipt;
    }

    @Override
    public Serializable execute(String arg) {
        return null;
    }
}
