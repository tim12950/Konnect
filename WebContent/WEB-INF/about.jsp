<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - About</title>
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

<h1>About</h1>
<p style="font-size:150%">
My name is Tim, and if you are here, you were probably brought here by my resume or cover letter, and want to check out something that I built. 
Konnect is a website intended for university students in the lower mainland to buy, sell, or trade goods and services such as textbooks and tutoring services.
Users post their listings, and other users can contact them and negotiate an exchange/purchase; this is similar to craigslist, but with the requirement that
one needs a student email from UBC, SFU, KPU, or BCIT in order to sign up. The intention behind this requirement is to provide additional legitimacy behind each
listing; however, this requirement is not currently enforced in order to allow you to test all the site's features.
<br><br>
This web application was written in Java, and also uses Expression Language (EL) and JSP to dynamically generate user-visible content. I wrote the Java and EL code,
while credit for the CSS and HTML goes to my partners: Navdeep Singh, Rajan Boughan, Kevin Noyes, and Rayzel Linag.
<br><br>
Feel free to play around: try creating an account, logging in, posting/editing/deleting a listing, and messaging (you'll need more than one account for that).
Note that any information you enter will not be transported or stored securely, so when signing up for an account, don't use a password you use for any other 
online service. Please refer to the FAQ page for more information, and the source code is available on my GitHub page: <a href="https://github.com/tim12950">https://github.com/tim12950</a>.
</p>

</body>
</html>