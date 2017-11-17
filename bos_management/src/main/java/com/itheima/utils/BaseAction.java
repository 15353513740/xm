package com.itheima.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * action抽取工具类
 *
 * @author 徐明明
 * @version 1.0，2017年10月29日
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T>  {

	//模型驱动
	protected  T model;
	
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	//构造器完成model实例化
	public BaseAction() {
		
		// 构造子类Action对象 ，获取继承父类型的泛型
		 Type genericSuperclass = this.getClass().getGenericSuperclass();
		 
		// 获取类型第一个泛型参数
		 ParameterizedType paType=(ParameterizedType) genericSuperclass;
		 
		 Class<T> modelClass = (Class<T>) paType.getActualTypeArguments()[0];
					
			try {
				model = modelClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				System.out.println("模型构造失败...");
			}
		 
	}
	
	//接收分页参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	//将查询到的结果压入栈底
	public void pushPageDataToValueStack(Page<T> pageData){
		
		Map<String, Object> map=new HashMap<String , Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(map);
		
	}
	

}
