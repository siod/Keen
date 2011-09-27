<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.googlecode.objectify.*" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="keen.shared.Image" %>
<%@ page import="keen.shared.DAO" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.images.ImagesService" %>
<%@ page import="com.google.appengine.api.images.ImagesServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Text" %>
<%
			UserService us = UserServiceFactory.getUserService();
			User fred = us.getCurrentUser();
			ImagesService is = ImagesServiceFactory.getImagesService();
			DAO dao = new DAO();
%>
<%
	Query<Image> query = dao.ofy().query(Image.class).filter("owner",fred.getUserId()).order("date");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Images</title>
		<meta name="description" content="">
		<meta name="author" content="">
		
		<jsp:include page="/includes.jsp"/>
		
		<link type="text/css" rel="stylesheet" href="/css/main.css" />
		<link type="text/css" rel="stylesheet" href="/css/colorbox.css" />
		<script type="text/javascript" src="/js/bootstrap-tabs.js"> </script>
		<script type="text/javascript" src="/js/jquery.colorbox.js"> </script>
		<script type="text/javascript">
			<%
			if (query.count() == 0) {
			%>
			var imageList = [];
			<% } else {
			%>
				var imageList = [
				<%
				int i = 0;
				for (Image img : query) {
				%>
						{
					id: '<%= img.id%>',
					title: '<%= img.title %>',
					data: '<%= is.getServingUrl(img.data) %>',
					comment: '<%= img.comment.getValue() %>',
					artist: '<%= img.artist %>',
					rating: '<%= img.rating.getRating() %>',
					date: '<%= img.date.toString() %>'
					<% if (i != query.count() - 1) { %>
					},
					<% } else { %>
					}
					<%} %>
				<% ++i;
				}
				%>
				];
				<% } %>
		</script>
		<script type="text/javascript">
			$(document).ready(function(){
					page = "image";
					$("a[rel='gallery']").colorbox({photo:true,slideshow:true, slideshowAuto:false});
					$("#imageTable").tablesorter({ sortList: [[1.0]] });
				});
		</script>
 	<head>
	<body style="height:100%;">
		<jsp:include page="/topbar.jsp"/>
	
	<div id="content" style="height:100%;">
	
	<div class="page-header">
    	<h1>Images <small><a href="/upload.jsp?image=1">Upload Images</a></small></h1>
 	</div>
	<ul class="tabs" data-tabs="tabs">
		<li class="active"><a href="#imageList">List</a></li>
		<li><a href="#imageThumbs">Thumbnails</a></li>
	</ul>
	<div id="main-content" class="tab-content">
		<div class="active" id="imageList">
			<table class='zebra-striped' id='imageTable'>
				<thead>
					<tr> 
						<th class='header'>Title</th>
						<th class='blue header'>Taken by</th>
						<th class='red header'>Rating</th>
						<th class='green header'>Date Taken</th>
						<th class='yellow header'>Comment</th>
						<th class='green header'>Tags</th>
						<th class='blue header'>View</th>
						<th class='blue header'>Download</th>
						<th class='red header'>Delete</th>
					</tr>
				</thead>
				<tbody>
		<%
		int i = 0;
		for (Image image : query) {
		String temp = "";
		for (String tag : image.tags)
			temp += "<span class=\"label success\">" + tag + "</span> ";
		%>
		<tr id="<%=i%>">
					<td> <%=image.title%></td>
					<td> <%=image.artist%> </td>
					<td> <%=image.rating.getRating()%></td>
					<td> <%=image.date.toString()%></td>
					<td> <%=image.comment.getValue()%> </td>
					<td> <%=temp%> </td>
					<td> <a class="btn info" rel="gallery" href="<%= is.getServingUrl(image.data)%>" title="<%=image.title%>">View</a> </td>
					<td> <a class="btn info" href="/serve?blob-key=<%=image.data.getKeyString()%>">Download</a> </td>
					<td> <button class="btn danger" onclick="deleteData(<%=image.id%>,'#<%=i%>');">Delete</button> </td>
		</tr>
		<% 
		i++;
		}
	   	%>
		</tbody>
		</table>
		</div>
		<div id="imageThumbs"> This is a different test.</div>
	</div>
	</div>
	</body>
</html>
