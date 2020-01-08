package bgu.spl.net.impl.stomp.framesToSend;

import bgu.spl.net.srv.Frame;

public class Receipt implements Frame<String> {

    private String receipt = "RECEIPT";
    private String id = "receipt-id:";
    private int opCode;

    public Receipt(String id) {
        this.id = this.id + id;
    }

    @Override
    public int getOpCode() {
        return 0;
    }
}
