<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="h-100">
	<head>
		<meta charset="UTF-8">
		<title>ForumEE</title>
	
		<link rel="icon" href="favicon.ico" type="image/x-icon" />	
		<!-- Bootstrap CSS -->
   		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	
		<style type="text/css">
			.jumbotron {
	    		padding: 2rem 2rem;
			}
			.before {
				position: relative;
			 	z-index: 2;
			}
		</style>
	</head>
	<body class="h-100">
		<jsp:include page="/WEB-INF/fragments/nav_bar.jspf"/>
	
		<div class="d-flex justify-content-center mt-3">
		
			<div class="jumbotron d-inline-flex flex-column col-10 col-md-6">
				<div class="d-flex  justify-content-between align-items-center">
					<h1>Sections</h1>
					<c:if test="${sessionScope.user.admin}">
						<a class="btn btn-success" href="newSection">new</a>
					</c:if>
				</div>
				
				
				<c:if test="${ empty requestScope.sections}">No sections available</c:if>
				
				<c:forEach var="section" items="${requestScope.sections}">
					<div class="card">
						<div class="card-body">
							<h5 class="card-title"> ${section.name} </h5> 
							<h6 class="card-subtitle mb-2 text-muted"> ${section.description}</h6>
						
							<a class="stretched-link" href="/section?id=${section.id}"></a>
							<c:if test="${sessionScope.user.admin}">
								<a class="btn btn-danger before" href="deleteSection?id=${section.id}">delete</a>
								<a class="btn btn-warning before" href="editSection?id=${section.id}">edit</a>
							</c:if>
						</div>
					</div>
				</c:forEach>
				
			</div>
		</div>
		
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</body>
	</html>
