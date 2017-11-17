package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.domain.system.User;

/**
 * 用户登录数据操作类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
public interface UserDao extends JpaRepository<User, Integer> {

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

}
