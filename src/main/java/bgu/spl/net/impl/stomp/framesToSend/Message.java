package bgu.spl.net.impl.stomp.framesToSend;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class Message implements Frame<String> {
    private String messgae = "MESSAGE";
    private String sub = "subscription:";
    private String id = "Message-id:";
    private String des = "destination:";
    private String body;

    public Message(String sub, String id, String des, String body) {
        this.sub = this.sub + sub;
        this.id = this.id + id;
        this.des = this.des + des;
        this.body = body;
    }

    @Override
    public int getOpCode() {
        return 0;
    }
}
