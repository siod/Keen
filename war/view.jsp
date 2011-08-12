<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>

<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="keen.server.ImageAdaptor" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>

<html>
	<head>
		<title> View images uploaded </title>
		<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
		<head>
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

<%
	DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
	Query query = new Query("image").addSort("date",Query.SortDirection.DESCENDING);
	List<Entity> images = dataStore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
	if (images.isEmpty()) {
		%>
		<p>No Images to view</p>
		<%
	} else {
	%>
	<p> Images </p>
	<%
	for (Entity ent : images) {
		ImageAdaptor image = new ImageAdaptor(ent);
		%>
		<div>
		<p> Image by user <%= image.getUser() %>
			Author <%= image.GetAuthor() %>
			Tags <%= image.GetTag() %> 
			Subjects <%= image.GetSubject() %> 
			Comments <%= image.GetComment() %> 
			Dates <%= image.GetDate() %> 
			Tags <%= image.GetTag() %> 
			</p>
			<form action="/serve" method="get">
				<input type="hidden" name="blob-key" value="<%=image.GetBlobKey().getKeyString() %>">
				<input type="submit" value="View image" />
			</form>
		</div>
	<%
	}
	}
	%>
	</body>
</html>
