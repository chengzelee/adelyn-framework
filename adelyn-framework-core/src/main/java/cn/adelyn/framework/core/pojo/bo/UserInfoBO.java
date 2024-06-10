package cn.adelyn.framework.core.pojo.bo;

import lombok.Data;

/**
 * @author chengze
 * @date 2022/11/11
 * @desc 用户信息上下文
 */
@Data
public class UserInfoBO {

	/**
	 * 租户id
	 */
	private String tenantId;

	/**
	 * userId
	 */
	private Long userId;
}
