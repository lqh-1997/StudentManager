package com.lqh.ssms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lqh.ssms.entity.User;

@Service
public interface UserService {
	public User findUserByUsername(String username);
	
	public int add(User user);
	
	public int edit(User user);
	
	public int delete(String ids);
	
	public List<User> findList(Map<String,Object> queryMap);
	
	public int getTotal(Map<String,Object> queryMap);
	
}
