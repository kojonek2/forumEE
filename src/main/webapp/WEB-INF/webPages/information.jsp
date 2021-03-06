<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="h-100">
	<head>
		<meta charset="UTF-8">
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<link rel="icon" href="favicon.ico" type="image/x-icon" />
		
		<!-- Bootstrap CSS -->
   		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
		<style type="text/css">
			.jumbotron {
	    		padding: 2rem 2rem;
			}
		</style>
		
	
		<title>Information!</title>
	</head>
	
	<body class="h-100">
		<jsp:include page="/WEB-INF/fragments/nav_bar.jspf"></jsp:include>
	
	
		<div class="container d-flex justify-content-center h-100">
			<div class="d-flex flex-column mt-3" style="min-width:20rem">
				<div class="jumbotron">
					<c:if test="${not empty requestScope.successMessage}">
						<div class="alert alert-success" role="alert">
								${requestScope.successMessage}
						</div>
					</c:if>
					<c:if test="${not empty requestScope.errorMessage}">
						<div class="alert alert-danger" role="alert">
								${requestScope.errorMessage}
						</div>
					</c:if>
			
					<a href="/" class="btn btn-primary w-100">Main Page</a>
				</div>
			</div>
		</div>
		
		
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</body>
</html>