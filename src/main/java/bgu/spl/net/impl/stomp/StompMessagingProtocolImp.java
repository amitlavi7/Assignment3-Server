package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.framesReceived.ConnectCommand;
import bgu.spl.net.impl.stomp.framesReceived.DisconnectCommand;
import bgu.spl.net.impl.stomp.framesReceived.SubscribeCommand;
import bgu.spl.net.impl.stomp.framesToSend.ConnectedCommand;
import bgu.spl.net.impl.stomp.framesToSend.Error;
import bgu.spl.net.impl.stomp.framesToSend.Receipt;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.Frame;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StompMessagingProtocolImp implements StompMessagingProtocol {

    private boolean shouldTerminate = false;

    private ConnectionsImp<String> connections;

    private int connectionId;
    private ConcurrentHashMap<String,String> topicList;  //left - subid, right - topicname
    private String userName;
    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = (ConnectionsImp)connections;
        topicList = new ConcurrentHashMap<>();
    }

    @Override
    public void process(Frame<String> message) {
        Frame frameToReturn;
        switch (message.getOpCode()) {
            case 1:
                frameToReturn = connect((ConnectCommand) message);
            case 2: {
                if(userName != null ) {
                    connections.getActiveUsers().put(userName,false);
                    Receipt receipt = new Receipt(Integer.toString(connectionId));
                    //dissconectuser
                }
            }
            case 4:{
                if(userName != null){
                    String des = ((SubscribeCommand)message).getDestination();
                    String id  = ((SubscribeCommand)message).getId();
                    if(connections.getTopicMap().contains(des)) {
                        if(!topicList.contains(id)) {
                            connections.getTopicMap().get(des).put(connectionId, id);
                            frameToReturn = new Receipt(((SubscribeCommand)message).getReceipt());
                        }
                        else
                           frameToReturn = new Error("The user is already subscribed to this genre");
                    }
                    else{
                       connections.getTopicMap().put(des,new ConcurrentHashMap<>());
                        connections.getTopicMap().get(des).put(connectionId,id);
                        topicList.put(id,des);
                    }
                }
            }
        }
    }

    private Frame connect(ConnectCommand message) {
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
        return frameToReturn;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}
