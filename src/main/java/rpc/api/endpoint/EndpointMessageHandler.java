package rpc.api.endpoint;

/**
 * Created by Omar MEBARKI on 22/02/15.
 */
public interface EndpointMessageHandler<T> {
    public Object handleMessage(T message);
}
