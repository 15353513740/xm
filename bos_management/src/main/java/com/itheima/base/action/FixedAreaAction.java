package com.itheima.base.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.PageRanges;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;

import cn.itcast.crm.domain.Customer;

import com.itheima.base.service.FixedAreaService;
import com.itheima.domain.FixedArea;
import com.itheima.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 定区action
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-27 21:16:49
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {

	// 创建定区业务代理对象
	@Autowired
	private FixedAreaService fixedAreaService;

	@Action(value = "fixed_save", results = { @Result(name = "success", type = "redirect", location = "/pages/base/fixed_area.html") })
	public String save() {

		// 调用业务层进行保存
		fixedAreaService.save(model);
		return SUCCESS;
	}

	/**
	 * 定区条件分页查询
	 * 
	 * @return
	 */
	@Action(value = "fixed_area", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {

		// 创建pageable
		Pageable pageable = new PageRequest(page - 1, rows);

		// 创建条件对象
		Specification<FixedArea> specification = new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				// 创建集合
				List<Predicate> list = new ArrayList<Predicate>();

				// 定区编码查询
				if (model.getId() != null
						&& StringUtils.isNotBlank(model.getId())) {
					Predicate p1 = cb.equal(root.get("id").as(String.class),
							model.getId());
					list.add(p1);
				}

				// 所属单位查询
				if (model.getCompany() != null
						&& StringUtils.isNotBlank(model.getCompany())) {

					Predicate p2 = cb.like(
							root.get("company").as(String.class),
							"%" + model.getCompany() + "%");
					list.add(p2);
				}

				// 给条件对象添加条件
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		// 调用业务层进行查询操作
		Page<FixedArea> page = fixedAreaService.findPageData(specification,
				pageable);

		// 调用父类方法进行压栈
		pushPageDataToValueStack(page);
		return SUCCESS;
	}

	/**
	 * 查询未关联用户信息
	 * 
	 * @return
	 */
	@Action(value = "fixedArea_findNoAssociationCustomers", results = { @Result(name = "success", type = "json") })
	public String findNoAssociationCustomers() {

		/**
		 * 使用webClient调用 webService接口 create 设置访问路径 accept 设置返回数据格式
		 * getCollection 获得返回集合
		 */
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9998/services/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		// 将数据压入值栈
		ActionContext.getContext().getValueStack().push(collection);

		return SUCCESS;
	}

	/**
	 * 查询已关联客户信息
	 * 
	 * @return
	 */
	@Action(value = "fixedArea_findHasAssociationFixedAreaCustomers", results = { @Result(name = "success", type = "json") })
	public String findHasAssociationFixedAreaCustomers() {

		/**
		 * 使用webClient调用 webService接口 create 设置访问路径 accept 设置返回数据格式
		 * getCollection 获得返回集合 type 设置传输参数格式
		 */
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:9998/services/customerService/associationfixedareacustomers/"
						+ model.getId()).accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).getCollection(Customer.class);

		// 将数据压入值栈
		ActionContext.getContext().getValueStack().push(collection);

		return SUCCESS;
	}

	/** 属性驱动接收页面出过来的用户ID */
	private String[] customerIds;

	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	/**
	 * 关联客户实现
	 * 
	 * @return
	 */
	@Action(value = "decidedzone_assigncustomerstodecidedzone", results = { @Result(name = "success", type = "redirect", location = "/pages/base/fixed_area.html") })
	public String CustomersToFixedArea() {

		// 通过工具类将数组转换成用逗号分隔的字符串
		String customerId = StringUtils.join(customerIds, ",");

		// 进行关联客户操作
		WebClient.create(
				"http://localhost:9998/services/customerService/associationCustomerToFixedArea"
						+ "?customerId=" + customerId + "&fixedarea="
						+ model.getId()).put(null);
		return SUCCESS;
	}
	
	//属性驱动接收页面传输的 快递员ID 和收派时间ID
	private Integer courierId;
	private Integer takeTimeId;
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	/**
	 * 关联快递员
	 * @return
	 */
	@Action(value="fixedArea_courier",results={
			@Result(name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String fixedAreaCorrelationCourier(){
		
		//调用业务层关联快递员
		fixedAreaService.fixedAreaCorrelationCourier(model,courierId,takeTimeId);
		
		return SUCCESS;
	}

}
