<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.* "%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询页面</title>
</head>
<body>

<%
	String user_name= request.getParameter("username");

	if(user_name!=null && !user_name.equals("")){

		try{
			Class.forName("org.gjt.mm.mysql.Driver");
			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/school","root","root");
			Statement stmt=(Statement)conn.createStatement();
			String sql="select * from t_student_bus where student='"+user_name+"'";

			ResultSet rs=(ResultSet)stmt.executeQuery(sql);
			if(rs.next())
			{
				String surl=rs.getString("bus")+".jsp";
				response.sendRedirect(surl);
			%>
			<jsp:forward page="main.jsp"/>
			<%                    

			}else out.println("username is wrong");
				out.println("<a href=login.jsp>Return</a>");
			}catch(Exception ee){ee.printStackTrace();}

	}else{
		out.println("Please log in");
		out.println("<a href=login.jsp>Return</a>"); 
	}       
%>


</body>
</html>