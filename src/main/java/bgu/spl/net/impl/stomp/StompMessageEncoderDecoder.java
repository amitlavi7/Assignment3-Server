package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.stomp.framesReceived.*;
import bgu.spl.net.impl.stomp.framesToSend.ConnectedCommand;
import bgu.spl.net.impl.stomp.framesToSend.Error;
import bgu.spl.net.impl.stomp.framesToSend.Message;
import bgu.spl.net.impl.stomp.framesToSend.Receipt;
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
//            System.out.println("building frame");
            words.addLast(popString()); //words.add
            return buildFrame();
        }
        if (nextByte == '\n') {
            System.out.println("backslash n");
            words.addLast(popString()); //words.add
//            return null;
        }
        pushByte (nextByte);
        return null;
    }

    @Override
    public byte[] encode(Frame<String> frame) {
//        Field[] fields = frame.getClass().getDeclaredFields();
//        StringBuilder frameToBytes = new StringBuilder();
//        for(int i = 0; i < fields.length; i++)
//            frameToBytes.append(fields[i]).append('\n');
          StringBuilder frameToBytes = new StringBuilder();
          switch (frame.getOpCode()) {
              case (1): { //connected
                  ConnectedCommand command = ((ConnectedCommand)frame);
                  frameToBytes.append(command.getConnected()).append("\n").append(command.getVersion()).append("\n");
                  break;
              }
              case (2): { //error
                  Error command = ((Error)frame);
                  frameToBytes.append(command.getError()).append("\n").append(command.getCauseOfError()).append("\n");
                  break;
              }
              case  (3): { //message
                  Message command = ((Message)frame);
                  frameToBytes.append(command.getMessgae()).append("\n").
                          append(command.getSub()).append("\n").
                          append(command.getId()).append("\n").
                          append(command.getDes()).append("\n").
                          append(command.getBody()).append("\n");
                  break;
              }
              case (4): { // receipt
                  Receipt command = ((Receipt)frame);
                  frameToBytes.append(command.getReceipt()).append("\n").append(command.getId()).append("\n");
              }
          }
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
        System.out.print(res);//////////for debug
        return res;
    }

    private Frame<String> buildFrame(){
        System.out.println("got case: " + words.getFirst());
        System.out.println(words.toString());
        switch (words.getFirst()){
            case ("CONNECT"): {
                System.out.println("case: connect");
                String version = words.get(1).split(":")[1];
                String host = words.get(2).split(":")[1];
                String login = words.get(3).split(":")[1];
                String pass = words.get(4).split(":")[1];
                words.clear();
                return new ConnectCommand(version,host ,login ,pass);
            }
            case ("SUBSCRIBE"): {
                System.out.println("case: subscribe");
                String destination = words.get(1).split(":")[1];
                String id = words.get(2).split(":")[1];
                String receipt = words.get(3).split(":")[1];
                words.clear();
                return new SubscribeCommand(destination, id, receipt);
            }
            case ("SEND"): {
                System.out.println("case: send");
                String destination = words.get(1).split(":")[1];;
                System.out.println("check for word: " + words.toString());
                String body = words.get(3);///////////////stam besvil tom
                words.clear();
                return new SendCommand(destination,body);
            }
            case ("DISCONNECT"): {
                System.out.println("case: disconnect");
                String receipt = words.get(1).split(":")[1];
                words.clear();
                return new DisconnectCommand(receipt);
            }
            case ("UNSUBSCRIBE"):{
                System.out.println("case: unsubscribe");
                String id = words.get(1).split(":")[1];
                String receipt = words.get(2).split(":")[1];
                words.clear();
                return new Unsubscribe(id, receipt);
            }
            default: return null;
        }
    }
}
