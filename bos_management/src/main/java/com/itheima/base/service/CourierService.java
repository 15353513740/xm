package com.itheima.base.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.domain.Courier;

/**
 * 快递员业务操作接口
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-26 14:01:32
 */
public interface CourierService {

	/**
	 * 快递员信息保存方法
	 * 
	 * @param courier
	 */
	void save(Courier courier);

	/**
	 * 条件分页查询
	 * @param pageable
	 * @return
	 */
	Page<Courier> findPageData(Specification<Courier> specification,Pageable pageable);

	/**
	 * 进行批量作废操作
	 * @param split
	 */
	void delBatch(String[] split);
	
	/**
	 * 进行批量恢复
	 * @param split
	 */
	void come(String[] split);

	/**
	 * 查询未关联分区快递员
	 * @return
	 */
	List<Courier> findnoassociation();

}
