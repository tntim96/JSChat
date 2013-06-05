package chat;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import static java.security.Security.addProvider;

public class StateTest {

    @Before
    public void setUp() {
        addProvider(new BouncyCastleProvider());
    }

    @Test
    public void shouldWork() {
        State.password = "secret".toCharArray();
        System.out.println("length = " + State.getKeyFromPassword().getEncoded().length);
    }
}
