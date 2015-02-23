package rpc.server;

import org.junit.Test;
import rpc.api.endpoint.EndPoint;
import rpc.api.endpoint.EndpointMessageHandler;

import java.util.ArrayList;

/**
 * Created by Omar MEBARKI on 22/02/15.
 */


public class RPCServerTest {
    @Test
    public void simpleTest() {

        EndPoint endPoint = new ZeroMQReqRepEndPoint("tcp://*:12345");
        EndpointMessageHandler<byte[]> messageHandler = new ZeroMQMessageBytesHandler(new ArrayList<>());
        endPoint.setMessageHandler(messageHandler);


        RPCServer rpcServer = new RPCServer(endPoint);
        rpcServer.start();


        try {
            System.out.printf("before sleep");
            Thread.sleep(10000);
            System.out.printf("after sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("stop");
         rpcServer.stop();
        System.out.printf("End");
    }
}
