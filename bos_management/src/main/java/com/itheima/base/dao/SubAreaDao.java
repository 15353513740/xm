package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.itheima.domain.SubArea;


/**
 * 分区数据操作接口
 * 
 * @author 徐明明
 * @version 1.0,2017-10-28 14:03:08
 */
public interface SubAreaDao extends JpaRepository<SubArea, String>,
JpaSpecificationExecutor<SubArea> {

}
