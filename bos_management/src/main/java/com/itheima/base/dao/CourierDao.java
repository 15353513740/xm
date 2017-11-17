package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.Courier;

/**
 * 快递员数据操作接口
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-26 14:05:20
 * JpaRepository 普通操作
 * JpaPersistentEntity 条件查询
 */

public interface CourierDao extends JpaRepository<Courier, Integer>, 
JpaSpecificationExecutor<Courier> {
	
	/**
	 * 批量作废
	 * @param id
	 */
	@Query("update Courier set deltag='1' where id=?")
	@Modifying
	public void delBatch(Integer id);

	/**
	 * 批量恢复
	 * @param id
	 */
	@Query("update Courier set deltag=null where id=?")
	@Modifying
	public void come(Integer id);
}
