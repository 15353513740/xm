package com.itheima.base.action.take_delivery;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.domain.system.User;
import com.itheima.utils.BaseAction;

/**
 * 用户登录action
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月16日
 */
@Scope("prototype")
@Controller
@Namespace("/")
@ParentPackage("json-default")
public class UserAction extends BaseAction<User> {

	@Action(value = "user_login", results = {
			@Result(name = "login", type = "redirect", location = "/login.html"),
			@Result(name = "success", type = "redirect", location = "index.html") })
	public String userLogin() {

		// 基于shiro实现登录
		Subject subject = SecurityUtils.getSubject();

		//封装用户名和密码
		AuthenticationToken token = new UsernamePasswordToken(
				model.getUsername(), model.getPassword());
		
		try {
			//登录成功
			subject.login(token);
			return SUCCESS;
		} catch (AuthenticationException e) {
			//打印异常信息
			e.printStackTrace();
			return LOGIN;
		}
		
	}

}
