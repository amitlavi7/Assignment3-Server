package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.framesReceived.ConnectCommand;
import bgu.spl.net.impl.stomp.framesReceived.SendCommand;
import bgu.spl.net.impl.stomp.framesReceived.SubscribeCommand;
import bgu.spl.net.impl.stomp.framesReceived.Unsubscribe;
import bgu.spl.net.impl.stomp.framesToSend.ConnectedCommand;
import bgu.spl.net.impl.stomp.framesToSend.Error;
import bgu.spl.net.impl.stomp.framesToSend.Message;
import bgu.spl.net.impl.stomp.framesToSend.Receipt;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.Frame;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StompMessagingProtocolImp implements StompMessagingProtocol {

    private boolean shouldTerminate = false;

    private ConnectionsImp<Frame> connections;

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
        Frame frameToSend;
        switch (message.getOpCode()) {
            case 1: {
                frameToSend = connect((ConnectCommand) message);
                connections.send(connectionId, frameToSend);
            }
            case 2: {
                frameToSend = disconnect(connectionId);
                connections.send(connectionId, frameToSend);
            }
            case 3: {
                String des = ((SendCommand)message).getDestination();
                String body = ((SendCommand)message).getBody();
                frameToSend = new Message("","",des,body);
                connections.send(des,frameToSend);
            }
            case 4: {
                frameToSend = subscribe((SubscribeCommand) message);
                connections.send(connectionId, frameToSend);
            }
            case 5: {
                frameToSend = unsubscribe((Unsubscribe)message);
                connections.send(connectionId, frameToSend);
            }
        }
    }

    private Frame disconnect(int connectionId) {
        if (userName != null) {
            connections.getActiveUsers().put(userName, false);
        }
        shouldTerminate = true;
        return new Receipt(Integer.toString(connectionId));
    }

    private Frame subscribe(SubscribeCommand message) {
        Frame frameToReturn;
        if (userName != null) {
            String des = message.getDestination();
            String id = message.getId();
            if (connections.getTopicMap().contains(des)) {
                if (!topicList.contains(id)) {
                    connections.getTopicMap().get(des).put(connectionId, id);
                    return new Receipt(message.getReceipt());
                } else
                    return new Error("The user is already subscribed to this genre");
                //TODO:check if we need to terminate connection because of error
            } else {
                connections.getTopicMap().put(des, new ConcurrentHashMap<>());
                connections.getTopicMap().get(des).put(connectionId, id);
                topicList.put(id, des);
                return new Receipt(message.getReceipt());
            }
        }
        else
            return new Error("Username does not log in");
        //TODO:check if we need to terminate connection because of error
    }

    private Frame unsubscribe(Unsubscribe message) {
        if (userName != null) {
            String topicId = message.getId();
            String receiptId = message.getReceipt();
            if (topicList.contains(topicId)) {
                connections.getTopicMap().get(topicList.get(topicId)).remove(connectionId);
                topicList.remove(topicId);
                return new Receipt(receiptId);
            } else {
                return new Error("The user has not subscribed to this topic");
                //TODO:check if we need to terminate connection because of error
            }
        } else {
            return new Error("User does not exist");
            //TODO:check if we need to terminate connection because of error

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
                    frameToReturn = new Error("User already logged in");
                    shouldTerminate = true;
                }
            }
            else {
                frameToReturn = new Error("Wrong password!");
                shouldTerminate = true;
                //TODO: really close the connection
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
