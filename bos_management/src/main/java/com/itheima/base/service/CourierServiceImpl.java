package com.itheima.base.service;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.CourierDao;
import com.itheima.domain.Courier;

/**
 * 快递员业务实现类
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-26 14:02:54
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	/** 注入数据操作类代理对象 */
	@Autowired
	private CourierDao courierDao;
	
	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.CourierService#save(com.itheima.domain.Courier)
	 */
	@Override
	public void save(Courier courier) {
		courierDao.save(courier);
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.CourierService#findPageData(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
		
		return courierDao.findAll(specification,pageable);
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.CourierService#delete(java.lang.String[])
	 */
	@Override
	public void delBatch(String[] split) {
		
		//遍历数组转化成integer类型
		for (int i = 0; i < split.length; i++) {
			Integer id=Integer.parseInt(split[i]);
			
			//调用数据操作层进行操作
			courierDao.delBatch(id);
		}
		
	}

	/**
	 * (non-Javadoc)
	 * @see com.itheima.base.service.CourierService#come(java.lang.String[])
	 */
	@Override
	public void come(String[] split) {
		
		//遍历数组并转化成Inte类型
		for (int i = 0; i < split.length; i++) {
			Integer id=Integer.parseInt(split[i]);
			
			//调用数组操作层进行修改
			courierDao.come(id);
		}
		
	}

	/**
	 * 查询未关联分区快递员
	 */
	@Override
	public List<Courier> findnoassociation() {
		
		//创建条件对象
		Specification<Courier> specification=new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				//判断列表集合长度为0
				Predicate p=cb.isEmpty(root.get("fixedAreas").as(Set.class));
				
				return p;
			}
		};
		return courierDao.findAll(specification);
	}
	

}
