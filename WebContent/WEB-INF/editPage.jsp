<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - Edit</title>
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
	</c:if>  <!-- notify user of invalid edits -->

	<!-- show the listing picture, if any -->
	<c:if test="${listing.getHasPicture() == 1 }">
		<img src="data:image/png;base64,<c:out value="${listing.getPicture()}"/>"><br><br>
	</c:if>
    
    <!-- editing options -->
    <form action="/upload" method="post" enctype="multipart/form-data">
    <input type="hidden" name="listingID" value="${listing.getlistingID()}">
    
    <c:if test="${listing.getHasPicture() == 1}">
    	Edit photo: <input type="file" name="pic"><br><br>
    	Or delete photo: <input type="checkbox" name="deletePic"><br><br>
    	<!-- make delete photo checkbox here -->
    </c:if>
    
    <c:if test="${listing.getHasPicture() == 0}">
    	Add photo: <input type="file" name="pic"><br><br>
    </c:if>
    
	Edit title: <input type="text" name="title" value="${listing.getName()}" style = "width:250px"><br><br>
	Edit price: <br>
	
			<c:choose>
				<c:when test="${listing.getPrice().equals('Free')}">
					<input type="radio" name="price" value="free" checked> Free<br>
  					<input type="radio" name="price" value="uponRequest"> Available Upon Request<br>
  					<input type="radio" name="price" value="specify"> Specify: $
  					<input type="text" name="money"><br><br>
				</c:when>
				
				<c:when test="${listing.getPrice().equals('Available Upon Request')}">
					<input type="radio" name="price" value="free"> Free<br>
  					<input type="radio" name="price" value="uponRequest" checked> Available Upon Request<br>
  					<input type="radio" name="price" value="specify"> Specify: $
  					<input type="text" name="money"><br><br>
				</c:when>
				
				<c:otherwise>
					<input type="radio" name="price" value="free"> Free<br>
  					<input type="radio" name="price" value="uponRequest"> Available Upon Request<br>
  					<input type="radio" name="price" value="specify" checked> Specify: $
  					<input type="text" name="money" value="${listing.getPrice().substring(1)}"><br><br>
				</c:otherwise>
			</c:choose>
	
	Edit description: <br><textarea name="description" style = "width:350px;height:200px">${listing.getDescription()}</textarea><br><br>
	Edit category: <select name="category">
					<option value="${listing.getCategory()}"><c:out value="${listing.getCategory()}"/></option>
					<c:forEach items="${listOfCategories}" var="category">
						<option value="${category}">${category}</option>
					</c:forEach>
					</select>
					
		<button type="submit">Submit</button>
	</form>
	<br><br>
</body>
</html>