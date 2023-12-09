package cn.adelyn.framework.feign.filter;

import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponseEntity;
import cn.adelyn.framework.feign.config.FeignInsideAuthConfig;
import cn.adelyn.framework.feign.util.HttpHandler;
import cn.adelyn.framework.feign.util.IpHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author chengze
 * @date 2022/12/7
 * @desc 授权过滤，子系统可以自己实现AuthConfigAdapter接口
 * 		 也可以在 adelyn.auth.unanth-uri 直接配置不需要授权的路径
 */
@Component
@Slf4j
public class FeignAuthFilter implements Filter {

	@Autowired
	private FeignInsideAuthConfig feignInsideAuthConfig;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (!feignRequestCheck(req)) {
			// 因为是实现接口，没法直接返回统一的响应类
			HttpHandler.printServerResponseToWeb(ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED));
			return;
		}

		try {
			chain.doFilter(req, resp);
		} finally {
		}

	}

	private boolean feignRequestCheck(HttpServletRequest req) {
		// 不是feign请求，不用校验
		if (!req.getRequestURI().contains(FeignInsideAuthConfig.FEIGN_URL_PREFIX)) {
			return true;
		}

		// 校验feign 请求携带的 key 和 secret 是否正确
		String appKey = req.getHeader(FeignInsideAuthConfig.APP_KEY_HEADER);
		String appSecret = req.getHeader(FeignInsideAuthConfig.APP_SECRET_HEADER);
		if (!StringUtils.hasLength(appKey)
				|| !StringUtils.hasLength(appSecret)
				|| !Objects.equals(appSecret, feignInsideAuthConfig.getSecretMap().get(appKey))) {
			return false;
		}

		// ip白名单
		List<String> ipWhiteList = feignInsideAuthConfig.getIpWhiteList();
		// 有ip白名单，且ip不在白名单内，校验失败
		if (!CollectionUtils.isEmpty(ipWhiteList)
				&& !ipWhiteList.contains(IpHelper.getIpAddr())) {
			log.error("ip not in ip White list: {}, ip, {}", ipWhiteList, IpHelper.getIpAddr());
			return false;
		}
		return true;
	}

}
