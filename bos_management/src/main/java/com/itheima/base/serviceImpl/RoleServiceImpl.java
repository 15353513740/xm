package com.itheima.base.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.RoleDao;
import com.itheima.base.service.take_delivery.RoleService;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;

/**
 * 角色业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public List<Role> findByUser(User user) {
		
		//为admin用户预留所有角色
		if(user.getUsername().equals("admin")){
			return roleDao.findAll();
		}else{
			return roleDao.findByUser(user.getId());
		}
		
	}

}
