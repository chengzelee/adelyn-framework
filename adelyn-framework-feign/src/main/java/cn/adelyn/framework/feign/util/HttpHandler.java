package cn.adelyn.framework.feign.util;

import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponseEntity;
import cn.adelyn.framework.core.util.Json;
import cn.hutool.core.util.CharsetUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 输出错误信息
 */
@Slf4j
public class HttpHandler {

	public static <T> void printServerResponseToWeb(ServerResponseEntity<T> serverResponseEntity) {
		if (serverResponseEntity == null) {
			log.info("print obj is null");
			return;
		}

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes == null) {
			log.error("requestAttributes is null, can not print to web");
			return;
		}
		HttpServletResponse response = requestAttributes.getResponse();
		if (response == null) {
			log.error("httpServletResponse is null, can not print to web");
			return;
		}
		log.error("response error: " + serverResponseEntity.getMsg());
		response.setCharacterEncoding(CharsetUtil.UTF_8);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter printWriter;
		try {
			printWriter = response.getWriter();
			printWriter.write(Json.toJsonString(serverResponseEntity));
		} catch (IOException e) {
			throw new AdelynException(ResponseEnum.PRINT_MSG_TO_RESPONSE_FAIL, e);
		}
	}

}
