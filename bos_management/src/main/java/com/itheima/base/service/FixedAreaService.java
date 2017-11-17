package com.itheima.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.domain.FixedArea;


/**
 * 定区业务接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年10月29日
 */
public interface FixedAreaService {

	/**
	 * 定区信息保存
	 * 
	 * @param model
	 */
	void save(FixedArea model);

	/**
	 * 条件分页查询
	 * @param specification
	 * @param pageable
	 * @return
	 */
	Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable);

	/**
	 * 定区关联快递员
	 * @param model
	 * @param courierId
	 * @param takeTimeId
	 */
	void fixedAreaCorrelationCourier(FixedArea model, Integer courierId,
			Integer takeTimeId);

	
}
