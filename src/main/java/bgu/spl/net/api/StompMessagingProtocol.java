package bgu.spl.net.api;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.Frame;

import java.util.LinkedList;

public interface StompMessagingProtocol  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId, Connections<String> connections);
    
    void process(Frame<String> message);
	
	/**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}
