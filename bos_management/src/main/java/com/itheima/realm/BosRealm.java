package com.itheima.realm;

import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itheima.base.service.take_delivery.PermissionService;
import com.itheima.base.service.take_delivery.RoleService;
import com.itheima.base.service.take_delivery.UserService;
import com.itheima.domain.system.Permission;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;

/**
 * 自定义安全管理器
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月17日
 */
@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {

	/** 获得用户业务实现代理 */
	@Autowired
	private UserService userService;

	/** 获得权限业务代理对象 */
	@Autowired
	private PermissionService permissionService;
	
	/** 获得角色业务代理对象 */
	@Autowired
	private RoleService roleService;
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {

		System.out.println("shiro授权");
		
		SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
		//获得用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		//根据用户查询角色
		List<Role> list=roleService.findByUser(user);
		
		//遍历获得用户所拥有的角色
		for (Role role : list) {
			//添加角色关键字
			simpleAuthorizationInfo.addRole(role.getKeyword());
		}
		
		//根据用户查询权限
		List<Permission> permissions=permissionService.findByUser(user);
		
		//遍历权限
		for (Permission permission : permissions) {
			//添加权限关键字
			simpleAuthorizationInfo.addStringPermission(permission.getKeyword());
		}
		return simpleAuthorizationInfo;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		System.out.println("已经认证");
		//转换token
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		
		//根据用户名进行查询
		User user=userService.findByUsername(usernamePasswordToken.getUsername());
		
		//判断是否查询到用户
		if(user==null){
			//用户不存在返回null
			return null;
		}else{
			//用户存在返回密码
			return new SimpleAuthenticationInfo(user, 
					user.getPassword(),getName());
			
		}
		
	}
}
