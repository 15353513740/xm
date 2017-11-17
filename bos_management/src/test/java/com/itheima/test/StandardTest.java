package com.itheima.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.base.dao.StandardDao;
import com.itheima.domain.Standard;

/**
 * spring data练习
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-24 21:25:31
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="classpath:applicationContext.xml")
public class StandardTest {

	/** 获得数据操作类的代理对象 */
	//@Autowired
	//private StandardDao standardDao;
	
	/**
	 * spring data条件查询
	 */
	/*@Test
	public void test01(){
		List<Standard> list = standardDao.findByName("90-200公斤");
		
		System.out.println(list);
	}
	*/
	@Test
	public void test02(){
		//查询所有
		//System.out.println(standardDao.findAll());
		
		//模糊查询
		//System.out.println(standardDao.findByNameLike("90%"));
		
		//命名查询
		//System.out.println(standardDao.queryName("90-200公斤"));
	}
	
	/**
	 * 修改操作
	 */
	/*@Test
	public void test03(){
		Standard standard=new Standard();
		standard.setId(5);
		standard.setMaxLength(444);
		standard.setMaxWeight(444);
		standard.setMinLength(444);
		standard.setMinWeight(444);
		standard.setName("wewee");
		standardDao.save(standard);
	}
	*/
	/**
	 * 删除操作
	 */
	/*@Test
	public void test04(){
		standardDao.delete(5);
	}*/
	
	/**
	 * 命名修改操作
	 */
	/*@Test
	@Transactional
	@Rollback(false)
	public void test05(){
		standardDao.update("9985公斤", 6);
	}*/
}
