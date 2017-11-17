package com.itheima.base.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.base.service.take_delivery.OrderService;
import com.itheima.domain.take_delivery.Order;
import com.itheima.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;

import freemarker.core.ReturnInstruction.Return;

/**
 * 订单action
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月12日
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class OrderAction extends BaseAction<Order> {

	/** 创建订单业务层代理对象 */
	@Autowired
	private OrderService orderService;

	/**
	 * 根据订单号查询订单
	 * 
	 * @return
	 */
	@Action(value = "order_findByOrderNum", results = { @Result(name = "success", type = "json") })
	public String findByOrderNum() {
		
		// 调用业务层进行查询
		Order order = orderService.findByOrderNum(model.getOrderNum());

		// 创建集合
		Map<String, Object> map = new HashMap<String, Object>();

		// 判断是否查询到订单
		if (order == null) {
			map.put("success", false);
		} else {
			map.put("success", true);
			map.put("orderData", order);
		}

		// 将集合压入值栈
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}

}
