package com.lqh.ssms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lqh.ssms.entity.Clazz;

@Service
public interface ClazzService {
	
	public int add(Clazz clazz);
	
	public int edit(Clazz clazz);
	
	public int delete(String ids);
	
	public List<Clazz> findList(Map<String,Object> queryMap);
	
	public int getTotal(Map<String,Object> queryMap);
	
	public List<Clazz> findAll();
}
