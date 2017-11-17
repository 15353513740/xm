package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.itheima.domain.FixedArea;

/**
 * 定区数据操作接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月10日
 */
public interface FixedAreaDao extends JpaRepository<FixedArea, String>,
JpaSpecificationExecutor<FixedArea> {

	
}
