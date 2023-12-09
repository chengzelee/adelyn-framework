package cn.adelyn.framework.feign.config;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**  
 * @author chengze
 * @date 2022/10/17
 * @desc 
 */
@Component
@ConditionalOnClass({RequestInterceptor.class})
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    @Autowired
    private FeignInsideAuthConfig feignInsideAuthConfig;

    @Override
    public void apply(RequestTemplate template) {
        String appKey = feignInsideAuthConfig.getKey();
        // feign的内部请求，往请求头放入key 和 secret进行校验
        template.header(FeignInsideAuthConfig.APP_KEY_HEADER, appKey);
        template.header(FeignInsideAuthConfig.APP_SECRET_HEADER, feignInsideAuthConfig.getSecretMap().get(appKey));

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");


        if (StrUtil.isNotBlank(authorization)) {
            template.header("Authorization", authorization);
        }
    }
}
