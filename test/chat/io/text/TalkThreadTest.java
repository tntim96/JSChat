package chat.io.text;

import chat.menu.CryptMenu;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.ObjectInputStream;
import java.security.Key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TalkThreadTest {
    {
        java.security.Security.addProvider(new BouncyCastleProvider());
    }

    public Cipher getPublicCipher() throws Exception {
        ObjectInputStream in = new ObjectInputStream(getClass().getResourceAsStream("/rsa-public.key"));
        Key publicKey = (Key) in.readObject();
        in.close();
        Cipher cipher = CryptMenu.getAsymmetricCipher();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher;
    }

    public Key getKey(String resource) throws Exception {
        ObjectInputStream in = new ObjectInputStream(getClass().getResourceAsStream(resource));
        Key key = (Key) in.readObject();
        in.close();
        return key;
    }

    @Test
    public void shouldEncryptAndDecryptShortDataWithRSA() throws Exception {
        String longString = "Short string";
        byte[] orig = longString.getBytes();
//        byte[] encrypted = TalkThread.encrypt(CryptMenu.getAsymmetricCipher(), orig, getKey("/rsa-public.key"));
//        byte[] decrypted = TalkThread.decrypt(CryptMenu.getAsymmetricCipher(), encrypted, getKey("/rsa-private.key"));
//
//        assertEquals(new String(decrypted), longString);
    }

    @Test
    @Ignore
    public void shouldEncryptAndDecryptLongDataWithRSA() throws Exception {
        String longString = "This string is longer than the RSA cipher block size....................................................................................";
        byte[] orig = longString.getBytes();
        assertTrue(longString.length() > CryptMenu.getAsymmetricCipher().getBlockSize());
//        byte[] encrypted = TalkThread.encrypt(CryptMenu.getAsymmetricCipher(), orig, getKey("/rsa-public.key"));
//        byte[] decrypted = TalkThread.decrypt(CryptMenu.getAsymmetricCipher(), encrypted, getKey("/rsa-private.key"));
//
//        assertEquals(new String(decrypted), longString);
    }
}
