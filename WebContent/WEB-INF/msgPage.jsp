<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/tutoringservices.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - Messages</title>
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

<c:if test="${buychains != null}">
	Messages regarding listings you're interested in buying:<br>
	<c:forEach items="${buychains}" var="chain">
		<a href="/messages/buy/${chain.getChainID()}">Re: ${chain.getListingName()}; a listing offered by ${chain.getSellerName()}
		<c:if test="${chain.isContainsNewMsg()}"> (new message!)</c:if>
		</a><br>
	</c:forEach>
</c:if>

<br>

<c:if test="${sellchains != null}">
	Messages regarding listings you're selling:<br>
	<c:forEach items="${sellchains}" var="chain">
		<a href="/messages/sell/${chain.getChainID()}">Re: ${chain.getListingName()}; from ${chain.getBuyerName()}
		<c:if test="${chain.isContainsNewMsg()}"> (new message!)</c:if>
		</a><br>
	</c:forEach>
</c:if>

<c:if test="${buychains == null && sellchains == null}">
	You don't have any messages!
</c:if>

</body>
</html>