<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<div title="欢迎使用" style="padding:20px;overflow:hidden; color:red; " >
	<p style="font-size: 50px; line-height: 60px; height: 60px;">${systemInfo.schoolName}</p>
	<p style="font-size: 25px; line-height: 30px; height: 30px;">欢迎使用学生信息管理系统</p>
  	<p>开发人员：fantasy</p>
  	<p id="currentTime">当前时间：</p>
  	
  	<hr />
  	<h2>系统环境</h2>
  	<p>系统环境：Windows</p>
	<p>开发工具：Eclipse</p>
	<p>Java版本：JDK 1.8</p>
	<p>服务器：tomcat 8.5</p>
	<p>数据库：MySQL 15.1</p>
</div>
<script type="text/javascript">
	var currentTime = document.getElementById("currentTime");
	currentTime.innerHTML += new Date(); 
</script>
</body>
</html>