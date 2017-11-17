package com.itheima.base.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.PermissionDao;
import com.itheima.base.service.take_delivery.PermissionService;
import com.itheima.domain.system.Permission;
import com.itheima.domain.system.User;

/**
 * 权限业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;
	
	@Override
	public List<Permission> findByUser(User user) {
		
		//为admin用户预留所有权限
		if(user.getUsername().equals("admin")){
			return permissionDao.findAll();
		}else{
			return permissionDao.findByUser(user.getId());
		}
	}

}
