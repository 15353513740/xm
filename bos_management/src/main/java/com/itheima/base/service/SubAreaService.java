package com.itheima.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.itheima.domain.SubArea;

/**
 * 分区业务接口
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-28 13:56:18
 */
public interface SubAreaService {

	/**
	 * 分区信息保存
	 * 
	 * @param subArea
	 */
	void save(SubArea subArea);

	/**
	 * 分区分页显示及查询
	 * 
	 * @param pageable
	 * @param specification
	 * @return
	 */
	Page<SubArea> findPageData(Pageable pageable,
			Specification<SubArea> specification);
	
	/**
	 * 简单分页操作
	 * @param pageable
	 * @return
	 */
	Page<SubArea> findPageData2(Pageable pageable);

	/**
	 * 进行文件上传
	 * @param subareas
	 */
	void svaeBatch(ArrayList<SubArea> list);

}
