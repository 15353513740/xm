package com.itheima.base.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.attribute.standard.PageRanges;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.base.service.AreaService;
import com.itheima.domain.Area;
import com.itheima.domain.SubArea;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 区域action
 * 
 * @author 徐明明
 * @version 1.0, 2017-10-27 21:16:49
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {

	/** 模型驱动创建区域对象 */
	private Area area = new Area();

	@Override
	public Area getModel() {
		return area;
	}

	// 创建区域业务代理对象
	@Autowired
	private AreaService areaService;

	/** 接收文件 */
	private File file;

	/** 接收文件名称 */
	private String fileFileName;

	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * 文件上传操作
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Action("area_batchImport")
	public void batchImport() throws FileNotFoundException, IOException {

		// 创建一个集合
		ArrayList<Area> array = new ArrayList<Area>();

		// 定义一个Workbook
		Workbook workbook = null;

		// 判断得到的文件是最新版还是旧版本
		if (fileFileName.endsWith(".xls")) {
			// 97-07版
			workbook = new HSSFWorkbook(new FileInputStream(file));
		} else if (fileFileName.endsWith(".xlsx")) {
			// 07版以后
			workbook = new XSSFWorkbook(new FileInputStream(file));
		}

		// 读取一个sheet
		Sheet sheetAt = workbook.getSheetAt(0);

		// 遍历获得每一行
		for (Row row : sheetAt) {

			// 跳过第一行
			if (row.getRowNum() == 0) {
				continue;
			}

			// 跳过空行
			if (row.getCell(0) == null
					|| StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}

			// 创建区域对象
			Area area = new Area();
			// 为区域各属性赋值
			// 区域ID
			area.setId(row.getCell(0).getStringCellValue());
			// 省份
			area.setProvince(row.getCell(1).getStringCellValue());
			// 城市
			area.setCity(row.getCell(2).getStringCellValue());
			// 区域
			area.setDistrict(row.getCell(3).getStringCellValue());
			// 邮编
			area.setPostcode(row.getCell(4).getStringCellValue());

			// 基于pinyin4j生成城市简码和城市编码
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();

			// 进行截取字符串
			province = province.substring(0, province.length() - 1);
			city = city.substring(0, city.length() - 1);
			district = district.substring(0, district.length() - 1);

			// 通过工具类生成简码
			String[] byString = PinYin4jUtils.getHeadByString(province + city
					+ district);

			// 创建字符缓冲对象
			StringBuffer stringBuffer = new StringBuffer();

			// 遍历字符数组
			for (String string : byString) {
				stringBuffer.append(string);
			}

			// 将字符数组转换成字符串
			String shortcode = byString.toString();
			System.out.println("城市简码" + shortcode);
			// 给实体类赋值
			area.setShortcode(shortcode);

			// 通过工具类生成城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			System.out.println("城市编码" + citycode);
			// 给实体类进行赋值
			area.setCitycode(citycode);

			// 将区域对象存入集合中
			array.add(area);
		}

		// 调用业务进行添加
		areaService.saveBatch(array);
	}

	/** 接收页面当页数 */
	private Integer page;

	/** 接收每页显示数 */
	private Integer rows;

	public void setPage(Integer page) {
		this.page = page;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	@Action(value = "find_area", results = { @Result(name = "success", type = "json") })
	public String findAll() {

		List<Area> list = areaService.findAll();

		ValueStack valueStack = ActionContext.getContext().getValueStack();

		valueStack.push(list);
		return SUCCESS;

	}

	/**
	 * 分页条件查询
	 * 
	 * @return
	 */
	@Action(value = "area_page", results = { @Result(name = "success", type = "json") })
	public String findAl() {

		// 创建pageable对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// 创建条件对象
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {

				// 创建集合
				List<Predicate> list = new ArrayList<Predicate>();

				// 进行条件判断和添加
				// 模糊查询省
				if (area.getProvince() != null
						&& StringUtils.isNotBlank(area.getProvince())) {

					Predicate p1 = cb.like(root.get("province")
							.as(String.class), "%" + area.getProvince() + "%");
					list.add(p1);
				}

				// 模糊查询市
				if (area.getCity() != null
						&& StringUtils.isNotBlank(area.getCity())) {

					Predicate p2 = cb.like(root.get("city").as(String.class),
							"%" + area.getCity() + "%");
					list.add(p2);
				}

				// 模糊查询区县
				if (area.getDistrict() != null
						&& StringUtils.isNotBlank(area.getDistrict())) {

					Predicate p3 = cb.like(root.get("district")
							.as(String.class), "%"+area.getDistrict()+"%");
					list.add(p3);
				}
				// 将条件存入条件对象中
				return cb.and(list.toArray(new Predicate[0]));
			}
		};

		// 调用业务层传入分页和条件对象
		Page<Area> page = areaService.findPageData(pageable,
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
	 * 文件导出功能
	 */
	@Action(value="area_export")
	public String export(){
		
		//查询所有区域信息
		List<Area> list = areaService.findAll();
		
		//创建Excel 工作对象
		HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
		
		//创建sheet
		HSSFSheet sheet = hssfWorkbook.createSheet("区域数据");
		
		//创建行
		HSSFRow row = sheet.createRow(0);
		//创建表空间字段
		row.createCell(0).setCellValue("区域编号");
		row.createCell(1).setCellValue("省份");
		row.createCell(2).setCellValue("市");
		row.createCell(3).setCellValue("区县");
		
		//遍历集合创建表信息
		for (int i = 0; i < list.size(); i++) {
			
			//获得区域对象
			Area area2 = list.get(i);
			
			//创建行
			HSSFRow createRow = sheet.createRow(i+1);
			
			//创建行内字段
			createRow.createCell(0).setCellValue(area2.getId());
			createRow.createCell(1).setCellValue(area2.getProvince());
			createRow.createCell(2).setCellValue(area2.getCity());
			createRow.createCell(3).setCellValue(area2.getDistrict());
		}
		
		//创建文件名
		String filename="fq.xsl";
		
		//获得mime对象
		String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
		
		//获得域对象
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//设置两个消息头
		response.setContentType(mimeType);
		response.setHeader("content-disposition", "attachment;filename="+filename);
		
		//设置消息体
		try {
			hssfWorkbook.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
}
