package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.util.Arrays;
import java.util.LinkedList;

public class StompMessageEncoderDecoder implements MessageEncoderDecoder<LinkedList<String>> {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private LinkedList<String> words = new LinkedList<>();

    @Override
    public LinkedList<String> decodeNextByte(byte nextByte) {
        if (nextByte == '\u0000') {
            return words;
        }
        if (nextByte == '\n') {
            words.add(popString());
        }
        pushByte (nextByte);
        return null;
    }

    @Override
    public byte[] encode(LinkedList<String> words) {
        String message = "";
        for (String word: words) {
            message = message + word + '\n';
        }
        return (message + '\u0000').getBytes();
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
}
