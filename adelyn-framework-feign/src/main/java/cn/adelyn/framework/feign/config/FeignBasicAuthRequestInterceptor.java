package cn.adelyn.framework.feign.config;

import cn.adelyn.framework.core.util.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
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

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");


        if (StringUtil.hasLength(authorization)) {
            template.header("Authorization", authorization);
        }
    }
}
