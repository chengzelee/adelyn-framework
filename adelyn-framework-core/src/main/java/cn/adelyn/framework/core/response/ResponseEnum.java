package cn.adelyn.framework.core.response;

/**
 * @author chengze
 * @date 2022/8/24
 * @desc 返回状态码枚举类
 */
public enum ResponseEnum {

	/**
	 * success
	 */
	SUCCESS("200", "success"),

	/**
	 * 失败
	 */
	FAIL("500", "fail"),

	/**
	 * 方法参数没有校验，内容由输入内容决定
	 */
	METHOD_ARGUMENT_NOT_VALID("002", ""),

	/**
	 * 无法读取获取请求参数
	 */
	HTTP_MESSAGE_NOT_READABLE("003", "请求参数格式有误"),

	/**
	 * 未登录
	 */
	UNLOGIN("401", "未登录"),

	/**
	 * 未授权
	 */
	UNAUTHORIZED("403", "未授权"),

	/**
	 * 流控
	 */
	TOO_MANY_REQUEST("429", "too many request"),
	;

    private final String code;

	private final String msg;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	ResponseEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResponseEnum{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				'}';
	}
}
