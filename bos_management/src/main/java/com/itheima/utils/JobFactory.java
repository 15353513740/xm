package com.itheima.utils;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Service;

/**
 * job工厂
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月8日
 */
@Service("jobFactory")
public class JobFactory extends AdaptableJobFactory{
	
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;
	
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle)
			throws Exception {
		Object jobInstance = super.createJobInstance(bundle);
		capableBeanFactory.autowireBean(jobInstance);
		return jobInstance;
	}
}
