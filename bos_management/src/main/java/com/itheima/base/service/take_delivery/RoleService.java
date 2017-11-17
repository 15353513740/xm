package com.itheima.base.service.take_delivery;

import java.util.List;

import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;

/**
 * 角色业务借口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
public interface RoleService {

	/**
	 * 根据用户查询角色
	 * @param user
	 * @return
	 */
	List<Role> findByUser(User user);

}
