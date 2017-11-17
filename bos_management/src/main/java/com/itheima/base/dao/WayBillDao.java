package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.take_delivery.Order;
import com.itheima.domain.take_delivery.WayBill;

/**
 * 运单数据操作接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月12日
 */
public interface WayBillDao extends JpaRepository<WayBill, Integer> {

	/**
	 * 根据运单号查询运单
	 * @param wayBillNum
	 * @return
	 */
	WayBill findByWayBillNum(String wayBillNum);

	/**
	 * 根据订单ID查询运单
	 * @param order
	 * @return
	 */
	@Query(value="select * from t_way_bill where c_order_id=?",nativeQuery=true)
	WayBill findByOrder(Integer id);

}
