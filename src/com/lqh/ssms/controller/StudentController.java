package com.lqh.ssms.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lqh.ssms.entity.Student;
import com.lqh.ssms.page.Page;
import com.lqh.ssms.service.ClazzService;
import com.lqh.ssms.service.StudentService;
import com.lqh.ssms.util.StringUtil;

import net.sf.json.JSONArray;

@RequestMapping("/student")
@Controller
public class StudentController {

	@Autowired
	public StudentService studentService;
	@Autowired
	public ClazzService clazzService;

	/**
	 * 学生列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(ModelAndView model) {
		model.setViewName("student/student_list");
		// model.addObject("gradeList",gradeService.findAll());
		// model.addObject("gradeListJson",JSONArray.fromObject(gradeService.findAll()));
		model.addObject("clazzList", clazzService.findAll());
		model.addObject("clazzListJson", JSONArray.fromObject(clazzService.findAll()));
		return model;
	}

	/**
	 * 获取学生列表
	 * 
	 * @param name
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/get_list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "clazzId", required = false) Long clazzId, 
			HttpServletRequest req,
			Page page) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", "%" + username + "%");
		Object attribute = req.getSession().getAttribute("userType");
		if("2".equals(attribute.toString())) {
			Student loginedStudent = (Student) req.getSession().getAttribute("user");
			queryMap.put("username", "%" + loginedStudent.getUsername() + "%");
		}
		if (clazzId != null) {
			queryMap.put("clazzId", clazzId);
		}
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", studentService.findList(queryMap));
		ret.put("total", studentService.getTotal(queryMap));
		return ret;
	}

	/**
	 * 删除班级
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(@RequestParam(value = "ids[]", required = true) Long[] ids) {
		Map<String, String> ret = new HashMap<String, String>();
		if (ids == null || ids.length == 0) {
			ret.put("type", "error");
			ret.put("msg", "请选择需要删除的数据");
			return ret;
		}
		if (studentService.delete(StringUtil.joinString(Arrays.asList(ids), ",")) <= 0) {
			ret.put("type", "error");
			ret.put("msg", "删除失败");
			return ret;
		}

		ret.put("type", "success");
		ret.put("msg", "删除成功");
		return ret;
	}

	/**
	 * 修改班级信息
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(Student student) {
		Map<String, String> ret = new HashMap<String, String>();
		if (student == null) {
			ret.put("type", "error");
			ret.put("msg", "修改失败");
			return ret;
		}
		if (StringUtils.isEmpty(student.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "学生姓名不得为空");
			return ret;
		}
		if (student.getClazzId() == null) {
			ret.put("type", "error");
			ret.put("msg", "所属班级不得为空");
			return ret;
		}
		if (student.getPassword() == null) {
			ret.put("type", "error");
			ret.put("msg", "登录密码不得为空");
			return ret;
		}
		studentService.edit(student);
		ret.put("type", "success");
		ret.put("msg", "修改成功");
		return ret;
	}

	/**
	 * 增加新的班级信息
	 * 
	 * @param student
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Student student) {
		Map<String, String> ret = new HashMap<String, String>();
		if (StringUtils.isEmpty(student.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "学生信息不得为空");
			return ret;
		}
		if (student.getClazzId() == null) {
			ret.put("type", "error");
			ret.put("msg", "请选择所属班级");
			return ret;
		}
		if (student.getPassword() == null) {
			ret.put("type", "error");
			ret.put("msg", "登录密码不得为空");
			return ret;
		}
		student.setStudentId(StringUtil.generateStudentId("2019", "x"));
		if (studentService.add(student) <= 0) {
			ret.put("type", "error");
			ret.put("msg", "添加失败");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "添加成功");
		return ret;
	}

	@RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> uploadPhoto(MultipartFile photo, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Map<String, String> ret = new HashMap<String, String>();
		if (photo == null) {
			ret.put("type", "error");
			ret.put("msg", "文件呢？？");
			return ret;
		}
		if (photo.getSize() > 10485760) {
			ret.put("type", "error");
			ret.put("msg", "传这么大的图片,你是弱智吗");
			return ret;
		}
		String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1,
				photo.getOriginalFilename().length());
		if (!"jpg,jpeg,png,gif".contains(suffix.toLowerCase())) {
			ret.put("type", "error");
			ret.put("msg", "麻烦你能上传一下正常的文件格式emoji");
			return ret;
		}
		String savePath = req.getServletContext().getRealPath("/") + "\\upload\\";
		File savePathFile = new File(savePath);
		if (!savePathFile.exists()) {
			savePathFile.mkdir();
		}
		String fileName = new Date().getTime() + "." + suffix;
		photo.transferTo(new File(savePath + fileName));
		ret.put("type", "success");
		ret.put("msg", "图片上传成功");
		ret.put("src", req.getServletContext().getContextPath() + "/upload/" + fileName);
		return ret;
	}

}
