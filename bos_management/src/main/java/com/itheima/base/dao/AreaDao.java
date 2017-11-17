package com.itheima.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.itheima.domain.Area;
import com.itheima.domain.Standard;

/**
 * 区域信息数据操作接口
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-28 08:29:36
 */
public interface AreaDao extends JpaRepository<Area, String>,JpaSpecificationExecutor<Area>  {

	/**
	 * 根据ID查询区域对象
	 * @param id
	 * @return
	 */
	public Area findById(String id);

	/**
	 * 根据省市区查询区域对象
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 */
	public Area findByProvinceAndCityAndDistrict(String province, String city,
			String district);
	
}
