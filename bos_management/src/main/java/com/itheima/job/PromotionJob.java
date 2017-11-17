package com.itheima.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.itheima.base.service.take_delivery.PromotionService;

/**
 * 活动定时过期实现
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月8日
 */
public class PromotionJob implements Job {

	/** 获得业务代理对象 */
	@Autowired
	private PromotionService promotionService;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("活动状态修改了");
		promotionService.update(new Date());
		
	}

}
