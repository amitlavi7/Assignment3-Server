package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class LoginCommand implements Command<String> {

    private String host;
    private String username;
    private String password;

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Serializable execute(String cmd) {
        return null;
    }
}
