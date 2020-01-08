package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.stomp.framesReceived.*;
import bgu.spl.net.srv.Frame;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;

public class StompMessageEncoderDecoder implements MessageEncoderDecoder<Frame<String>> {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private LinkedList<String> words = new LinkedList<>();

    @Override
    public Frame<String> decodeNextByte(byte nextByte) {
        if (nextByte == '\u0000') {
            return buildFrame();
        }
        if (nextByte == '\n') {
            words.add(popString());
        }
        pushByte (nextByte);
        return null;
    }

    @Override
    public byte[] encode(Frame<String> frame) {
        Field[] fields = frame.getClass().getDeclaredFields();
        StringBuilder frameToBytes = new StringBuilder();
        for(int i = 0; i < fields.length; i++)
            frameToBytes.append(fields[i]).append('\n');

        return (frameToBytes.toString() + '\u0000').getBytes();
    }

    private void pushByte (byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private String popString() {
        String res = new String(bytes,0,len);
        len = 0;
        return res;
    }

    private Frame<String> buildFrame(){
        switch (words.getFirst()){
            case ("CONNECT"): {
                String version = words.get(1).split(":")[1];
                String login = words.get(2).split(":")[1];
                String pass = words.get(3).split(":")[1];
                return new ConnectCommand(version,login ,pass);
            }
            case ("SUBSCRIBE"): {
                String destination = words.get(1).split(":")[1];
                String id = words.get(2).split(":")[1];
                String receipt = words.get(3).split(":")[1];
                return new SubscribeCommand(destination, id, receipt);
            }
            case ("SEND"): {
                String destination = words.get(1).split(":")[1];;
                String body = words.get(2).split(":")[1];;
                return new SendCommand(destination,body);
            }
            case ("DISCONNECT"): {
                String receipt = words.get(1).split(":")[1];
                return new DisconnectCommand(receipt);
            }
            case ("UNSUBSCRIBE"):{
                String id = words.get(1).split(":")[1];
                return new Unsubscribe(id);
            }
            default: return null;
        }
    }
}
