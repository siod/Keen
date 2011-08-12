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
	</head>
	<body>
		<%
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();

		if (fred != null) {
%>
<p> Hello, <%= fred.getNickname() %> (
<a href="<%= us.createLogoutURL(request.getRequestURI()) %>">Sign Out</a>.)</p>
<%
	} else {
%>
<p>Hello!  (You can
<a href="<%= us.createLoginURL(request.getRequestURI()) %>">Sign In</a>.)</p>
</p>
<%
	}
%>


		<form action="<%= blobServ.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
			<input type="text" name="author">
			<input type="text" name="subject">
			<input type="text" name="comment">
			<input type="text" name="tag">
			<select name="filetype">
				<option value="JPG"> JPG </option>
				<option value="PNG"> PNG </option>
				<option value="GIF"> GIF </option>
			</select>
			<br />
			<input type="text" name="foo">
			<input type="file" name="myFile">
			<input type="submit" value="Sumbit">
		</form>
	</body>
<html>
