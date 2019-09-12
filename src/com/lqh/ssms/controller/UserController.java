package com.lqh.ssms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lqh.ssms.entity.User;
import com.lqh.ssms.page.Page;
import com.lqh.ssms.service.UserService;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	public UserService userService;

	/**
	 * 用户列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("user/user_list");
		return model;
	}
	
	/**
	 * 获取用户列表
	 * @param username
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/get_list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(value = "username", required = false, defaultValue = "") String username, Page page) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", "%" + username + "%");
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", userService.findList(queryMap));
		ret.put("total", userService.getTotal(queryMap));
		return ret;
	}
	
	/**
	 * 删除用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(
			@RequestParam(value = "ids[]", required = true) Long[] ids
		) {
		Map<String, String> ret = new HashMap<String, String>();
		if(ids == null) {
			ret.put("type", "error");
			ret.put("msg", "请选择需要删除的数据");
			return ret;
		}
		String idString = "";
		for(Long id: ids) {
			idString += id +",";
		}
		idString = idString.substring(0, idString.length()-1);
		if(userService.delete(idString) <= 0) {
			ret.put("type", "error");
			ret.put("msg", "删除失败");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功");
		return ret;
	}
	
	/**
	 * 修改用户名密码
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(User user) {
		Map<String, String> ret = new HashMap<String, String>();
		if (user == null) {
			ret.put("type", "error");
			ret.put("msg", "修改失败");
			return ret;
		}
		if (StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "errer");
			ret.put("msg", "用户名不得为空");
			return ret;
		}
		User existUser = userService.findUserByUsername(user.getUsername());
		if (existUser != null) {
			if(user.getId() != existUser.getId()) {
				ret.put("type", "error");
				ret.put("msg", "用户名已存在");
				return ret;
			}
		}
		userService.edit(user);
		ret.put("type", "success");
		ret.put("msg", "修改成功");
		return ret;
	}
	
	/**
	 * 增加新的用户资料
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user) {
		Map<String, String> ret = new HashMap<String, String>();
		if (user == null) {
			ret.put("type", "error");
			ret.put("msg", "添加失败");
			return ret;
		}
		if (StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "errer");
			ret.put("msg", "用户名不得为空");
			return ret;
		}
		User existUser = userService.findUserByUsername(user.getUsername());
		if (existUser != null) {
			ret.put("type", "error");
			ret.put("msg", "用户名已存在");
			return ret;
		}
		userService.add(user);
		ret.put("type", "success");
		ret.put("msg", "添加成功");
		return ret;
	}
}
