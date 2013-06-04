package chat.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

public class SecurityUtils {

    public Key convertToKey(String password) {
        try {
            KeySpec ks = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("AES", "BC");
            return skf.generateSecret(ks);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        try {
//            byte[] bytes1 = getBytes(password.getBytes());
//            byte[] bytes2 = getBytes(bytes1);
//            byte[] bytes3 = getBytes(bytes2);
//            byte[] bytes4 = getBytes(bytes3);
//            byte[] bytes = new byte[bytes1.length * 4];
//            System.arraycopy(bytes1, 0, bytes, 0, bytes1.length);
//            System.arraycopy(bytes2, 0, bytes, bytes1.length, bytes2.length);
//            System.arraycopy(bytes3, 0, bytes, bytes1.length * 2, bytes3.length);
//            System.arraycopy(bytes4, 0, bytes, bytes1.length * 3, bytes4.length);
//            SecretKeyFactory skf = SecretKeyFactory.getInstance("AES", "BC");
//
//
//            return skf.generateSecret(new SecretKeySpec(bytes, "AES"));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    private byte[] getBytes(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(data);
        return md.digest();
    }

    public byte[] encrypt(Key key, byte[] bytes) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] decrypt(Key key, byte[] bytes) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
