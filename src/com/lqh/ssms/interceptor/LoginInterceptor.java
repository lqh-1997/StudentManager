package com.lqh.ssms.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lqh.ssms.entity.User;

import net.sf.json.JSONObject;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object arg2) throws Exception {
		//String url = req.getRequestURI(); 
		//System.out.println(url + "进入拦截器");
		Object user =  req.getSession().getAttribute("user");
		if(user == null) {
			//System.out.println("登录失败");
			//用来判断是否为AJAX请求  AJAX存在 x-requested-with:XMLHttpRequest参数
			if("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
				Map<String, String> ret = new HashMap<String, String>();
				ret.put("type", "error");
				ret.put("msg", "登录状态失效，请重新登录");
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
				return false;
			}
			resp.sendRedirect(req.getContextPath() + "/system/login");
			return false;
		}
		return true;
	}
	
}
