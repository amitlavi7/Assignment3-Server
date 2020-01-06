package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class DisconnectCommand implements Command <String> {

    private String receipt;

    public DisconnectCommand(String receipt) {
        this.receipt = receipt;
    }

    public String getReceipt() {
        return receipt;
    }

    @Override
    public Serializable execute(String arg) {
        return null;
    }
}
