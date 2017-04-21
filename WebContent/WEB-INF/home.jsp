<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!-- the only servlet that calls this page is "home" -->
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - Home</title>
</head>
<body>

<header class="mainheader">
	<h1><center>Konnect</center></h1>
		
		<c:if test="${goodbye}">
			<p>Goodbye!</p>
		</c:if>
		
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
	<h2>Categories</h2>
	
	<span><h3><a href="/listings">All Listings</a></h3></span>
	
	<span><h3><a href="/listings/category/books">Books</a></h3></span>
	
	<span><h3><a href="/listings/category/carpooling">Carpooling</a></h3></span>
	
	<span><h3><a href="listings/category/electronics">Electronics</a></h3></span>
		 
	<span><h3><a href="listings/category/tutoring">Tutoring Services</a></h3></span>
	
	<span><h3><a href="listings/category/other">Other</a></h3></span>



</body>
</html>