package server;


import org.zeromq.ZMQ;

/**
 * Created by retina on 20/02/15.
 */
public class Server {
    public static void main(String[] args) throws Exception{
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.REP);
        socket.bind("tcp://*:5678");

        while(true){
            byte[] message = socket.recv();
            socket.send("Echo:" + new String(message));
            System.out.println("Echo:" + new String(message));

        }
    }
}
