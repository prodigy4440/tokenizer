package com.fahdisa.data.common.core;

import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Security;

public class KeyUtil {

    static {
        Security.addProvider(new BouncyCastleFipsProvider());
    }

    public static SecretKey generateKey() throws GeneralSecurityException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BCFIPS");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    public static SecretKey loadKey(byte[] keyBytes) {
        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            throw new IllegalArgumentException("keyBytes wrong length for AES key");
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static byte[] encrypt(SecretKey key, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BCFIPS");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(SecretKey key, byte[] cipherText)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BCFIPS");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    public static String toHex(byte[] data){
        return Hex.toHexString(data);
    }

}
