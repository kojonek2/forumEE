<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html class="h-100">
	<head>
		<link rel="icon" href="favicon.ico" type="image/x-icon" />	
		<!-- Bootstrap CSS -->
   		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
	
		<meta charset="UTF-8">
		<title>Delete topic ${requestScope.topic.title}</title>
		
		<style type="text/css">
			.jumbotron {
	    		padding: 2rem 2rem;
			}
		</style>
	</head>
	<body class="h-100">
		<jsp:include page="/WEB-INF/fragments/nav_bar.jspf"/>
	
		<div class="d-flex justify-content-center mt-3">
			<div class="jumbotron d-inline-flex flex-column">
			
				<form action="/deleteTopic?id=${requestScope.topic.id}" method="post">
					<h1>Are you sure that you want to delete topic ${requestScope.topic.title}?</h1>
					
					<button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
					<button type="submit" name="action" value="cancel" class="btn btn-success">Cancel</button>
				</form>
				
			</div>
		</div>
		
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
	    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</body>
</html>