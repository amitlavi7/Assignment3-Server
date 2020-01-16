package bgu.spl.net.impl.stomp.framesToSend;

import bgu.spl.net.srv.Frame;

public class Receipt implements Frame<String> {

    private String receipt = "RECEIPT";
    private String id = "receipt-id:";
    private int opCode;
    private boolean disconnect;

    public Receipt(String id,boolean disconnect) {
        this.id = this.id + id;
        this.disconnect = disconnect;
    }

    public String getReceipt() {
        return receipt;
    }

    public String getId() {
        return id;
    }

    public boolean isDisconnect() {
        return disconnect;
    }

    @Override
    public int getOpCode() {
        return 4;
    }
}
