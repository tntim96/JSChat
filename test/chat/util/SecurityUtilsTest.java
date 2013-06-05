package chat.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import static java.security.Security.addProvider;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SecurityUtilsTest {
    private SecurityUtils securityUtils = new SecurityUtils();

    @Before
    public void setUp() {
        addProvider(new BouncyCastleProvider());
    }

    @Test
    public void shouldGenerateSymmetricKeyFromPassword() {
        Key key = securityUtils.convertToKey("password");
        String data = "Data";
        byte[] encrypted = securityUtils.encrypt(key, data.getBytes());
        byte[] decrypted = securityUtils.decrypt(key, encrypted);
        assertThat(decrypted, equalTo(data.getBytes()));
    }
}
