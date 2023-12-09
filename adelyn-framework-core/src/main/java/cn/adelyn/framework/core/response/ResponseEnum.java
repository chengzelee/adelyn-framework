package cn.adelyn.framework.core.response;

/**
 * @author chengze
 * @date 2022/8/24
 * @desc 返回状态码枚举类
 */
public enum ResponseEnum {

	/**
	 * ok
	 */
	OK("000", "ok"),

	/**
	 * 用于直接显示提示用户的错误，内容由输入内容决定
	 */
	SHOW_FAIL("001", ""),

	/**
	 * 方法参数没有校验，内容由输入内容决定
	 */
	METHOD_ARGUMENT_NOT_VALID("002", ""),

	/**
	 * 无法读取获取请求参数
	 */
	HTTP_MESSAGE_NOT_READABLE("003", "请求参数格式有误"),

	/**
	 * 输出
	 */
	PRINT_MSG_TO_RESPONSE_FAIL("004","输出信息到response失败"),

	/**
	 * 服务器出了点小差
	 */
	EXCEPTION("005", "服务器出了点小差"),

	/**
	 * 未登录
	 */
	UNLOGIN("8004-0", "未登录"),

	/**
	 * 未授权
	 */
	UNAUTHORIZED("8004-1", "未授权"),

	/**
	 * 账号认证失败
	 */
	ACCOUNT_VERIFY_FAIL("8004-2","用户名或密码不正确"),

	/**
	 * 用户已禁用
	 */
	ACCOUNT_DISABLED("8004-3","用户已禁用"),

	/**
	 * token超时
	 */
	TOKEN_TIMEOUT("8002-1","token超时"),

	/**
	 * token解析失败
	 */
	TOKEN_DECRYPT_FAIL("8002-2","token格式不符"),

	/**
	 * 全文检索失败
	 */
	SEARCH_MATCH_FAIL("8004-1","检索失败"),

	/**
	 * 获取博客详情失败
	 */
	SEARCH_GETDETAIL_FAIL("8004-2","获取博客详情失败"),

	/**
	 * 生成uploadUrl失败
	 */
	FILE_UPLOAD_URL_GEN_FAIL("8005-1","生成文件上传url失败"),

	/**
	 * 生成downloadUrl失败
	 */
	FILE_DOWNLOAD_URL_GEN_FAIL("8005-2","生成文件下载url失败"),

	/**
	 * 删除文件失败
	 */
	FILE_OSS_DELETE_FAIL("8005-3","删除oss对象失败"),

	/**
	 * 添加博客失败
	 */
	BLOG_INSERT_FAIL("8041-1","添加博客失败"),

	/**
	 * 更新博客失败
	 */
	BLOG_UPDATE_FAIL("8041-2","更新博客失败"),

	/**
	 * 删除博客失败
	 */
	BLOG_DELETE_FAIL("8041-3","删除博客失败"),

	/**
	 * 博客不存在
	 */
	BLOG_NOT_FOUND("8041-4","博客不存在")
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
