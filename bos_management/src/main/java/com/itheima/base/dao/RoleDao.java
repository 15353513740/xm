package com.itheima.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.itheima.domain.system.Role;

/**
 * 角色持久化接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
public interface RoleDao extends JpaRepository<Role, Integer> {

	/**
	 * 根据用户查询角色
	 * @param user
	 * @return
	 */
	@Query("from Role r inner join fetch r.users u where u.id=? ")
	List<Role> findByUser(Integer id);

}
