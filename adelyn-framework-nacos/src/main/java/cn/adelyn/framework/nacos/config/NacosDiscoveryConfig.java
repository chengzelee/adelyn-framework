package cn.adelyn.framework.nacos.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosDiscoveryConfig {

    /**
     * 服务注册到nacos的ip
     */
    @Value("${adelyn.common.nacos.discovery.hostIp}")
    private String nacosDiscoveryHostIp;

    @Bean
    public NacosDiscoveryProperties nacosProperties() {
        //new一个nacos服务发现配置对象
        NacosDiscoveryProperties properties = new NacosDiscoveryProperties();
        //设置发现注册的IP，即注册中心详情中的IP，这里很关键，默认是Inet4Address.getLocalHost(),即如果包含子网，则获取的是子网IP
        properties.setIp(nacosDiscoveryHostIp);
        // 因为里面的 init() 方法被 @PostConstruct 修饰，在注入到容器后会被执行，故不需要配置
        return properties;
    }


}
