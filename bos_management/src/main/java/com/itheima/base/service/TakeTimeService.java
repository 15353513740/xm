package com.itheima.base.service;

import java.util.List;
import com.itheima.domain.TakeTime;

/**
 * 收派时间业务接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月1日
 */

public interface TakeTimeService {

	/**
	 * 查询所有收派时间对象
	 * @return
	 */
	List<TakeTime> findAll();

}
