package cn.adelyn.framework.core.util;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;

public class HttpUtil {
    /**
     * 连接超时 sec
     */
    private Integer connectTimeout = 5;
    /**
     * 连接活跃时间，超时不活跃销毁 sec
     */
    private Integer validateAfterInactivity = 10;
    /**
     * 所有路由最大连接数
     */
    private Integer connectMaxTotal = 20;
    /**
     * 单条路由的最大连接数
     */
    private Integer connectMaxPerRoute = 20;
    /**
     * 请求超时时间 sec
     */
    private Integer responseTimeout = 60;
    /**
     * 从链接池获取连接的超时时间 mill
     */
    private Integer connectionRequestTimeout = 300;
    /**
     * 最大重试次数
     */
    private Integer maxRetries = 1;
    /**
     *  重试时间间隔 sec
     */
    private Integer retryInterval = 1;
    /**
     * 是否需要忽略证书链校验
     */
    private Boolean needIgnoreCertChain = false;

    private RestTemplate restTemplate;

    /**
     * no body, no urlParam, no httpHeaders
     */
    public <T> T exchange(String url, HttpMethod httpMethod, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType);
    }

    /**
     * no body, no urlParam, have httpHeaders
     */
    public <T> T exchange(String url, HttpMethod httpMethod, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType);
    }

    /**
     * no body, have urlParam, no httpHeaders
     */
    public <T> T exchange(String url, HttpMethod httpMethod, Class<T> responseType, Map<String, String> urlParams) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType, urlParams);
    }

    /**
     * no body, have urlParam, have httpHeaders
     */
    public <T> T exchange(String url, HttpMethod httpMethod, HttpHeaders headers, Class<T> responseType, Map<String, String> urlParams) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType, urlParams);
    }

    /**
     * have body, no urlParam, no httpHeaders
     */
    public <T, S> T exchange(String url, HttpMethod httpMethod, Class<T> responseType, S body) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType);
    }

    /**
     * have body, no urlParam, have httpHeaders
     */
    public <T, S> T exchange(String url, HttpMethod httpMethod, HttpHeaders headers, Class<T> responseType, S body) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType);
    }

    /**
     * have body, have urlParam, no httpHeaders
     */
    public <T, S> T exchange(String url, HttpMethod httpMethod, Class<T> responseType, Map<String, String> urlParams, S body) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType, urlParams);
    }

    /**
     * have body, have urlParam, have httpHeaders
     */
    public <T, S> T exchange(String url, HttpMethod httpMethod, HttpHeaders headers, Class<T> responseType, Map<String, String> urlParams, S body) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
        return restTemplateExchange(url, httpMethod, requestEntity, responseType, urlParams);
    }

    private <T> T restTemplateExchange(String url, HttpMethod httpMethod, HttpEntity<Object> requestEntity, Class<T> responseType) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, responseType);
        return responseEntity.getBody();
    }

    private <T> T restTemplateExchange(String url, HttpMethod httpMethod, HttpEntity<Object> requestEntity, Class<T> responseType, Map<String, String> urlParams) {
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, requestEntity, responseType, urlParams);
        return responseEntity.getBody();
    }

    private void init() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(getHttpClient());
        restTemplate = new RestTemplate(httpRequestFactory);
    }

    public CloseableHttpClient getHttpClient() {
        PoolingHttpClientConnectionManager connectionManager;

        if (needIgnoreCertChain) {
            // 自定义 SSL 策略,忽略证书链校验
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", createSSLConnSocketFactory())
                .build();
            connectionManager = new PoolingHttpClientConnectionManager(registry);
        } else {
            connectionManager = new PoolingHttpClientConnectionManager();
        }

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(connectTimeout))
            .setValidateAfterInactivity(TimeValue.ofSeconds(validateAfterInactivity))
            .build();

        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setMaxTotal(connectMaxTotal); // 设置连接池大小
        connectionManager.setDefaultMaxPerRoute(connectMaxPerRoute); // 设置每条路由的最大并发连接数

        RequestConfig requestConfig = RequestConfig.custom()
            .setResponseTimeout(Timeout.ofSeconds(responseTimeout))
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectionRequestTimeout))
            .build();

        return HttpClients.custom()
            .setRetryStrategy(new DefaultHttpRequestRetryStrategy(maxRetries, TimeValue.ofSeconds(retryInterval)))
            .setConnectionManager(connectionManager)
            .setConnectionManagerShared(true)
            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
            .setDefaultRequestConfig(requestConfig)
            .build();
    }

    /**
     * 信任所有证书
     */
    private SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslConnectionSocketFactory;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, (s, sslSession) -> true);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return sslConnectionSocketFactory;
    }

    private HttpUtil() {
        // 私有构造函数，防止直接实例化
    }

    public static HttpUtilBuilder builder() {
        return new HttpUtilBuilder();
    }

    public static class HttpUtilBuilder {
        private Integer connectTimeout;
        private Integer validateAfterInactivity;
        private Integer connectMaxTotal;
        private Integer connectMaxPerRoute;
        private Integer responseTimeout;
        private Integer connectionRequestTimeout;
        private Integer maxRetries = 1;
        private Integer retryInterval = 1;
        private Boolean needIgnoreCertChain;

        private HttpUtilBuilder() {
            // 私有构造函数，防止直接实例化
        }

        public HttpUtilBuilder connectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public HttpUtilBuilder validateAfterInactivity(Integer validateAfterInactivity) {
            this.validateAfterInactivity = validateAfterInactivity;
            return this;
        }

        public HttpUtilBuilder connectMaxTotal(Integer connectMaxTotal) {
            this.connectMaxTotal = connectMaxTotal;
            return this;
        }

        public HttpUtilBuilder connectMaxPerRoute(Integer connectMaxPerRoute) {
            this.connectMaxPerRoute = connectMaxPerRoute;
            return this;
        }

        public HttpUtilBuilder responseTimeout(Integer responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        public HttpUtilBuilder connectionRequestTimeout(Integer connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }

        public HttpUtilBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public HttpUtilBuilder retryInterval(Integer retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public HttpUtilBuilder needIgnoreCertChain(Boolean needIgnoreCertChain) {
            this.needIgnoreCertChain = needIgnoreCertChain;
            return this;
        }

        public HttpUtil build() {
            HttpUtil httpUtil = new HttpUtil();
            // 保留默认值
            if (Objects.nonNull(this.connectTimeout)) {
                httpUtil.connectTimeout = this.connectTimeout;
            }
            if (Objects.nonNull(this.validateAfterInactivity)) {
                httpUtil.validateAfterInactivity = this.validateAfterInactivity;
            }
            if (Objects.nonNull(this.connectMaxTotal)) {
                httpUtil.connectMaxTotal = this.connectMaxTotal;
            }
            if (Objects.nonNull(this.connectMaxPerRoute)) {
                httpUtil.connectMaxPerRoute = this.connectMaxPerRoute;
            }
            if (Objects.nonNull(this.responseTimeout)) {
                httpUtil.responseTimeout = this.responseTimeout;
            }
            if (Objects.nonNull(this.connectionRequestTimeout)) {
                httpUtil.connectionRequestTimeout = this.connectionRequestTimeout;
            }
            if (Objects.nonNull(this.maxRetries)) {
                httpUtil.maxRetries = this.maxRetries;
            }
            if (Objects.nonNull(this.retryInterval)) {
                httpUtil.retryInterval = this.retryInterval;
            }
            if (Objects.nonNull(this.needIgnoreCertChain)) {
                httpUtil.needIgnoreCertChain = this.needIgnoreCertChain;
            }

            // 必须要初始化一下
            httpUtil.init();
            return httpUtil;
        }
    }
}
