package com.itheima.base.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.base.service.TakeTimeService;
import com.itheima.domain.TakeTime;
import com.itheima.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 查询所有收派时间列表
 * 
 *
 * @author 徐明明
 * @version 1.0，2017-11-1 17:17:38
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class TakeTimeAction extends BaseAction<TakeTime> {

	/** 获得收派时间业务代理对象 */
	@Autowired
	private TakeTimeService taketimeService;
	
	@Action(value="taketime_findAll",results={
			@Result(name="success",type="json")})
	public String findAll(){
		
		//调用方法获得所有对象集合
		List<TakeTime> list=taketimeService.findAll();
		
		//将集合压入值栈
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
