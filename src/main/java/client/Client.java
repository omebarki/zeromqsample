package client;

import org.zeromq.ZMQ;

/**
 * Created by retina on 21/02/15.
 */
public class Client {
    public static void main(String[] args) throws Exception{
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://127.0.0.1:5678");
        socket.send("Hello");
        System.out.println(new String(socket.recv()));

        socket.send("World");
        System.out.println(new String(socket.recv()));

    }
}
