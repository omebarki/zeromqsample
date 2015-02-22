package rpc.server;

import rpc.api.endpoint.EndPoint;

/**
 * Created by retina on 22/02/15.
 */
public class RPCServer {
    private volatile boolean doWork = true;
    private final EndPoint endPoint;

    public RPCServer(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public synchronized void start(){
        if(doWork){
               Thread t = new
                       Thread(new Runnable() {
                   @Override
                   public void run() {
                         endPoint.start();
                   }
               });
            t.start();
        }
    }

    public synchronized void stop(){
        doWork = false;
        endPoint.stop();
    }
}
