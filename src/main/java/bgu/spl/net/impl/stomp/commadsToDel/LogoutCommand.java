package bgu.spl.net.impl.stomp.commadsToDel;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class LogoutCommand implements Command<String> {
    @Override
    public Serializable execute(String arg) {
        return null;
    }
}
