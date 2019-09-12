package com.lqh.ssms.controller;

import java.util.Arrays;
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

import com.lqh.ssms.entity.Grade;
import com.lqh.ssms.page.Page;
import com.lqh.ssms.service.GradeService;
import com.lqh.ssms.util.StringUtil;

@RequestMapping("/grade")
@Controller
public class GradeController {

	@Autowired
	public GradeService gradeService;

	/**
	 * 用户列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("grade/grade_list");
		return model;
	}

	/**
	 * 获取年级列表
	 * 
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/get_list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(@RequestParam(value = "name", required = false, defaultValue = "") String name,
			Page page) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("name", "%" + name + "%");
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", gradeService.findList(queryMap));
		ret.put("total", gradeService.getTotal(queryMap));
		return ret;
	}

	/**
	 * 删除年级
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(
			@RequestParam(value = "ids[]", required = true) Long[] ids
		) {
		Map<String, String> ret = new HashMap<String, String>();
		if (ids == null || ids.length == 0) {
			ret.put("type", "error");
			ret.put("msg", "请选择需要删除的数据");
			return ret;
		}
		try {
			if(gradeService.delete(StringUtil.joinString(Arrays.asList(ids), ",")) <= 0) {
				ret.put("type", "error");
				ret.put("msg", "删除失败");
				return ret;
			}
		}catch (Exception e) {
			ret.put("type", "error");
			ret.put("msg", "此处存在班级信息啊，弟弟");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功");
		return ret;
	}

	/**
	 * 修改年级信息
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Grade grade) {
		Map<String, String> ret = new HashMap<String, String>();
		if (grade == null) {
			ret.put("type", "error");
			ret.put("msg", "修改失败");
			return ret;
		}
		if (StringUtils.isEmpty(grade.getName())) {
			ret.put("type", "errer");
			ret.put("msg", "年级信息不得为空");
			return ret;
		}
		Grade existGrade = gradeService.findGradeByGradename(grade.getName());
		if (existGrade != null) {
			if (grade.getId() != existGrade.getId()) {
				ret.put("type", "error");
				ret.put("msg", "年级信息已存在");
				return ret;
			}
		}
		gradeService.edit(grade);
		ret.put("type", "success");
		ret.put("msg", "修改成功");
		return ret;
	}

	/**
	 * 增加新的年级信息
	 * 
	 * @param grade
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Grade grade) {
		Map<String, String> ret = new HashMap<String, String>();
		if (grade == null) {
			ret.put("type", "error");
			ret.put("msg", "添加失败");
			return ret;
		}
		if (StringUtils.isEmpty(grade.getName())) {
			ret.put("type", "errer");
			ret.put("msg", "年级信息不得为空");
			return ret;
		}
		Grade existGrade = gradeService.findGradeByGradename(grade.getName());
		if (existGrade != null) {
			ret.put("type", "error");
			ret.put("msg", "年级信息已存在");
			return ret;
		}
		gradeService.add(grade);
		ret.put("type", "success");
		ret.put("msg", "添加成功");
		return ret;
	}

}
