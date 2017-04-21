<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - Upload</title>
</head>
<body>

<header class="mainheader">
	<h1><center>Konnect</center></h1>
	<p>Welcome, ${sessionScope.username}!</p>
		<nav>
			<ul>
				<li><a href="/home">HOME</a></li>
				<li><a href="/upload">UPLOAD</a></li>
				<li><a href="/mylistings">MANAGE LISTINGS</a></li>
				<li><a href="/messages">MESSAGES<c:if test="${sessionScope.newmsg != null}"><c:out value="(You have new messages!)"/></c:if></a></li>
				<li><a href="/about">ABOUT</a></li>
				<li><a href="/faq">FAQ</a></li>
				<li><a href="/logout">LOGOUT</a></li>
			</ul>
		</nav>
	
</header>
<br><br>
<c:if test="${msg != null}">
		${msg}<br><br>
	</c:if>

<form action="/upload" method="post" enctype="multipart/form-data">
	Photo (optional): <input type="file" name="pic"><br><br>
	Title: <input type="text" name="title" required style = "width:250px"><br><br>
	Price: <br><input type="radio" name="price" value="free" checked> Free<br>
  			<input type="radio" name="price" value="uponRequest"> Available Upon Request<br>
  			<input type="radio" name="price" value="specify"> Specify: $
  			<input type="text" name="money"><br><br>
	Description: <br><textarea name="description" style = "width:350px;height:200px" required></textarea><br><br>
	Category: <select name="category">
					<option value="select">Select a category</option>
  					<option value="books">books</option>
  					<option value="electronics">electronics</option>
  					<option value="cars">cars</option>
  					<option value="tutoring">tutoring</option>
  					<option value="other">other</option>
			</select> <br><br>
<button type="submit">Submit</button>
</form>

</body>
</html>