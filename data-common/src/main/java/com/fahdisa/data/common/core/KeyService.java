package com.fahdisa.data.common.core;

import com.fahdisa.data.common.api.SecretData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class KeyService {

    private static final ObjectMapper OBJECT_MAPPER = Jackson.newObjectMapper();


    public SecretKey generateKey() throws GeneralSecurityException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BCFIPS");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }


    public SecretKey loadKey(byte[] keyBytes) {
        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            throw new IllegalArgumentException("keyBytes wrong length for AES key");
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    public Map<String, String> encrypt(String transactionId, String pan, String expiration, String cvv) {
        SecretData secretData = new SecretData();
        secretData.setCvv(cvv);
        secretData.setExpiration(expiration);
        secretData.setPan(pan);
        secretData.setTransactionId(transactionId);

        try {
            String data = OBJECT_MAPPER.writeValueAsString(secretData);
            SecretKey key = KeyUtil.generateKey();
            byte[] encrypted = KeyUtil.encrypt(key, data.getBytes(StandardCharsets.UTF_8));
            String hex = KeyUtil.toHex(encrypted);
            Map<String, String> map = new HashMap<>();
            map.put("key", KeyUtil.toHex(key.getEncoded()));
            map.put("token", hex);
            return map;
        } catch (JsonProcessingException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String key, String data) {
        try {
            byte[] keyData = Hex.decode(key);
            SecretKey secretKey = loadKey(keyData);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BCFIPS");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Hex.decode(data)));
        } catch (GeneralSecurityException e) {
            return null;
        }
    }

}
