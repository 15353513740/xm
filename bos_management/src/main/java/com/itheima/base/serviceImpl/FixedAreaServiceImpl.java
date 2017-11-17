package com.itheima.base.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.CourierDao;
import com.itheima.base.dao.FixedAreaDao;
import com.itheima.base.dao.TakeTimeDao;
import com.itheima.base.service.FixedAreaService;
import com.itheima.domain.Courier;
import com.itheima.domain.FixedArea;
import com.itheima.domain.TakeTime;

/**
 * 定区业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年10月29日
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	/** 创建定区数据操作代理对象 */
	@Autowired
	private FixedAreaDao fixedAreaDao;
	
	
	/**
	 * 定区信息保存
	 */
	@Override
	public void save(FixedArea model) {
		fixedAreaDao.save(model);
		
	}


	/**
	 * 条件分页查询
	 */
	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable) {

		//调用数据操作层进行查询操作
		return fixedAreaDao.findAll(specification,pageable);
	}

	
	//创建快递员和收派时间数据操作代理对象
	@Autowired
	private CourierDao courierDao;
	@Autowired
	private TakeTimeDao takeTimeDao;
	
	/**
	 * 关联快递员到定区
	 */
	@Override
	public void fixedAreaCorrelationCourier(FixedArea model, Integer courierId,
			Integer takeTimeId) {
		//分别查询到三个对象的持久化对象
		FixedArea fixedArea = fixedAreaDao.findOne(model.getId());
		Courier courier = courierDao.findOne(courierId);
		TakeTime takeTime = takeTimeDao.findOne(takeTimeId);
		
		//通过快照自动更新数据库的特性进行添加
		//将收派时间添加到快递员上
		courier.setTakeTime(takeTime);
		
		//将快递员关联到定区
		fixedArea.getCouriers().add(courier);
		
	}

}
