package com.itheima.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

/**
 * 自定义拦截器
 * 
 *
 * @author 徐明明
 * @version 1.0，2017年11月6日
 */
public class ExtendStrutsFilter extends StrutsPrepareAndExecuteFilter{ 
    public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {  
        HttpServletRequest request = (HttpServletRequest) req;  
        //不过滤的url,可以自行添加  
        if (request.getRequestURI().contains("/service")) {  
            chain.doFilter(req, res);  
        }else{  
            super.doFilter(request, res, chain); 
        }  
    }  
} 
