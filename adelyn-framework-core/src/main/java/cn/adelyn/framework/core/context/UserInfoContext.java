package cn.adelyn.framework.core.context;


import cn.adelyn.framework.core.pojo.bo.UserInfoBO;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 保存用户信息上下文
 */
public class UserInfoContext {

	/** The request holder. */
	private static final ThreadLocal<UserInfoBO> USER_INFO_IN_TOKEN_HOLDER = new ThreadLocal<>();

	public static UserInfoBO get() {
		return USER_INFO_IN_TOKEN_HOLDER.get();
	}

	public static void set(UserInfoBO userInfoBO) {
		USER_INFO_IN_TOKEN_HOLDER.set(userInfoBO);
	}

	public static void clean() {
		if (USER_INFO_IN_TOKEN_HOLDER.get() != null) {
			USER_INFO_IN_TOKEN_HOLDER.remove();
		}
	}

	public static String getTenantId() {
		return get().getTenantId();
	}

	public static Long getUserId() {
		return get().getUserId();
	}

	public static Integer getUserType() {
		return get().getUserType();
	}


}
