package com.itheima.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * freemarker 测试类
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月6日
 */
public class FreemarkerTest {

/*	@Test
	public void test1() throws IOException, TemplateException{
		//配置模板对象
		Configuration configuration=new Configuration(Configuration.VERSION_2_3_22);
		//配置模板路径
		configuration.setDirectoryForTemplateLoading(
				new File("src/main/webapp/WEB-INF/templates"));
		//获取模板对象
		Template template = configuration.getTemplate("hello.ftl");
		
		//创建集合
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("title", "黑马程序员");
		map.put("msg", "java13");
		
		//合并输出
		template.process(map, new PrintWriter(System.out));
	}*/
	
}
