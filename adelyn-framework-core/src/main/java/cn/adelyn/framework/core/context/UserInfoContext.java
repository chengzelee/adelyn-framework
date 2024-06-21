package cn.adelyn.framework.core.context;


import cn.adelyn.framework.core.pojo.bo.UserInfoBO;

import java.util.Objects;

/**
 * @author chengze
 * @date 2022/12/20
 * @desc 保存用户信息上下文
 */
public class UserInfoContext {

	/** The request holder. */
	private static final InheritableThreadLocal<UserInfoBO> USER_INFO_IN_TOKEN_HOLDER = new InheritableThreadLocal<>();

	public static UserInfoBO get() {
		return USER_INFO_IN_TOKEN_HOLDER.get();
	}

	public static void set(UserInfoBO userInfoBO) {
		USER_INFO_IN_TOKEN_HOLDER.set(userInfoBO);
	}

	public static void clean() {
		USER_INFO_IN_TOKEN_HOLDER.remove();
	}

	public static String getTenantId() {
		return Objects.isNull(get()) ? null : get().getTenantId();
	}

	public static Long getUserId() {
		return Objects.isNull(get()) ? null : get().getUserId();
	}


}
