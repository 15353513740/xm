package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itheima.domain.take_delivery.WorkBill;

/**
 * 工单数据操作接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月10日
 */
public interface WorkBillDao extends JpaRepository<WorkBill, Integer> {

}
