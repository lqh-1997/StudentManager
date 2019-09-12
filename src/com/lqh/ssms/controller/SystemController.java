package com.lqh.ssms.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lqh.ssms.entity.Student;
import com.lqh.ssms.entity.User;
import com.lqh.ssms.service.StudentService;
import com.lqh.ssms.service.UserService;
import com.lqh.ssms.util.CpachaUtil;

@RequestMapping("/system")
@Controller
public class SystemController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StudentService studentService;
	
	/**
	 * 侧边栏
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView model) {
		model.setViewName("system/admin");
		return model;
	}
	
	/**
	 * 登录界面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView model) {
		model.setViewName("system/login");
		return model;
	}
	
	/**
	 * 登出界面
	 * @param req
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest req) {
		req.getSession().setAttribute("user", null);
		//返回登录界面
		return "redirect:login";
	}
	
	 /**
	  * 登录信息验证
	  * @param username
	  * @param password
	  * @param vcode
	  * @param type
	  * @param req
	  * @return
	  */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> login(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "vcode", required = true) String vcode,
			@RequestParam(value = "type", required = true) int type,
			HttpServletRequest req
			) {
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(username)) {
			ret.put("type", "error");
			ret.put("msg", "用户名不得为空");
			return ret;
		}
		if(StringUtils.isEmpty(password)) {
			ret.put("type", "error");
			ret.put("msg", "密码不得为空");
			return ret;
		}
		if(StringUtils.isEmpty(vcode)) {
			ret.put("type", "error");
			ret.put("msg", "验证码不得为空");
			return ret;
		}
		String capche = (String) req.getSession().getAttribute("loginCpacha");
		if(!capche.toUpperCase().equals(vcode.toUpperCase())) {
			ret.put("type", "error");
			ret.put("msg", "验证码错误");
			return ret;
		}
		req.getSession().setAttribute("loginCpacha",null);
		if(type == 1) {
			User user = userService.findUserByUsername(username);
			if(user == null) {
				ret.put("type", "error");
				ret.put("msg", "用户不存在");
				return ret;
			}
			if(!password.equals(user.getPassword())) {
				ret.put("type", "error");
				ret.put("msg", "密码错误");
				return ret;
			}
			req.getSession().setAttribute("user", user);
		}
		if(type == 2) {
			Student student = studentService.findByUsername(username);
			if(student == null) {
				ret.put("type", "error");
				ret.put("msg", "学生信息不存在");
				return ret;
			}
			if(!password.equals(student.getPassword())) {
				ret.put("type", "error");
				ret.put("msg", "密码错误");
				return ret;
			}
			req.getSession().setAttribute("user", student);
		}
		req.getSession().setAttribute("userType", type);
		ret.put("type", "success");
		ret.put("msg", "登陆成功");
		return ret;	
	}
	
	/**
	 * 获取验证码
	 * @param request
	 * @param px
	 * @param width
	 * @param height
	 * @param resp
	 */
	@RequestMapping(value = "/get_cpacha",method = RequestMethod.GET)
	public void getCpacha(HttpServletRequest request,
			@RequestParam(value = "px", defaultValue = "4", required = false) Integer px,
			@RequestParam(value = "width", defaultValue = "98", required = false) Integer width,
			@RequestParam(value = "height", defaultValue = "33", required = false) Integer height,
			HttpServletResponse resp) {
		CpachaUtil capchaUtil = new CpachaUtil(px, width, height);
		String generatorVCode = capchaUtil.generatorVCode();
		request.getSession().setAttribute("loginCpacha", generatorVCode);
		BufferedImage generatorRotateVCodeImage = capchaUtil.generatorRotateVCodeImage(generatorVCode, false);
		try {
			ImageIO.write(generatorRotateVCodeImage, "gif", resp.getOutputStream());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
}
