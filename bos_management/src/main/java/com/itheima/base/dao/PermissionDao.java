package com.itheima.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.system.Permission;

/**
 * 权限持久类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
public interface PermissionDao extends JpaRepository<Permission, Integer>  {

	/**
	 * 根据用户查询权限
	 * @param id
	 * @return
	 */
	@Query("from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id=? ")
	List<Permission> findByUser(int id);

}
