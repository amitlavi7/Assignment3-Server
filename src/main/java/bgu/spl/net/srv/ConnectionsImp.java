package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.framesToSend.Message;
import bgu.spl.net.impl.stomp.framesToSend.Receipt;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImp<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionHandlerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> topicMap = new ConcurrentHashMap<>();//second hash, <conectionid, subid>
    private ConcurrentHashMap<String, String> usersDetailsMap = new ConcurrentHashMap<>();
    private HashMap<String, Boolean> activeUsers = new HashMap<>();
    private int msgId = 0;

    public ConcurrentHashMap<String, ConcurrentHashMap<Integer, String>> getTopicMap() {
        return topicMap;
    }

    public ConcurrentHashMap<String, String> getUsersDetailsMap() {
        return usersDetailsMap;
    }

    public HashMap<String, Boolean> getActiveUsers() {
        return activeUsers;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if (msg != null) {
            System.out.println("connection ID: " + connectionId);//////for debug
            connectionHandlerMap.get(connectionId).send(msg);
            if (msg.getClass() == Receipt.class) {
                if (((Receipt) msg).isDisconnect()) {
                    disconnect(connectionId);
                    for(String topic : topicMap.keySet()){
                        topicMap.get(topic).remove(connectionId);
                    }
                }
            }
            return true;
        }
//TODO: figure out why bool
        return false;
    }

    @Override
    public void send(String channel, T msg) {
        topicMap.putIfAbsent(channel, new ConcurrentHashMap<>());
        ConcurrentHashMap<Integer, String> toSend = topicMap.get(channel);
        for (Integer conId : toSend.keySet()) {
            System.out.println("Im inside for loop of send by topic");
            ((Message) msg).setSub(toSend.get(conId));
            ((Message) msg).increaseMsgId(String.valueOf(msgId++));
            connectionHandlerMap.get(conId).send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connectionHandlerMap.remove(connectionId);
    }

    public void addConnectionHandler(int id, ConnectionHandler<T> connectionHandler) {
        connectionHandlerMap.put(id, connectionHandler);
    }


    public void addUser(String username, String password) {
        usersDetailsMap.put(username, password);
        activeUsers.put(username, true);
    }


}
