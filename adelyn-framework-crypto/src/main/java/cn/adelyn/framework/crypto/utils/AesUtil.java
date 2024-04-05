package cn.adelyn.framework.crypto.utils;

import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chengze
 * @date 2022/12/7
 * @desc 加解密工具
 */
@Component
public class AesUtil {

    private static final Map<String, AesBytesEncryptor> aesBytesEncryptor = new ConcurrentHashMap<>();

    public static byte[] aesEncrypt(String password, String salt, byte[] data){
        return getEncryptor(password, salt).encrypt(data);
    }

    public static byte[] aesDecrypt(String password, String salt, byte[] data){
        return getEncryptor(password, salt).decrypt(data);
    }

    public static String aesEncryptStr(String password, String salt, String data){
        return new String(Base64.getEncoder().encode(aesEncrypt(password, salt, data.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
    }

    public static String aesDecryptStr(String password, String salt, String data){
        return new String(aesDecrypt(password, salt, Base64.getDecoder().decode(data)), StandardCharsets.UTF_8);
    }

    private static AesBytesEncryptor getEncryptor(String password, String salt) {
        String key = (password + "-" + salt).intern();
        return aesBytesEncryptor.computeIfAbsent(key, encryptor -> new AesBytesEncryptor(
                password, salt, null, AesBytesEncryptor.CipherAlgorithm.CBC
        ));
    }
}
