package com.itheima.base.service.take_delivery;


import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.domain.PageBean;
import com.itheima.domain.take_delivery.Promotion;

/**
 * 活动信息实体业务接口
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月5日
 */

public interface PromotionService {

	//活动信息保存
	void save(Promotion promotion);

	//分页查询活动信息
	Page<Promotion> findAll(Pageable pageable);

	/**
	 * 活动分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	@Path("/findPageData")
	@GET
	@Produces({"application/xml","application/json"})
	PageBean<Promotion> findPageData(
			@QueryParam("page") int page,
			@QueryParam("rows") int rows);
	
	
	/**
	 * 根据活动信息ID查询详情
	 * @param id
	 * @return
	 */
	@Path("/promotion/{id}")
	@GET
	@Produces({"application/xml","application/json"})
	Promotion findById(@PathParam("id") Integer id);

	/**
	 * 修改活动状态
	 * @param date
	 */
	void update(Date date);
	
}
