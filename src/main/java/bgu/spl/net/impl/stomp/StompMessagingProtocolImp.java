package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.framesReceived.ConnectCommand;
import bgu.spl.net.impl.stomp.framesReceived.Unsubscribe;
import bgu.spl.net.impl.stomp.framesToSend.ConnectedCommand;
import bgu.spl.net.impl.stomp.framesToSend.Error;
import bgu.spl.net.impl.stomp.framesToSend.Receipt;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.Frame;

import java.util.LinkedList;

public class StompMessagingProtocolImp implements StompMessagingProtocol {

    private boolean shouldTerminate = false;

    private ConnectionsImp<String> connections;

    private int connectionId;
    private LinkedList<String> topicList;
    private String userName;
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = (ConnectionsImp)connections;
        topicList = new LinkedList<>();
    }

    @Override
    public void process(Frame<String> message) {
        switch (message.getOpCode()) {
            case 1:
                connect((ConnectCommand) message);
            case 2: {
                Receipt receipt = new Receipt(Integer.toString(connectionId));
                if(userName != null ) {
                    connections.getActiveUsers().put(userName,false);
                    //dissconectuser
                }
            }
            case 4:{

            }
            case 5: {
                if (userName != null) {
                    String topic = ((Unsubscribe)message).getDestination();
                    if (connections.getTopicMap().contains(topic))
                        connections.getTopicMap().get(topic)
                    Unsubscribe unsubscribe = new Unsubscribe(Integer.toString(connectionId));
                }
            }
        }
    }

    private void connect(ConnectCommand message) {
        String username = message.getUsername();
        String password = message.getPassword();
        Frame frameToReturn;
        if (connections.getUsersDetailsMap().containsKey((username))){
            if(connections.getUsersDetailsMap().get(message.getUsername()).equals((password))){
                if(!connections.getActiveUsers().get(username)){
                    frameToReturn = new ConnectedCommand("1.2");
                    this.userName = username;
                }
                else {
                    frameToReturn = new Error("The user have an active connection");
                    //TODO:close connection
                }
            }
            else {
                frameToReturn = new Error("Wrong password!");
                //TODO:close connection
            }
        } else {
            //create new user
            connections.addUser(username,password);
            frameToReturn = new ConnectedCommand("1.2");
            this.userName = username;
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
