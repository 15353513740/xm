package com.itheima.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.SubAreaDao;
import com.itheima.domain.Area;
import com.itheima.domain.SubArea;

/**
 * 
 * 分区业务实现类
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-28 14:01:58
 */
@Service
@Transactional
public class SubAreaServiceImpl implements SubAreaService {

	/** 创建数据操作接口 */
	@Autowired
	private SubAreaDao subAreaDao;
	
	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.SubAreaService#save(com.itheima.domain.SubArea)
	 */
	@Override
	public void save(SubArea subArea) {
		subAreaDao.save(subArea);
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.SubAreaService#findPageData(org.springframework.data.domain.Pageable, org.springframework.data.jpa.domain.Specification)
	 */
	@Override
	public Page<SubArea> findPageData(Pageable pageable,
			Specification<SubArea> specification) {
		
		//调用数据操作类进行查询
		return subAreaDao.findAll(specification,pageable);
	}

	
	@Override
	public Page<SubArea> findPageData2(Pageable pageable) {
		return subAreaDao.findAll(pageable);
	}

	
	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.SubAreaService#svaeBatch(java.util.List)
	 */

	@Override
	public void svaeBatch(ArrayList<SubArea> list) {
		subAreaDao.save(list);
		
	}

}
