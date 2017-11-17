package com.itheima.base.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.base.dao.AreaDao;
import com.itheima.base.service.AreaService;
import com.itheima.base.service.AreaServiceImpl;
import com.itheima.base.service.SubAreaService;
import com.itheima.domain.Area;
import com.itheima.domain.SubArea;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 分区管理action
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-28 13:46:00
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class SubAreaAction extends ActionSupport implements
		ModelDriven<SubArea> {

	/** 创建分区业务代理对象 */
	private SubArea subArea = new SubArea();

	@Override
	public SubArea getModel() {
		return subArea;
	}

	/** 注入区域数据操作对象 */
	@Autowired
	private AreaDao areaDao;
	
	/**
	 * 创建分区业务代理对象
	 */
	@Autowired
	private SubAreaService subAreaService;

	/**
	 * 保存操作
	 * @return
	 */
	@Action(value = "sub_area", results = { @Result(name = "success", type = "redirect", location = "/pages/base/sub_area.html") })
	public String save() {

		// 调用业务类进行保存操作
		subAreaService.save(subArea);
		return SUCCESS;
	}

	/** 接收页面发送的当页数 */
	private Integer page;

	/** 接收页面发送的每页显示数 */
	private Integer rows;

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * 分区信息分页查询
	 * 
	 * @return
	 */
	@Action(value = "sub_areaFind", results = { @Result(name = "success", type = "json") })
	public String findAll() {

		// 创建pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// 创建条件对象
		Specification<SubArea> specification = new Specification<SubArea>() {

			// Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			@Override
			public Predicate toPredicate(Root<SubArea> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				// 条件集合
				List<Predicate> list = new ArrayList<Predicate>();
				/**
				 * 快递员工号查询 StringUtils.isNotBlank 判断接收到的数据是否为空
				 * root.get("courierNum") 要加条件查询的属性字段 as(String.class) 属性字段的类型
				 * courier.getCourierNum 接收到的属性
				 */

				// 通过jaon方法获得关联的root JoinType.INNER设置为内连接
				Join<Object, Object> areaRoot = root.join("area",
						JoinType.INNER);

				// 省模糊查询
				if (subArea.getArea() != null
						&& StringUtils.isNotBlank(subArea.getArea()
								.getProvince())) {
					// 设置查询条件
					Predicate p1 = cb.like(
							areaRoot.get("province").as(String.class), "%"
									+ subArea.getArea().getProvince() + "%");
					list.add(p1);
				}

				// 市模糊查询
				if (subArea.getArea() != null
						&& StringUtils.isNotBlank(subArea.getArea().getCity())) {
					// 设置条件
					Predicate p2 = cb.like(areaRoot.get("city")
							.as(String.class), "%"
							+ subArea.getArea().getCity() + "%");
					list.add(p2);
				}

				// 区县模糊查询
				if (subArea.getArea() != null
						&& StringUtils.isNotBlank(subArea.getArea()
								.getDistrict())) {
					// 设置条件
					Predicate p3 = cb.like(
							areaRoot.get("district").as(String.class), "%"
									+ subArea.getArea().getDistrict() + "%");
					list.add(p3);

				}

				// 关键字查询
				if (StringUtils.isNotBlank(subArea.getKeyWords())) {
					// 设置条件
					Predicate p4 = cb.equal(
							root.get("keyWords").as(String.class),
							subArea.getKeyWords());
					list.add(p4);
				}

				// 给条件对象添加条件
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		// 调用业务层传入分页和条件对象
		Page<SubArea> page = subAreaService.findPageData(pageable,
				specification);

		// 创建集合
		Map<String, Object> map = new HashMap<String, Object>();

		// 将得到的page对象总记录数和每页显示集合返回页面
		map.put("rows", page.getContent());
		map.put("total", page.getTotalElements());
		// 将集合压入栈顶
		ServletActionContext.getContext().getValueStack().push(map);
		return SUCCESS;

	}

	
	/**
	 * 普通分页
	 * 
	 * @return
	 */
	@Action(value = "ub_areaFind", results = { @Result(name = "success", type = "json") })
	public String findAll2() {
		// 创建pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// 调用业务层传入分页和条件对象
		Page<SubArea> page = subAreaService.findPageData2(pageable);

		// 创建集合
		Map<String, Object> map = new HashMap<String, Object>();

		// 将得到的page对象总记录数和每页显示集合返回页面
		map.put("rows", page.getContent());
		map.put("total", page.getTotalElements());
		// 将集合压入栈顶
		ServletActionContext.getContext().getValueStack().push(map);
		return SUCCESS;

	}
	
	/** 创建区域业务代理对象 */
	@Autowired
	AreaService areaService;
	
	
	/** 接收文件 */
	private File file;

	/** 接收文件名 */
	private String fileFileName;

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	
	/**
	 * 导入文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Action("subArea_batchImport")
	public void batchImport() throws FileNotFoundException, IOException {

		//创建一个集合
		ArrayList<SubArea> list=new ArrayList<SubArea>();
		
		//定义一个Workbook
		Workbook workbook=null;
		
		//判断接收到的文件格式
		if(fileFileName.endsWith(".xls")){
			//97-07
			workbook=new HSSFWorkbook(new FileInputStream(file));
		}else if(fileFileName.endsWith(".xlsx")){
			//07以后
			workbook=new XSSFWorkbook(new FileInputStream(file));
		}
		//获得sheet
		Sheet sheet = workbook.getSheetAt(0);
		
		//遍历sheet获得每一行
		for (Row row : sheet) {
			
			//跳过第一行
			if(row.getRowNum()==0){
				continue;
			}
			
			//跳过空行 分区编号	定区编码	区域编码	关键字	起始号	结束号	单双号	位置信息

			if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				continue;
			}
		
			//创建分区对象
			SubArea subArea=new SubArea();
			
			//对各个属性进行赋值
			subArea.setId(row.getCell(0).getStringCellValue());
			//省
			String province=row.getCell(1).getStringCellValue();
			//市
			String city=row.getCell(2).getStringCellValue();
			//区县
			String district=row.getCell(3).getStringCellValue();
			Area area=areaDao.findByProvinceAndCityAndDistrict(province, city, district);
			//创建区域业务对象
			subArea.setArea(area);
			subArea.setKeyWords(row.getCell(4).getStringCellValue());
			subArea.setStartNum(row.getCell(5).getStringCellValue());
			subArea.setEndNum(row.getCell(6).getStringCellValue());
			subArea.setSingle(row.getCell(7).getStringCellValue().charAt(0));
			subArea.setAssistKeyWords(row.getCell(8).getStringCellValue());
			
			//将对象存入集合
			list.add(subArea);
		}
		System.out.println(list);
		//调用业务类进行操作
		subAreaService.svaeBatch(list);
	}
	
	

}
