package bgu.spl.net.srv;

import bgu.spl.net.impl.stomp.framesReceived.SubscribeCommand;
//import javafx.util.Pair;
import jdk.internal.util.xml.impl.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImp<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connectionHandlerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<Integer,Integer>>> topicMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> usersDetailsMap = new ConcurrentHashMap<>();
    private HashMap<String,Boolean> activeUsers = new HashMap<>();

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<Integer, Integer>>> getTopicMap() {
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

    public void addToTopicMap (SubscribeCommand subscriber) {
        topicMap.put(subscriber.getDestination(), );
    }

    public void addUser (String username, String password) {
        usersDetailsMap.put(username,password);
        activeUsers.put(username,true);
    }




}
