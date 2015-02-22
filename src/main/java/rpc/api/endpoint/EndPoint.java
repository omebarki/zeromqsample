package rpc.api.endpoint;

/**
 * Created by Omar MEBARKI on 22/02/15.
 */
public interface EndPoint {
    public void start();

    public void stop();

    /**
     * Message handling is done sequentially
     * @param messageHandler
     */
    public void  setMessageHandler(EndpointMessageHandler<?> messageHandler);

}
