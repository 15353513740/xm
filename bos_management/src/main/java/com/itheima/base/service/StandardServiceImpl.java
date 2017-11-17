package com.itheima.base.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.StandardDao;
import com.itheima.domain.Standard;

/**
 * 收派标准业务实现类
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-23 23:58:47
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	/** 创建数据操作类代理对象 */
	@Autowired
	private StandardDao standardDao;
	
	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.StandardService#save(com.itheima.domain.Standard)
	 */
	@Override
	public void save(Standard standard) {
		
		//调用方法实现数据保存操作
		standardDao.save(standard);
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.StandardService#findPageData(java.awt.print.Pageable)
	 */
	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		
		return standardDao.findAll(pageable);
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.StandardService#findAll()
	 */
	@Override
	public List<Standard> findAll() {
		
		//调用持久层进行查询
		return standardDao.findAll();
	}
	
}
