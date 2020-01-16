package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

public class StompServer {

    public static void main(String[] args) {
        switch (args[1]) {
            case "tpc": {
                System.out.println("Server started with tpc");
                Server.threadPerClient(
                        Integer.parseInt(args[0]), //port
                        () -> new StompMessagingProtocolImp(), //protocol factory
                        () -> new StompMessageEncoderDecoder() //message encoder decoder factory
                ).serve();
                break;
            }
            case "reactor": {
                System.out.println("Server started with reactor");
                Server.reactor(
                        Runtime.getRuntime().availableProcessors(),
                        Integer.parseInt(args[0]), //port
                        () -> new StompMessagingProtocolImp(), //protocol factory
                        () -> new StompMessageEncoderDecoder() //message encoder decoder factory
                ).serve();
                break;
            }
        }
    }
}
