<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - FAQ</title>
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

<h3>Why isn't this site served over HTTPS?</h3>
<p>
I need to purchase a domain in order to request a SSL certificate; the free alternative is a self-signed certificate, but, in that case, browsers will tell users that the
connection is insecure (the data is encrypted, but its destination is not authenticated).
</p>

<h3>How many pictures can I upload per listing?</h3>
<p>
For now, at most one.
</p>

<h3>Are messages pushed to my browser?</h3>
<p>
No, you have to refresh the page in order to see whether you have new messages.
</p>

<h3>How do I message a user directly?</h3>
<p>
You can't; you have to do so through a listing being offered by the other user.
</p>

<h3>How do I delete messages?</h3>
<p>
The only way to delete a message is for the listing associated with it to be deleted.
</p>

<h3>Where is the source code for this web app?</h3>
<p>
On my GitHub page: <a href="https://github.com/tim12950">https://github.com/tim12950</a>
</p>

</body>
</html>