package cn.adelyn.framework.core.response;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author chengze
 * @date 2022/8/24
 * @desc 统一的返回数据
 */
public class ServerResponse<T> implements Serializable {

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

	public boolean ifSuccess() {
		return Objects.equals(ResponseEnum.SUCCESS.getCode(), this.code);
	}

	public boolean ifFail() {
		return !Objects.equals(ResponseEnum.SUCCESS.getCode(), this.code);
	}


	public static <T> ServerResponse<T> success(T data) {
		ServerResponse<T> serverResponse = new ServerResponse<>();
		serverResponse.setData(data);
		serverResponse.setMsg(ResponseEnum.SUCCESS.getMsg());
		serverResponse.setCode(ResponseEnum.SUCCESS.getCode());
		return serverResponse;
	}

	public static <T> ServerResponse<T> success() {
		ServerResponse<T> serverResponse = new ServerResponse<>();
		serverResponse.setCode(ResponseEnum.SUCCESS.getCode());
		serverResponse.setMsg(ResponseEnum.SUCCESS.getMsg());
		return serverResponse;
	}

	public static <T> ServerResponse<T> fail(ResponseEnum responseEnum) {
		ServerResponse<T> serverResponse = new ServerResponse<>();
		serverResponse.setMsg(responseEnum.getMsg());
		serverResponse.setCode(responseEnum.getCode());
		return serverResponse;
	}

	public static <T> ServerResponse<T> fail(ResponseEnum responseEnum, T data) {
		ServerResponse<T> serverResponse = new ServerResponse<>();
		serverResponse.setMsg(responseEnum.getMsg());
		serverResponse.setCode(responseEnum.getCode());
		serverResponse.setData(data);
		return serverResponse;
	}

	/**
	 * 前端显示失败消息
	 */
	public static <T> ServerResponse<T> fail(String msg) {
		ServerResponse<T> serverResponse = new ServerResponse<>();
		serverResponse.setMsg(msg);
		serverResponse.setCode(ResponseEnum.FAIL.getCode());
		return serverResponse;
	}

	@Override
	public String toString() {
		return "ServerResponse{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
	}
}
