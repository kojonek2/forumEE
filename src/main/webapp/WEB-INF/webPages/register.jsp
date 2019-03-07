<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register Page</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/register" method="POST">
		<input type="text" name="username" required="required"> 
		<input type="password" name="password" required="required">
		<input type="submit" value="Register">
	</form>
</body>
</html>