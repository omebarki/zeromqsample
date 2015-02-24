package rpc.server;

import com.google.protobuf.ByteString;
import omar.mebarki.envelop.Envelop;
import rpc.api.service.ServiceHandler;
import rpc.util.Util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Omar MEBARKI
 * Date: 2/24/2015
 */
public class ServiceHandlerImpl<T> implements ServiceHandler {
    private final Object delegate;
    private final Class<T> service;
    private final String serviceName;
    private Map<String, Method> methods = new HashMap<>();

    public ServiceHandlerImpl(Class<T> service, Object delegate, String serviceName) {
        if (service == null) {
            throw new NullPointerException("service is null");
        }
        if (!service.isInterface()) {
            throw new IllegalArgumentException("the service class must be an interface");
        }
        if (delegate == null) {
            throw new NullPointerException("delegate is null");
        }
        if (serviceName == null) {
            throw new NullPointerException("serviceName is null");
        }
        this.service = service;
        this.delegate = delegate;
        this.serviceName = serviceName;
        init();
    }

    private void init() {
        final Method[] iMethods = service.getMethods();
        for (Method method : iMethods) {
            methods.put(Util.computeMethodHash(method), method);
        }
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public Object handleCall(Object call) {
        Envelop.CallEnvelop remoteCall = (Envelop.CallEnvelop) call;
        Method method = methods.get(remoteCall.getMethodHash());
        if (method == null) {
            throw new RuntimeException("No mothod for hash:" + remoteCall.getMethodHash());
        }
        try {
            Object[] args = transformArgs(method, remoteCall.getArgsList()).toArray();
            Object result = method.invoke(delegate, args);
            Envelop.ResponseEnvelop responseEnvelop = Envelop.ResponseEnvelop.newBuilder()
                    .setIsOK(true)
                    .setService(serviceName)
                    .setMethodHash(remoteCall.getMethodHash())
                    .setResult(0, toByteString(result))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static List<?> transformArgs(Method method, List<ByteString> argsLis) throws Exception {
        List<Object> result = new ArrayList<Object>();
        final Iterator<ByteString> iterator = argsLis.iterator();
        for (Parameter parameter : method.getParameters()) {
            final Class<?> type = parameter.getType();
            final Method typeMethodCreator = type.getMethod("parseFrom", byte[].class);
            result.add(typeMethodCreator.invoke(null, iterator.next().toByteArray()));
        }
        return result;
    }

    private static ByteString toByteString(Object object) throws Exception {
        final Class<?> aClass = object.getClass();
        final Method method = aClass.getMethod("toByteString");
        if (method == null) {
            throw new RuntimeException("No mothod toByteString");
        }
        ByteString byteString = (ByteString) method.invoke(object);
        return byteString;
    }
}
