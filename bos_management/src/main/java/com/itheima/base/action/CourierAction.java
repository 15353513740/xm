package com.itheima.base.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.dom4j.CDATA;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.base.service.CourierService;
import com.itheima.domain.Courier;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 快递员操作action
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-26 13:55:33
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements
		ModelDriven<Courier> {

	/** 创建快递员实体对象 */
	private Courier courier = new Courier();

	@Override
	public Courier getModel() {
		return courier;
	}

	/** 创建快递员业务操作类代理对象 */
	@Autowired
	private CourierService courierService;

	/**
	 * 快递员信息保存
	 * 
	 * @return
	 */
	@Action(value = "courier_save", results = { 
			@Result(name = "success", type = "redirect", location = "/pages/base/courier.html") })
	public String save() {

		// 调用方法进行保存操作
		courierService.save(courier);
		return SUCCESS;
	}

	
	
	// 接收页面请求发送的数据 当前页和每页显示数
	private Integer rows;
	private Integer page;

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * 快递员信息分页查询
	 */
	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String find() {

		// 创建pageable对象/
		Pageable pageable = new PageRequest(page - 1, rows);

		// 创建条件对象
		Specification<Courier> specification = new Specification<Courier>() {

			// Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			@Override
			public Predicate toPredicate(Root<Courier> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				// 创建存入条件对象的集合
				List<Predicate> list = new ArrayList<Predicate>();

				/**
				 * 快递员工号查询 StringUtils.isNotBlank 判断接收到的数据是否为空
				 * root.get("courierNum") 要加条件查询的属性字段 as(String.class) 属性字段的类型
				 * courier.getCourierNum 接收到的属性
				 */
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.equal(
							root.get("courierNum").as(String.class),
							courier.getCourierNum());
					list.add(p1);
				}
				/**
				 * 快递员单位模糊查询
				 */
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(
							root.get("company").as(String.class),
							"%" + courier.getCompany() + "%");
					list.add(p2);

				}

				/**
				 * 快递员类型查询
				 */
				if (StringUtils.isNotBlank(courier.getType())) {

					Predicate p3 = cb.equal(root.get("type").as(String.class),
							courier.getType());

					list.add(p3);
				}

				// 通过join方法获得关联表的root JoinType.INNER内连接查询
				Join<Object, Object> standardRoot = root.join("standard",
						JoinType.INNER);

				/**
				 * 模糊查询快递员所对应的收派标准名称
				 */
				if (courier.getStandard() != null
						&& StringUtils.isNotBlank(courier.getStandard()
								.getName())) {

					// 设置查询条件
					Predicate p4 = cb.like(
							standardRoot.get("name").as(String.class), "%"
									+ courier.getStandard().getName() + "%");
					list.add(p4);

				}
				// 给条件对象添加条件属性
				return cb.and(list.toArray(new Predicate[0]));
			}

		};

		// 传入pageable对象,查询返回page对象
		Page<Courier> page = courierService.findPageData(specification,
				pageable);

		// 创建map集合
		Map<String, Object> map = new HashMap<String, Object>();

		// 将封装的page对象数据封装到map集合中返回页面 分别是总记录数,每页显示数据集合
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());

		// 将集合存入ValueStack中
		ActionContext.getContext().getValueStack().push(map);

		return SUCCESS;
	}
	
	
	
	
	//接收页面删除ID数据
	private String ids;

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**
	 * 快递员批量作废
	 * @return
	 */
	@Action(value="courier_delBatch",results={
			@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String delete(){
		
		//将接受到的数据进行分割
		String[] split = ids.split(",");
		
		//调用业务类进行修改
		courierService.delBatch(split);
		return SUCCESS;
	}
	
	/**
	 * 快递员批量作废恢复
	 * @return
	 */
	@Action(value="courier_come",results={
			@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String come(){
		
		//将收到的数据进行切割
		String[] split = ids.split(",");
	
		//调用业务层进行操作
		courierService.come(split);
		return SUCCESS;
	}
	
	/**
	 * 查询未关联定区快递员
	 * @return
	 */
	@Action(value="courier_findnoassociation",results={
			@Result(name="success",type="json")})
	public String findnoassociation(){
		
		//调用业务类进行查询
		List<Courier> list=courierService.findnoassociation();
		
		//将查询到的集合压入值栈
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
