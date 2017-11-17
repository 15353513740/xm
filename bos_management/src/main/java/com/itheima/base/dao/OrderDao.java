package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.domain.take_delivery.Order;

/**
 * 订单实体持久化接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月9日
 */
public interface OrderDao extends JpaRepository<Order, Integer> {

	/**
	 * 根据订单号查询订单
	 * @param orderNum
	 * @return
	 */
	Order findByOrderNum(String orderNum);

}
