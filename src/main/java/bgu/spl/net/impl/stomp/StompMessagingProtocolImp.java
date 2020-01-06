package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.srv.Connections;

import java.io.Serializable;
import java.util.LinkedList;

public class StompMessagingProtocolImp implements StompMessagingProtocol {

    private boolean shouldTerminate = false;

    @Override
    public void start(int connectionId, Connections<String> connections) {

    }

    @Override
    public void process(LinkedList<String> message) {

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
