<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="h-100">
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<!-- Bootstrap CSS -->
   		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<style type="text/css">
			.jumbotron {
	    		padding: 2rem 2rem;
			}
		</style>
		<title>Login page</title>
	</head>
	<body class="h-100">
		
		<div class="container d-flex justify-content-center h-100">
			<div class="d-flex flex-column justify-content-center col-lg-4">
				<form class="jumbotron" action="j_security_check" method="POST">
				
					<c:if test="${not empty requestScope.sucessMessage}">
						<div class="alert alert-success" role="alert">
  							${requestScope.sucessMessage}
						</div>
					</c:if>
					
					<div class="w-100 d-flex justify-content-center">
						<h1>Log In!</h1>
					</div>
					<c:if test="${param.wrongCredentials}">
						<div class="alert alert-danger" role="alert">
  							Login failed! Wrong credentials!
						</div>
					</c:if>
					<div class="form-group">
						<label for="username">Username</label>
						<input class="form-control" type="text" name="j_username" required="required">
					</div>
					<div class="form-group">
						<label for="password">Password</label>
						<input class="form-control" type="password" name="j_password" required="required">
					</div>
					<input type="submit" value="Log In" class="btn btn-primary w-100">
					<a href="/register" class="btn btn-link w-100">register</a>
				</form>
			</div>
		</div>
		
		
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</body>
</html>