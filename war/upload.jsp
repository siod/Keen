<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
	BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();
%>

<html>
	<head>
		<title> Upload Test</title>
		<link type="text/css" rel="stylesheet" href="/css/main.css" />
	</head>
	<body>
	<div id="content">
		<%
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();

		if (fred != null) {
%>
<p> Hello, <%= fred.getNickname() %> (
<a href="<%= us.createLogoutURL(request.getRequestURI()) %>">Sign Out</a>.)</p>
<br />
<form action="<%= blobServ.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
	<p>Author: <input type="text" name="author"> </p>
	<p>Subject: <input type="text" name="title"> </p>
	<p>Comment: <input type="text" name="comment"> </p>
	<p>Tags: <input type="text" name="tag"> </p>
	<p>Filetype: <select name="filetype">
				<option value="JPG"> JPG </option>
				<option value="PNG"> PNG </option>
				<option value="GIF"> GIF </option>
			</select> </p>
	<p>File to Store :<input type="file" name="myFile"> </p>
	<p><input type="submit" value="Upload"> </p>
</form>
<%
	} else {
%>
<p>Hello!  (You can
<a href="<%= us.createLoginURL(request.getRequestURI()) %>">Sign In</a>.)</p>
<%
	}
%>


			</div>
	</body>
<html>
