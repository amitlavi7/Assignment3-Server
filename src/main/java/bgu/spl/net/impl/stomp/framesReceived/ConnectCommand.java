package bgu.spl.net.impl.stomp.framesReceived;

import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class ConnectCommand implements Frame<String> {

    private String version;
    private String username;
    private String password;
    private int opCode = 1;

    public ConnectCommand(String version, String username, String password) {
        this.version = version;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getOpCode() {
        return opCode;
    }

}

//    @Override
//    public String toString() {
//        return "CONNECTED" + '\n' +
//                "version:" + version + '\n';
//    }
//}
