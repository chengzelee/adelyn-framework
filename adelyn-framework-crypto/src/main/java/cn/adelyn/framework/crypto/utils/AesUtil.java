package cn.adelyn.framework.crypto.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author chengze
 * @date 2022/12/7
 * @desc 加解密工具
 */
@Component
public class AesUtil implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        password = passwordConfig;
        salt = saltConfig;
    }

    @Value("${adelyn.common.crypto.aes.password:sdfbJKNJKsndskaj//[;[Y&*%^*&(*90FCdkj:./?}")
    private String passwordConfig;

    @Value("${adelyn.common.crypto.aes.salt:1BBF5EBB}")
    private CharSequence saltConfig;

    private static String password;

    private static CharSequence salt;

    private static volatile AesBytesEncryptor aesBytesEncryptor;

    private static AesBytesEncryptor getAesInstance() {
        if (aesBytesEncryptor != null){
            return aesBytesEncryptor;
        }
        synchronized (AesBytesEncryptor.class){
            if (aesBytesEncryptor == null){
                //  password 和 salt 不变，那么生成的密钥也不会变
                aesBytesEncryptor = new AesBytesEncryptor(
                        password, salt, null, AesBytesEncryptor.CipherAlgorithm.CBC
                );
            }
        }
        return aesBytesEncryptor;
    }

    public static byte[] aesEncrypt(byte[] bytes){
        return getAesInstance().encrypt(bytes);
    }

    public static byte[] aesDecrypt(byte[] bytes){
        return getAesInstance().decrypt(bytes);
    }

    public static String aesEncryptStr(String str){
        return new String(Base64.getEncoder().encode(aesEncrypt(str.getBytes(StandardCharsets.UTF_8))));
    }

    public static String aesDecryptStr(String str){
        return new String(aesDecrypt(Base64.getDecoder().decode(str)), StandardCharsets.UTF_8);
    }
}
