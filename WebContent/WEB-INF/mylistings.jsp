<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - My Listings</title>
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
	</c:if> <!-- only msgs should be upload successful, and listing deleted -->

<c:if test="${myListings != null}"> 

	<c:forEach items="${myListings}" var="listing">
		<a href="/mylistings/${listing.getlistingID()}">${listing.getName()}</a><br>
	</c:forEach>
	
</c:if>

<c:if test="${myListings == null}">
	You don't have any listings!
</c:if>

</body>
</html>