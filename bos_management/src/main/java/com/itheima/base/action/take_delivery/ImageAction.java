package com.itheima.base.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.utils.BaseAction;
import com.opensymphony.xwork2.ActionContext;

/**
 * 图片上传的action
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月4日
 */
@Scope("prototype")
@Controller
@Namespace("/")
@ParentPackage("json-default")
public class ImageAction extends BaseAction<Object> {

	/** 上传的文件 */
	private File imgFile;
	/** 文件名 */
	private String imgFileFileName;
	/** 文件类型 */
	private String imgFileContentType;

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	/**
	 * 图片上传保存
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "image_upload", results = { @Result(name = "success", type = "json") })
	public String upload() throws IOException {

		System.out.println("文件名:" + imgFileFileName);
		System.out.println("文件路径:" + imgFileContentType);

		// 文件存放路径
		String savePath = ServletActionContext.getServletContext().getRealPath(
				"/upload");
		// 文件相对路径
		String saveUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";

		System.out.println("保存相对路径:" + saveUrl);
		// 生成随机文件名
		UUID uuid = UUID.randomUUID();
		// 截取文件后缀名
		String ext = imgFileFileName
				.substring(imgFileFileName.lastIndexOf("."));
		// 拼接文件名
		String randomFileName = uuid + ext;

		// 保存图片
		FileUtils.copyFile(imgFile, new File(savePath + "/" + randomFileName));
		// 通知浏览器文件上传成功
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("error", 0);
		// 返回相对路径
		map.put("url", saveUrl + randomFileName);
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}

	/**
	 * 上传文件预览功能
	 * 
	 * @return
	 */
	@Action(value = "image_manage", results = { @Result(name = "success", type = "json") })
	public String manage() {

		// 获得文件绝对路径
		String rootPath = ServletActionContext.getServletContext().getRealPath(
				"/")
				+ "upload/";

		// 获得文件相对路径
		String rootUrl = ServletActionContext.getRequest().getContextPath()
				+ "/upload/";

		// 创建已上传文件存储集合
		List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();

		// 上传文件目录
		File currentPathFile = new File(rootPath);

		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Map<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String> asList(fileTypes)
							.contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
								.lastModified()));
				// 将遍历的文件对象存入集合中
				fileList.add(hash);
			}
		}

		// 将文件信息压入值栈返回页面
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("moveup_dir_path", "");
		// 设置绝对路径
		map.put("current_dir_path", rootPath);
		// 设置相对路径
		map.put("current_url", rootUrl);
		// 设置文件总数
		map.put("total_count", fileList.size());
		// 传入文件对象集合
		map.put("file_list", fileList);

		// 将map压入值栈
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
}
