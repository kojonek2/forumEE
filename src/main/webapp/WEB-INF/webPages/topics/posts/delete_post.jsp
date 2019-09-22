<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html class="h-100">
	<head>
		<meta charset="UTF-8">
		<title>${requestScope.topic.title}</title>
	
		<link rel="icon" href="favicon.ico" type="image/x-icon" />	
		<!-- Bootstrap CSS -->
   		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	
		<style type="text/css">
			.jumbotron {
	    		padding: 2rem 2rem;
			}
		</style>
	</head>
	<body class="h-100">
		<jsp:include page="/WEB-INF/fragments/nav_bar.jspf"/>
	
		<div class="d-flex justify-content-center mt-3">
			<div class="jumbotron d-inline-flex flex-column col-10 col-md-6">
				<jsp:include page="/WEB-INF/fragments/topics/topic_breadcrumb.jspf"/>
				
				
				<c:if test="${empty requestScope.topic.posts}">No posts available</c:if>
				
				<c:forEach var="post" items="${requestScope.topic.posts}">
					<c:set var="post" scope="request" value="${post}"/>
				
					<c:if test="${requestScope.postToDelete.id eq post.id}">
						<jsp:include page="/WEB-INF/fragments/topics/delete_post.jspf"/>
					</c:if>
					<c:if test="${requestScope.postToDelete.id ne post.id}">
						<jsp:include page="/WEB-INF/fragments/topics/normal_post.jspf"/>
					</c:if>
					
				</c:forEach>
				
				<c:if test="${not empty sessionScope.user}">
					<a class="btn btn-success w-100" href="/newPost?topic=${requestScope.topic.id}">new post</a>
				</c:if>
			</div>
		</div>
		
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</body>
</html>
