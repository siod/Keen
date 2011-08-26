<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
	BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Videos</title>
    <meta name="description" content="">
    <meta name="author" content="">

    <jsp:include page="/includes.jsp"/>

	</head>

	<body onLoad="prettyPrint();">
		<jsp:include page="/topbar.jsp"/>
	
		<div class="container">
		
			<div class="page-header">
    			<h1>Videos <small>Huzzah!</small></h1>
 			</div>
			 
			 <a href="/uploadVideos.jsp">Upload Videos</a>
		</div>
	</body>
<html>
