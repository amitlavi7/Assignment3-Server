package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.framesReceived.SubscribeCommand;
import jdk.internal.util.xml.impl.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionsImp<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionHandlerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer,String>> topicMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> usersDetailsMap = new ConcurrentHashMap<>();
    private HashMap<String,Boolean> activeUsers = new HashMap<>();

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
//        clients.get(connectionId).

        return false;
    }
    @Override
    public void send(String channel, T msg) {

    }

    @Override
    public void disconnect(int connectionId) {
//        synchronized (this?);
        connectionHandlerMap.remove(connectionId);
    }

    public void addConnectionHandler(int id,ConnectionHandler<T> connectionHandler){
        connectionHandlerMap.put(id,connectionHandler);
    }


    public void addUser (String username, String password) {
        usersDetailsMap.put(username,password);
        activeUsers.put(username,true);
    }




}
