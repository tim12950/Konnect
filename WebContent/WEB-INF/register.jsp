<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	<title>Konnect - Register</title>

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
	
	
	<!-- Form -->
	<div class="box">
		<div class="up">
			<h1 id="name"><span id="logo">Daily <span>UI</span></span></h1>
		</div>
		<div class="login-box animated fadeInUp">
			<div class="box-header">
				<h2 class="heading">Register</h2>
			</div>
			<div style="color:#665851;font-size:250%;text-align:center"><c:out value="${msg}"></c:out></div>
			<form action="/register" method="post">
				<label for="e-mail">E-mail</label>
				<br/>
				<input type="text" name="email" id="e-mail" required>
				<br/>
				<label for="username">Username</label>
				<br/>
				<input type="text" name="username" id="username" required>
				<br/>
				<label for="password">Password</label>
				<br/>
				<input type="password" name="pwd" id="password" required>
				<br/>
				<label for="password">Confirm Password</label>
				<br/>
				<input type="password" name="pwdA" id="password" required>
				<br/>
				<button type="submit">Register</button>
			</form>
			<br/>
		</div>
	</div>
	 <!-- /.Form -->
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