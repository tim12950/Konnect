<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Konnect - Verify</title>

<!-- Google Fonts -->
	<link href='https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700|Lato:400,100,300,700,900' rel='stylesheet' type='text/css'>

	<link rel="stylesheet" href="/css/animate.css">
	
	<!-- Custom Stylesheet -->
	<link rel="stylesheet" href="/css/style.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
	
	<!--- New --->
	<link href="/css/clean-blog.min.css" rel="stylesheet">
	<link href="/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

 <!-- Navigation -->
	
    <nav class="navbar navbar-default navbar-custom navbar-fixed-top">
        <div class="container-fluid">
		
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    Menu <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand" href="/home">Konnect.</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="/home">Home</a>
                    </li>
					<li>
                        <a href="/login">Login</a>
                    </li>
                    <li>
                        <a href="/register">Register</a>
                    </li>
                    <li><a href="/about">ABOUT</a></li>
					<li><a href="/faq">FAQ</a></li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->

        </div>
        <!-- /.container -->
		
    </nav>
    
    <div class="box">
		<div class="up">
			<h1 id="name" class="hidden"><span id="logo">Login to <span>Konnect.</span></span></h1>
		</div>
		<div class="login-box animated fadeInUp">
			<div class="box-header">
				<h2 class="heading">Verify Account:</h2>
			</div>
			<div style="color:#665851;font-size:250%;text-align:center"><c:out value="${verificationmsg}"></c:out></div>
			<form action="/verify/${code}" method="post">
				<input type="hidden" name="code" value="${code}">
				<label for="password">Password</label>
				<br/>
				<input type="password" name="pwd" id="password" required>
				<br/>
				<button type="submit">Verify</button>
			</form>
			<br/>
		</div>
	</div>

</body>

<script>
	$(document).ready(function () {
    	$('#logo').addClass('animated fadeInDown');
    	$("input:text:visible:first").focus();
	});
	$('#username').focus(function() {
		$('label[for="username"]').addClass('selected');
	});
	$('#username').blur(function() {
		$('label[for="username"]').removeClass('selected');
	});
	$('#password').focus(function() {
		$('label[for="password"]').addClass('selected');
	});
	$('#password').blur(function() {
		$('label[for="password"]').removeClass('selected');
	});
</script>

</html>