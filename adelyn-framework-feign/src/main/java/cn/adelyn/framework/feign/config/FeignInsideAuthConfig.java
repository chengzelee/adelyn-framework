package cn.adelyn.framework.feign.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**  
 * @author chengze
 * @date 2022/10/17
 * @desc 
 */
@RefreshScope
@Configuration
public class FeignInsideAuthConfig {

    /**
     * feign请求前缀
     */
    public static final String FEIGN_URL_PREFIX = "/feign";

    public static final String APP_KEY_HEADER = "appKey";

    public static final String APP_SECRET_HEADER = "appSecret";

    @Value("#{${adelyn.feign.inside.secretMap}}")
    private Map<String, String> secretMap;

    @Value("${adelyn.feign.inside.appKey}")
    private String key;

    /**
     * ip 白名单
     * 先判断是否为空，是空返回mull，避免直接 split 返回有一个空字符串元素的 list
     */
    @Value("#{'${adelyn.feign.inside.ipWhiteList:}'.empty ? null : '${adelyn.feign.inside.ipWhiteList:}'.split(',')}")
    private List<String> ipWhiteList;

    public Map<String, String> getSecretMap() {
        return secretMap;
    }

    public String getKey() {
        return key;
    }

    public List<String> getIpWhiteList() {
        return ipWhiteList;
    }
}
