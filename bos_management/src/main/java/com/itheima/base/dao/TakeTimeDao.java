package com.itheima.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itheima.domain.TakeTime;

/**
 * 收派时间列表数据操作接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月1日
 */
public interface TakeTimeDao extends JpaRepository<TakeTime, Integer> {

}
