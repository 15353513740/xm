package com.itheima.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.domain.Standard;
/**
 * 数据操作接口
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-24 08:08:20
 */
public interface StandardDao extends JpaRepository<Standard, Integer> {

	/**
	 * 条件查询
	 * @param name
	 * @return
	 */
	public List<Standard> findByName(String name);
	
	/**
	 * 模糊条件查询
	 * @param name
	 * @return
	 */
	public List<Standard> findByNameLike(String name);
	
	/**
	 * 命名查询
	 * nativeQuery=true 当参数为true时时SQL 当参数是false时是HQL
	 * @param name
	 * @return
	 */
	//@Query(value="select * from t_standard where c_name=?",nativeQuery=true)
	@Query(value="from Standard where name=?",nativeQuery=false)
	public List<Standard> queryName(String name);
	
	/**
	 * 命名修改操作
	 */
	@Query(value="update t_standard set c_name=? where c_id=?",nativeQuery=true)
	//@Query(value="update Standard set name=? where id=?")
	@Modifying
	public void update(String name,Integer id);
}


