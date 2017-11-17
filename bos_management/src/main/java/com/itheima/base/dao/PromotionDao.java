package com.itheima.base.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.take_delivery.Promotion;

/**
 * 获得实体业务数据操作接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月5日
 */
public interface PromotionDao extends JpaRepository<Promotion,Integer > {

	//修改活动状态
	@Query("update Promotion set status='2' where endDate<? and status='1'")
	@Modifying
	void update(Date date);

}
