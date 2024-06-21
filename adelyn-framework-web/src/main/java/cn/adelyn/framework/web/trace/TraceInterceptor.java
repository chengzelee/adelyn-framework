package cn.adelyn.framework.web.trace;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ThreadLocalRandom;

public class TraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceIdFromHeader = request.getHeader("X-traceId");
        String traceId = StringUtils.hasLength(traceIdFromHeader) ? traceIdFromHeader : getRandomLongString();
        MDC.put("traceId", traceId);
        response.addHeader("X-traceId",traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove("traceId");
    }

    public static String getRandomLongString() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(0L, Long.MAX_VALUE));
    }

}
