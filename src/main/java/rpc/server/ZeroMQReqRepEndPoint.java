package rpc.server;

import org.zeromq.ZMQ;
import rpc.api.endpoint.EndPoint;
import rpc.api.endpoint.EndpointMessageHandler;

/**
 * Created by Omar MEBARKI on 22/02/15.
 */
public class ZeroMQReqRepEndPoint implements EndPoint {
    private volatile boolean doWork = true;
    private EndpointMessageHandler<byte[]>messageHandler;

    private final String listeningPort;
    private ZMQ.Socket socket;


    public ZeroMQReqRepEndPoint(String listeningPort) {
        this.listeningPort = listeningPort;
    }


    @Override
    public void start() {
        ZMQ.Context context = ZMQ.context(1);
        socket = context.socket(ZMQ.REP);
        socket.bind(listeningPort);

        while(doWork){
            byte[] message = socket.recv();
            messageHandler.handleMessage(message);

        }
    }

    @Override
    public void stop() {
        doWork = false;
        socket.close();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setMessageHandler(EndpointMessageHandler<?> messageHandler) {
        this.messageHandler = (EndpointMessageHandler<byte[]>) messageHandler;
    }
}
