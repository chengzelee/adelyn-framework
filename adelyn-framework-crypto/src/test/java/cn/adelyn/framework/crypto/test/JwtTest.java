package cn.adelyn.framework.crypto.test;

import cn.adelyn.framework.crypto.constant.AlgoConstant;
import cn.adelyn.framework.crypto.utils.JwtUtil;
import cn.adelyn.framework.crypto.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class JwtTest {

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void jwtTest() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        KeyPair keyPair = KeyUtil.generateKeyPair(AlgoConstant.RSA);

        String base64privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String base64PublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        PrivateKey privateKey = KeyUtil.getPrivateKey(Base64.getDecoder().decode(base64privateKey));
        PublicKey publicKey = KeyUtil.getPublicKey(Base64.getDecoder().decode(base64PublicKey));

        Date currentDate = new Date();
        // 最少一秒，再少了解析不到
        String token = JwtUtil.generateToken("123", currentDate, 1000, privateKey);
        String sub = JwtUtil.validateToken(token, new Date(currentDate.getTime() + 1), publicKey);

        log.info("publicKey: {}", base64PublicKey);
        log.info("privateKey: {}", base64privateKey);
        log.info("jwt: {}", token);
        log.info("sub: {}", sub);
    }
}
