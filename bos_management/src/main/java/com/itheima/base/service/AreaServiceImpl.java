package com.itheima.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.itheima.base.dao.AreaDao;
import com.itheima.domain.Area;
import com.itheima.domain.SubArea;

/**
 * 区域业务实现类
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-27 23:16:59
 */
@Service
public class AreaServiceImpl implements AreaService {

	/** 创建区域数据操作接口代理对象 */
	@Autowired
	private AreaDao areaDao;
	
	@Override
	public void saveBatch(ArrayList<Area> array) {
		
		//调用方法进行添加
		areaDao.save(array);
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.AreaService#findAll()
	 */
	@Override
	public List<Area> findAll() {
		
		return areaDao.findAll();
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.AreaService#findPageData(org.springframework.data.domain.Pageable, org.springframework.data.jpa.domain.Specification)
	 */
	@Override
	public Page<Area> findPageData(Pageable pageable,
			Specification<Area> specification) {
		
		return areaDao.findAll(specification,pageable);
	}

	

}
