package cn.adelyn.framework.feign.config;

import cn.adelyn.framework.core.util.HttpUtil;
import feign.Client;
import feign.hc5.ApacheHttp5Client;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**  
 * @author chengze
 * @date 2022/10/17
 * @desc 注入自定义的httpclient,不采用默认的java.net.HttpURLConnection(每次请求都会建立关闭连接)
 */
@Configuration
public class FeignHttpClientConfig {

    @Bean
    @ConditionalOnProperty(
            value = {"spring.cloud.openfeign.httpclient.hc5.enabled"},
            havingValue = "true",
            matchIfMissing = true
    )
    @ConditionalOnClass(ApacheHttp5Client.class)
    public Client httpClient() {
        CloseableHttpClient closeableHttpClient =  HttpUtil.builder().build().getHttpClient();
        return new ApacheHttp5Client(closeableHttpClient);
    }
}
