package com.itheima.base.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.UserDao;
import com.itheima.base.service.take_delivery.UserService;
import com.itheima.domain.system.User;

/**
 * 用户认证业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	/** 获得用户持久化代理 */
	@Autowired
	private UserDao userDao;
	
	/**
	 * 查询用户
	 */
	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

}
