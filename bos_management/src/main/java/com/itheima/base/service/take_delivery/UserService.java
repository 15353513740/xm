package com.itheima.base.service.take_delivery;

import com.itheima.domain.system.User;

/**
 * 用户认证业务
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
public interface UserService {

	/**
	 * 用户账号查询
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

}
