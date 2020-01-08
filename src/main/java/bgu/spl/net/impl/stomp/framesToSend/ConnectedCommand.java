package bgu.spl.net.impl.stomp.framesToSend;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class ConnectedCommand implements Frame<String> {

    private String connected;
    private String version;

    public ConnectedCommand(String version) {
        connected = "CONNECTED";
        this.version = "version:"+ version;
    }

    @Override
    public int getOpCode() {
        return 0;
    }
}
