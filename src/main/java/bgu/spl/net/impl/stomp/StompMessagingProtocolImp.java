package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.StompMessagingProtocol;
import bgu.spl.net.impl.stomp.framesReceived.*;
import bgu.spl.net.impl.stomp.framesToSend.ConnectedCommand;
import bgu.spl.net.impl.stomp.framesToSend.Error;
import bgu.spl.net.impl.stomp.framesToSend.Message;
import bgu.spl.net.impl.stomp.framesToSend.Receipt;
import bgu.spl.net.srv.Connections;
import bgu.spl.net.srv.ConnectionsImp;
import bgu.spl.net.srv.Frame;

import java.util.Map;
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
                System.out.println("making connect");
                frameToSend = connect((ConnectCommand) message);
                connections.send(connectionId, frameToSend);
                break;
            }
            case 2: {
                System.out.println("making disconnect");
                frameToSend = disconnect(connectionId, (DisconnectCommand)message);
                connections.send(connectionId, frameToSend);
                break;
            }
            case 3: {
                String des = ((SendCommand)message).getDestination();
                String body = ((SendCommand)message).getBody();
                frameToSend = new Message("","",des,body);//TODO: take care of the empty strings not needed
                connections.send(des,frameToSend);
                break;
            }
            case 4: {
                frameToSend = subscribe((SubscribeCommand) message);
                connections.send(connectionId, frameToSend);
                break;
            }
            case 5: {
                frameToSend = unsubscribe((Unsubscribe)message);
                if(frameToSend != null)
                    connections.send(connectionId, frameToSend);
                break;
            }
        }
    }

    private Frame disconnect(int connectionId,DisconnectCommand message) {
        if (userName != null) {
            connections.getActiveUsers().replace(userName, false);

        }
        return new Receipt(message.getReceipt(),true);
    }

    private Frame subscribe(SubscribeCommand message) {
        Frame frameToReturn;
        if (userName != null) {
            String des = message.getDestination();
            String id = message.getId();
            if (connections.getTopicMap().containsKey(des)) {
                    connections.getTopicMap().get(des).putIfAbsent(connectionId, id);
                    return new Receipt(message.getReceipt(),false);
                } else {
                connections.getTopicMap().put(des, new ConcurrentHashMap<>());
                connections.getTopicMap().get(des).put(connectionId, id);
                topicList.put(id, des);
                return new Receipt(message.getReceipt(),false);
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
            if (topicList.containsKey(topicId)) {
                connections.getTopicMap().get(topicList.get(topicId)).remove(connectionId);
                topicList.remove(topicId);
                return new Receipt(receiptId,false);
           } else {
                return null;
                //TODO:check if we need to terminate connection because of error
            }
        } else {
            return null;
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
                    frameToReturn = new ConnectedCommand(message.getVersion());
                    this.userName = username;
                }
                else {
                    frameToReturn = new Error("User already logged in");
//                    shouldTerminate = true;
                }
            }
            else {
                frameToReturn = new Error("Wrong password!");
//                shouldTerminate = true;
                //TODO: really close the connection
            }
        } else {
            //create new user
            connections.addUser(username,password);
            frameToReturn = new ConnectedCommand(message.getVersion());
            this.userName = username;
        }
        return frameToReturn;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    private String getIdByTopic (String topic) {
        for (Map.Entry<String, String> entry : topicList.entrySet())
            if (entry.getValue() == topic)
                return entry.getKey();
        return null;
    }

}
