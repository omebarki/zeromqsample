package rpc.server;

import com.google.protobuf.ByteString;
import omar.mebarki.envelop.Envelop;
import rpc.api.endpoint.EndpointMessageHandler;
import rpc.api.service.ServiceHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Omar MEBARKI on 22/02/15.
 */
public class ZeroMQMessageBytesHandler implements EndpointMessageHandler<byte[]> {
    private Map<String, ServiceHandler> services = new HashMap<>();

    public ZeroMQMessageBytesHandler(List<ServiceHandler> serviceHandlers) {
        for (ServiceHandler serviceHandler : serviceHandlers) {
            services.put(serviceHandler.getServiceName(), serviceHandler);
        }
    }

    @Override
    public Object handleMessage(byte[] message) {
        Envelop.ResponseEnvelop responseEnvelop = null;
        String serviceName = null;
        try {
            final Envelop.CallEnvelop remoteCall = Envelop.CallEnvelop.parseFrom(message);
            serviceName = remoteCall.getService();
            ServiceHandler serviceHandler = services.get(serviceName);
            if (serviceHandler == null) {
                throw new Exception("Unsupported service:" + remoteCall.getService());
            }
            responseEnvelop = (Envelop.ResponseEnvelop)serviceHandler.handleCall(remoteCall);
        } catch (Exception e) {
            responseEnvelop = Envelop.ResponseEnvelop.newBuilder()
                    .setIsOK(false)
                    .setService(serviceName)
                    .setResult(0, ByteString.copyFromUtf8(e.getMessage()))
                    .build();
        }
        return responseEnvelop;
    }
}

