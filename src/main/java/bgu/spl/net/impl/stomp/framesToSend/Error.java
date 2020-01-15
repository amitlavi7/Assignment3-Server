package bgu.spl.net.impl.stomp.framesToSend;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class Error implements Frame<String> {

    private String error = "ERROR";
    private String causeOfError;

    public Error(String causeOfError) {
        this.causeOfError = causeOfError;
    }

    public String getError() {
        return error;
    }

    public String getCauseOfError() {
        return causeOfError;
    }

    @Override
    public int getOpCode() {
        return 2;
    }
}
