package com.itheima.base.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.domain.Standard;

/**
 * 收派标准业务接口
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-23 23:57:07
 */
public interface StandardService {
	
	/**
	 * 收派标准数据保存
	 * @param standard
	 */
	void save(Standard standard);
	
	/**
	 * 收派标准分页查询
	 * 
	 * @param pageable
	 * @return
	 */

	Page<Standard> findPageData(Pageable pageable);

	/**
	 * 查询所有收派标准
	 * 
	 * @return
	 */
	List<Standard> findAll();
			

}
