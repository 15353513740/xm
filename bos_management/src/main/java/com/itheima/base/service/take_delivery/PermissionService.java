package com.itheima.base.service.take_delivery;

import java.util.List;

import com.itheima.domain.system.Permission;
import com.itheima.domain.system.User;

/**
 * 权限业务借口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
public interface PermissionService {

	
	/**
	 * 根据用户查询权限
	 * @param user
	 * @return
	 */
	List<Permission> findByUser(User user);

}
