<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Query page</title>
	<link href="page/style.css" rel="stylesheet" type="text/css" media="screen" />

  </head>
  
  <body>	
	<center>
	
		<div id="page">
		  <div id="search1" >
			<form name="loginForm" action="page/loginAction.jsp" method="post">
			  <table width="300" border="0" cellspacing="0" cellpadding="0" height="111">
				<tr>
				  <td height="30" align="center"><font color="#000080" style="font-size:20px"><b>School Bus Dispatching</b></font></td>
				</tr>
				<tr>
				  <td align="left"><font style="font-family:'Arial, Helvetica, sans-serif';font-size:14px" > Student Name</font></td>
				</tr>
				<tr align="center">
				  <td align="center"><input type="text" name="username"/></td>
				</tr>
				<tr >
				  <td align="center"><input type="submit" value="search"/>
					<input type="reset" value="reset"/></td>
				</tr>
			  </table>
			  <!--	
				Student Name:<input type="text" name="username"/><br/>
				<!--
				密码:<input type="text" name="password"/><br/>
				-->
			  <!--
			   <input type="submit" value="submit"/>
			   <input type="reset" value="reset"/>
			   -->
			</form>
		  </div>
		</div>
		
	</center>
  </body>
</html>

