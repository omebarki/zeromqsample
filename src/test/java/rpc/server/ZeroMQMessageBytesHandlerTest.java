package rpc.server;

import com.google.protobuf.ByteString;
import omar.mebarki.people.PersonOuterClass;
import org.junit.Test;
import rpc.api.service.ServiceHandler;
import omar.mebarki.envelop.Envelop;
import rpc.util.Util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar MEBARKI on 22/02/15.
 */
public class ZeroMQMessageBytesHandlerTest {

    public static final String TEST_SERVICE = "testService";

    private static interface SimpleService {
    }

    @Test
    public void testMarshalling() throws Exception {
        ServiceHandler serviceHandler = new ServiceHandlerImpl<>(TestService.class, new TestService() {
            @Override
            public void sayHello(PersonOuterClass.Person person) {
                System.out.println(person.getName());
            }

            @Override
            public ByteString sayHello2(PersonOuterClass.Person person) {
                return person.getNameBytes();
            }
        }, TEST_SERVICE);
        List<ServiceHandler> servicesHandlers = new ArrayList<ServiceHandler>();
        servicesHandlers.add(serviceHandler);
        ZeroMQMessageBytesHandler zeroMQMessageBytesHandler = new ZeroMQMessageBytesHandler(servicesHandlers);
        Envelop.CallEnvelop callEnvelop = makeSayHello2ReoteCall();
        zeroMQMessageBytesHandler.handleMessage(callEnvelop.toByteArray());
    }

    private Envelop.CallEnvelop makeSayHello2ReoteCall() throws Exception {
        final Method method = TestService.class.getMethod("sayHello2", PersonOuterClass.Person.class);
        PersonOuterClass.Person perso = PersonOuterClass.Person.newBuilder().
                setEmail("email@ff.fr").
                setId(1).
                setName("omar").
                build();

        Envelop.CallEnvelop callEnvelop = Envelop.CallEnvelop.newBuilder().
                setService(TEST_SERVICE).
                setMethodHash(Util.computeMethodHash(method)).
                addArgs(perso.toByteString()).
                addArgs(ByteString.copyFromUtf8("tt"))
                .build();
        return callEnvelop;
    }

    private static interface TestService {
        void sayHello(PersonOuterClass.Person person);

        ByteString sayHello2(PersonOuterClass.Person person);
    }
}
