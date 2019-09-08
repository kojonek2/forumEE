<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<jsp:forward page="/WEB-INF/webPages/login.jsp">
	<jsp:param value="true" name="wrongCredentials"/>
</jsp:forward>