package cn.adelyn.framework.core.response;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author chengze
 * @date 2022/8/24
 * @desc 统一的返回数据
 */
@Slf4j
public class ServerResponseEntity<T> implements Serializable {

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 信息
	 */
	private String msg;

	/**
	 * 数据
	 */
	private T data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return Objects.equals(ResponseEnum.OK.getCode(), this.code);
	}

	@Override
	public String toString() {
		return "ServerResponseEntity{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}

	public static <T> ServerResponseEntity<T> success(T data) {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setData(data);
		serverResponseEntity.setCode(ResponseEnum.OK.getCode());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> success() {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setCode(ResponseEnum.OK.getCode());
		serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
		log.error(responseEnum.toString());
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(responseEnum.getMsg());
		serverResponseEntity.setCode(responseEnum.getCode());
		return serverResponseEntity;
	}

	public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum, T data) {
		log.error(responseEnum.toString());
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(responseEnum.getMsg());
		serverResponseEntity.setCode(responseEnum.getCode());
		serverResponseEntity.setData(data);
		return serverResponseEntity;
	}

	/**
	 * 前端显示失败消息
	 */
	public static <T> ServerResponseEntity<T> showFailMsg(String msg) {
		ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
		serverResponseEntity.setMsg(msg);
		serverResponseEntity.setCode(ResponseEnum.SHOW_FAIL.getCode());
		return serverResponseEntity;
	}

}
