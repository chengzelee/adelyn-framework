package cn.adelyn.framework.crypto.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class BcConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        Security.addProvider(new BouncyCastleProvider());
    }
}
