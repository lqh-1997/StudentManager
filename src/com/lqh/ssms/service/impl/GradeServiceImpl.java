package com.lqh.ssms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lqh.ssms.dao.GradeDao;
import com.lqh.ssms.entity.Grade;
import com.lqh.ssms.service.GradeService;

@Service
public class GradeServiceImpl implements GradeService {
	
	@Autowired
	private GradeDao gradeDao;

	@Override
	public Grade findGradeByGradename(String name) {
		return gradeDao.findGradeByGradename(name);
	}

	@Override
	public int add(Grade grade) {
		return gradeDao.add(grade);
	}

	@Override
	public int edit(Grade grade) {
		return gradeDao.edit(grade);
	}

	@Override
	public int delete(String ids) {
		return gradeDao.delete(ids);
	}

	@Override
	public List<Grade> findList(Map<String, Object> queryMap) {
		return gradeDao.findList(queryMap);
	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		return gradeDao.getTotal(queryMap);
	}

	@Override
	public List<Grade> findAll() {
		return gradeDao.findAll();
	}

}
