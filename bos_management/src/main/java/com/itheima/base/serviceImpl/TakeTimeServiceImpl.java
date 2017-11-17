package com.itheima.base.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.TakeTimeDao;
import com.itheima.base.service.TakeTimeService;
import com.itheima.domain.TakeTime;

/**
 * 收派时间列表业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月1日
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

	/**  获得收派时间数据操作代理对象 */
	@Autowired
	private TakeTimeDao takeTimeDao;
	
	@Override
	public List<TakeTime> findAll() {
		return takeTimeDao.findAll();
	}

	
}
