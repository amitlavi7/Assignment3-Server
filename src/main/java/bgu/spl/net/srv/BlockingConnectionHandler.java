package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.StompMessagingProtocol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final StompMessagingProtocol protocol;
    private final MessageEncoderDecoder<T> encdec;
    private ConnectionsImp<T> connections;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private int connectionId;

    public BlockingConnectionHandler(
            Socket sock,
            MessageEncoderDecoder<T> reader,
            StompMessagingProtocol protocol,
            ConnectionsImp<T> connections,int connectionId)
    {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
        this.connections = connections;
        this.connectionId = connectionId;
    }

    @Override
    public void run() {
        protocol.start(connectionId,(ConnectionsImp<String>)connections);
        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                Frame<String> nextMessage = (Frame<String>) encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    System.out.println("sending to process, opCode:" + nextMessage.getOpCode());
                     protocol.process(nextMessage);
                }
            }
            System.out.println("closing connection");
        close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public void send(T msg) {
        if(msg!=null) {
            try {
                out.write(encdec.encode(msg));
                System.out.println("message was sent");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
