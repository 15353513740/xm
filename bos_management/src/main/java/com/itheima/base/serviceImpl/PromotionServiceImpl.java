package com.itheima.base.serviceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.PromotionDao;
import com.itheima.base.service.take_delivery.PromotionService;
import com.itheima.domain.PageBean;
import com.itheima.domain.take_delivery.Promotion;

/**
 * 活动实体业务实现类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月5日
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionDao promotionDao;
	
	/**
	 * 活动信息保存操作
	 */
	@Override
	public void save(Promotion promotion) {
		
		promotionDao.save(promotion);
	}

	/**
	 * 分页查询活动信息
	 */
	@Override
	public Page<Promotion> findAll(Pageable pageable) {
		
		return promotionDao.findAll(pageable);
	}

	/**
	 * 前台页面远程访问查询
	 */
	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		//创建分页对象
		Pageable pageable=new PageRequest(page-1,rows);
		
		//调用数据操作类进行查询
		Page<Promotion> pageData=promotionDao.findAll(pageable);
		
		//创建pageBean对象
		PageBean<Promotion> pageBean=new PageBean<Promotion>();
		
		//给对象进行赋值
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}

	/**
	 * 通过ID查询活动详情
	 */
	@Override
	public Promotion findById(Integer id) {
		return promotionDao.findOne(id);
	}

	/**
	 * 修改活动状态
	 */
	@Override
	public void update(Date date) {
		promotionDao.update(date);
		
	}

}
