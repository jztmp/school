<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
String paras = request.getParameter("paras");
String[] paraArray = null;
if (paras == null || (paraArray = paras.split("-")).length < 2){
    response.sendRedirect("../../SpatialNet/index/html/index.html");
}
String userId = paraArray[0];
String powerId = paraArray[1];

%>
</body>
</html>