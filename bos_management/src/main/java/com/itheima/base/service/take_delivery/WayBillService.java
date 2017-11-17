package com.itheima.base.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.domain.take_delivery.Order;
import com.itheima.domain.take_delivery.WayBill;

/**
 * 运单业务接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月12日
 */
public interface WayBillService {

	/**
	 * 保存运单操作
	 * @param model
	 */
	void save(WayBill model);

	/**
	 * 简单分页查询
	 * @param wayBill 
	 * @param pageable
	 * @return
	 */
	Page<WayBill> fandAll(WayBill wayBill, Pageable pageable);

	/**
	 * 根据运单号进行查询
	 * @param wayBillNum
	 * @return
	 */
	WayBill findByWayBillNum(String wayBillNum);

	/**
	 * 根据订单ID查询运单
	 * @param order
	 * @return
	 */
	WayBill findByOrder(Integer id);


}
