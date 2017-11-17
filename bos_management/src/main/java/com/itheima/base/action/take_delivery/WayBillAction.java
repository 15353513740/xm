package com.itheima.base.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.itheima.base.service.take_delivery.WayBillService;
import com.itheima.domain.take_delivery.WayBill;
import com.itheima.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 运单action
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月12日
 */
@Scope("prototype")
@Controller
@Namespace("/")
@ParentPackage("json-default")
public class WayBillAction extends BaseAction<WayBill> {

	/** 注入日志对象 */
	@Autowired
	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

	@Autowired
	private WayBillService wayBillService;

	/**
	 * 运单保存
	 * 
	 * @return
	 */
	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String wayBillSave() {
		// 创建集合
		Map<String, Object> map = new HashMap<String, Object>();
		//定义运单对象
		WayBill wayBill=null;
		try {

			// 去除没有ID的订单对象
			if (model.getOrder() != null
					&& (model.getOrder().getId() == null || model.getOrder()
							.getId() == 0)) {
				model.setOrder(null);
			}
			
			if (model.getOrder() != null
					&& (model.getOrder().getId() != null && model.getOrder()
							.getId() != 0)) {
				//查询运单是否已经关联订单
				wayBill=wayBillService.findByOrder(model.getOrder().getId());
				
			}
			
			
			if(wayBill!=null){
				map.put("success", false);
				map.put("msg", "订单号已经被关联请确认");
				// 日志对象打印错误信息
				LOGGER.error("运单保存失败,订单号是:" + model.getWayBillNum());
				
			}else{
				
				// 调用业务层进行保存操作
				wayBillService.save(model);

				// 保存成功
				map.put("success", true);
				map.put("msg", "订单保存成功");

				// 日志记录信息
				LOGGER.info("运单保存成功,运单号是:" + model.getWayBillNum());
			}
			
		} catch (Exception e) {
			e.printStackTrace();

			// 保存订单失败
			map.put("success", false);
			map.put("msg", "运单保存失败");

			// 日志对象打印错误信息
			LOGGER.error("运单保存失败,订单号是:" + model.getWayBillNum());
		}

		// 将集合压入值栈
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}

	/**
	 * 无条件分页查询
	 * 
	 * @return
	 */
	@Action(value = "waybill_pageQuery", results = {
			@Result(name = "success", type = "json") })
	public String pageQuery() {
		// 创建pageable对象 Sort.Order根据ID进行降序排列
		Pageable pageable = new PageRequest(page - 1, rows, new Sort(
				new Sort.Order(Sort.Direction.DESC, "id")));
		// 通过业务层进行查询
		Page<WayBill> pageData = wayBillService.fandAll(model,pageable);

		// 将数据压入值栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	/**
	 * 运单回显
	 * 
	 * @return
	 */
	@Action(value = "waybill_waybillNum", results = { 
			@Result(name = "success", type = "json") })
	public String findWayBillNum() {

		// 根据运单查询运单
		WayBill wayBill = wayBillService
				.findByWayBillNum(model.getWayBillNum());

		// 创建集合
		Map<String, Object> map = new HashMap<String, Object>();

		// 判断运单是否查找到
		if (wayBill == null) {
			map.put("success", false);
		} else {
			
			map.put("success", true);
			map.put("wayBillData", wayBill);
		}
		// 压入值栈
		ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}
}
