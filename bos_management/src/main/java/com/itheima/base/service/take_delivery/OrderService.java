package com.itheima.base.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.itheima.domain.take_delivery.Order;

/**
 * 订单信息业务接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月9日
 */
public interface OrderService {

	/**
	 * 保存订单信息
	 * @param order
	 */
	@Path("/order_save")
	@POST
	@Consumes({"application/xml","application/json"})
	public void  save(Order order);

	/**
	 * 根据订单ID查询订单
	 * @param orderNum
	 * @return
	 */
	public Order findByOrderNum(String orderNum);
	
			 
}
