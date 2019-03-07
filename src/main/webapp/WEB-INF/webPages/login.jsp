<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login page</title>
</head>
<body>
	<form action="j_security_check" method="POST">
		<input type="text" name="j_username" required="required">
		<input type="password" name="j_password" required="required">
		<input type="submit" value="Send">
	</form>
</body>
</html>