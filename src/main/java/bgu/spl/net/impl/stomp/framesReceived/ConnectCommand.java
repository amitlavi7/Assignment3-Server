package bgu.spl.net.impl.stomp.framesReceived;

import bgu.spl.net.srv.Frame;

import java.io.Serializable;

public class ConnectCommand implements Frame<String> {

    private String version;
    private String host;
    private String username;
    private String password;
    private int opCode = 1;

    public ConnectCommand(String version,String host, String username, String password) {
        this.version = version;
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public String getVersion() {
        return version;
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
