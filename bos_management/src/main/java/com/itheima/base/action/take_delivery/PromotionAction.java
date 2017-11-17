package com.itheima.base.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.poi.ss.formula.functions.Value;
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
import org.springframework.stereotype.Controller;

import com.itheima.base.service.take_delivery.PromotionService;
import com.itheima.domain.take_delivery.Promotion;
import com.itheima.utils.BaseAction;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 活的实体action
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月5日
 */
@Scope("prototype")
@Controller
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {

	/** 属性驱动获得文件 */
	private File titleImgFile;
	/** 属性驱动获得文件名 */
	private String titleImgFileFileName;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	// 注入业务操作类代理对象
	@Autowired
	private PromotionService promotionService;

	/**
	 * 宣传活动信息保存
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "/pages/take_delivery/promotion.html") })
	public String save() throws IOException {

		// 获得文件的绝对路径
		String savePath = ServletActionContext.getServletContext().getRealPath(
				"/upload/");

		// 获得文件相对路径
		String saveUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";

		// 生成文件名
		UUID uuid = UUID.randomUUID();
		// 截取文件后缀名
		String ext = titleImgFileFileName.substring(titleImgFileFileName
				.lastIndexOf("."));
		String randomFileName = uuid + ext;

		// 保存文件
		File destFile = new File(savePath + "/" + randomFileName);

		// 将页面要保存的文件拷贝到upload下
		FileUtils.copyFile(titleImgFile, destFile);

		// 将文件工程访问路径保存到数据库
		model.setTitleImg(ServletActionContext.getRequest().getContextPath()
				+ "/upload/" + randomFileName);

		// 调用业务层进行保存
		promotionService.save(model);
		return SUCCESS;
	}

	/**
	 * 活动信息分页展示
	 * 
	 * @return
	 */
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {

		// 创建分页对象
		Pageable pageable = new PageRequest(page - 1, rows);

		// 调用业务类进行查询
		Page<Promotion> pageData = promotionService.findAll(pageable);

		// 调用父类方法将结果压入值栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	
}
