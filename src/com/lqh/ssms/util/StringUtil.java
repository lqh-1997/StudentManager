package com.lqh.ssms.util;

import java.util.Date;
import java.util.List;

public class StringUtil {
	
	public static String joinString(List<Long> list, String split) {
		String ret = "";
		for(Long Long: list) {
			ret += Long + split;
		}
		if(ret.length()>0) {
			ret = ret.substring(0, ret.length() - split.length());		
		}
		return ret;
	}
	
	public static String generateStudentId(String prefix,String suffix) {
		return prefix + new Date().getTime() + suffix;
	}
}
