package com.lqh.ssms.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lqh.ssms.entity.Student;

@Repository
public interface StudentDao {
	public Student findByUsername(String username);
	
	public int add(Student student);

	public int edit(Student student);

	public int delete(String ids);

	public List<Student> findList(Map<String, Object> queryMap);

	public int getTotal(Map<String, Object> queryMap);
	
	public List<Student> findAll();
}
