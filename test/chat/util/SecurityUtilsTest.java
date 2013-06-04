package chat.util;

import chat.menu.CryptMenu;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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

    @Test
    public void should() throws Exception {
        for (int i = 1; i < 500; i++) {
            //CryptMenu.symmetricBlockStrength = i;
            try {
                Cipher encrypter = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
                Cipher decrypter = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");

                byte[] keyBytes = new byte[i];
//        for (int i = 0; i < keyBytes.length; i++) {
//            keyBytes[i] = 0;
//        }

                byte[] iv = new byte[16];
                IvParameterSpec spec = new IvParameterSpec(iv);
                SecretKey fileSessionKey = new SecretKeySpec(keyBytes, "AES");


                encrypter.init(Cipher.ENCRYPT_MODE, fileSessionKey, spec);
                System.out.println("Worked for " + i);
            } catch(Throwable t) {
                //System.out.println("t = " + t);
            }
        }
    }
}
