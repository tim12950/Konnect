<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
    <!-- THIS IS FOR ALL USERS TO BROWSE LISTINGS -->
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - ${listing.getName()}</title>
</head>

<body>
	
	
	<header class="mainheader">
	<h1><center>Konnect</center></h1>
		<c:if test="${sessionScope.loggedin}">
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
		</c:if>
		
		<c:if test="${sessionScope.loggedin == null}">
		<nav>
			<ul>
				<li><a href="/home">HOME</a></li>
				<li><a href="/upload">UPLOAD</a></li>
				<li><a href="/about">ABOUT</a></li>
				<li><a href="/faq">FAQ</a></li>
				<li><a href="/login">LOGIN</a></li>
			</ul>
		</nav>
		</c:if>
		
	</header>
	<br><br>
	
	<c:if test="${msgerror != null}">
		${msgerror}<br><br>
	</c:if>
	
	<c:if test="${listing.getHasPicture() == 1 }">
		<img src="data:image/png;base64,<c:out value="${listing.getPicture()}"/>"><br><br>
	</c:if>
    title: <c:out value="${listing.getName()}"/><br><br>
    description: <c:out value="${listing.getDescription()}"/><br><br>
    price: <c:out value="${listing.getPrice()}"/><br><br>
    category: <c:out value="${listing.getCategory()}"/><br><br>
    school: <c:out value="${listing.getSchool()}"/><br><br>
    seller: <c:out value="${listing.getSeller()}"/><br><br>
    date posted: <c:out value="${listing.getDate()}"/><br><br>
    
    Contact seller:
    <form action="/messages/new/${listing.getlistingID()}" method="post">
    	<textarea name="newcontactmsg" style = "width:350px;height:200px" required></textarea><br>
    	<button type="submit">Send</button>
    </form>
    <br><br>

</body>
</html>