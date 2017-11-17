package com.itheima.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.domain.Area;
import com.itheima.domain.SubArea;

/**
 * 区域业务接口
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-27 23:15:33
 */
public interface AreaService {

	/**
	 * 添加区域信息业务
	 * @param array
	 */
	void saveBatch(ArrayList<Area> array);

	/**
	 * 查询所有区域
	 * @return
	 */
	List<Area> findAll();

	/**
	 * 条件分页查询
	 * @param pageable
	 * @param specification
	 * @return
	 */
	Page<Area> findPageData(Pageable pageable,
			Specification<Area> specification);

	

}
