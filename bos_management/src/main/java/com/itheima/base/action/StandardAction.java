package com.itheima.base.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.itheima.base.service.StandardService;
import com.itheima.domain.Standard;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 收派标准action
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-23 23:36:15
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport implements
		ModelDriven<Standard> {

	/** 创建收派标准实体类对象 */
	private Standard standard = new Standard();

	/** 创建业务代理实现类 */
	@Autowired
	private StandardService standardService;

	@Override
	public Standard getModel() {
		return standard;
	}

	// 调用业务实现进行数据存储操作
	@Action(value = "standard_save", results = { 
			@Result(name = "success", type = "redirect", location = "/pages/base/standard.html") })
	public String add() {
		standardService.save(standard);
		return SUCCESS;
	}

	/** 属性驱动接收页面发送的当前页数 */
	private Integer page;
	
	/** 属性驱动接收页面每页显示条数 */
	private Integer rows;
	
	
	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * 收派标准分页查询
	 * @return
	 */
	@Action(value = "standard_pageQuery", results = { 
			@Result(name = "success", type = "json") })
	public String find() {
		
		//创建pageable对象
		Pageable pageable=new PageRequest(page-1,rows);
		
		//调用业务层进行查询操作
		Page<Standard> page=standardService.findPageData(pageable);
		
		//创建MAP集合
		Map<String, Object> map=new HashMap<String, Object>();
		
		//给集合赋值 每页显示集合
		map.put("rows", page.getContent());
		
		//给集合赋值 总记录数
		map.put("total", page.getTotalElements());
		
		//通过struts2-json-plugin 插件将map转化成JSON
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	/**
	 * 查询所有收派表的方法
	 * 
	 * @return
	 */
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		
		//调用业务进行数据查询
		List<Standard> list=standardService.findAll();
		//将查询到的数据通过struts2-json-plugin
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
}
