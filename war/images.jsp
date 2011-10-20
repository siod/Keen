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
	Query<Image> query = dao.ofy().query(Image.class).filter("owner",fred.getUserId());
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
			var List = [];
			<% } else {
			%>
				var List = [
				<%
				int i = 0;
				for (Image img : query) {
					String temp = "";
				for (String tag : img.tags)
					temp += "<span class=\"label success\">" + tag + "</span> ";
				%>
						{
					id: '<%= img.id%>',
					title: '<%= img.title %>',
					idata: '<%= is.getServingUrl(img.data) %>',
					data: '<%= img.data.getKeyString() %>',
					comment: "<%= (img.comment != null) ? img.comment.getValue() : "" %>",
					rating: '<%= (img.comment != null) ? img.rating.getRating() : ""%>',
					date: '<%= img.date.getDate() + "/" + img.date.getMonth() + "/" + img.date.getYear() %>',
					tags: '<%= temp %>'
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
			function addAllImages() {
				for (x in List) {

						addNewThumbnail(List[x]);
						addNewTableRow(List[x]);
					
				}
				$("a[rel='galleryThumb']").colorbox({photo:true,slideshow:true, slideshowAuto:false});
				$("a[rel='gallery']").colorbox({photo:true,slideshow:true, slideshowAuto:false});
				$("#imageTable").tablesorter();

			}
			
			function addNewThumbnail(image) {
				
				$('#imageThumbsList').append('<li><a rel="galleryThumb" href="' + image.idata 
											+ '" title="' + image.title 
											+ '"><img class="thumbnail" src="' 
											+ image.idata + '=s210" alt=""></a></li>'
											);
			}
			function addNewTableRow(image) {
				
				$('#imageTable tbody').append('<tr id="' + image.id + '"> <td>' +image.title 
						+ '</td> <td> ' + image.rating 
						+ '</td> <td> ' + image.date + '</td> <td> ' + image.comment 
						+ ' </td> <td> ' + image.tags 
						+ ' </td> <td> <a class="btn info" rel="gallery" href="' + image.idata 
						+ '" title="' + image.title 
						+ '">View</a> </td> <td> <a class="btn info" href="/download?filename=' + image.title + '&blob-key=' + image.data 
						+ '">Download</a> </td> '
						+ '<td> <input type="checkbox" name="' + image.id + '"/> </td> </tr>');

			}
			
			function searchComparer(searchStr,data) {
				return (searchStr == "" 
						|| data.title.match(searchStr) 
						||  data.comment.match(searchStr) 
						|| data.tags.match(searchStr)
						);
			}

			function doDelete(){
			matches = $(':checked');
				var ids = "";
				for(i = 0; i < matches.length; i++){
					ids += matches[i].name + "|";
				}
				for(i = 0; i < matches.length; i++){
					$('#' + matches[i].name).remove();
				}
				$.post('/'+ page,{action: "delete", id: ids });

			}
			
			function doEdit(){
			console.log("in doEdit");
				matches = $(':checked');
				var ids = "";
				for(i = 0; i < matches.length; i++){
					ids += matches[i].name + "|";
				}
				var title = document.getElementById("title").value;
				var comment = document.getElementById("comment").value;
				var tags = document.getElementById("tags").value;
				var rating = document.getElementById("rating").value;
				
				$.post('/'+ page,{action: "edit", id: ids, "title":title, "comment":comment, "tags":tags, "rating":rating });
					console.log("after post");
					return false;
			}
		</script>
		<script type="text/javascript">
			$(document).ready(function(){
					document.getElementById('searchBox').addEventListener('keyup', search ,false);
					page = "image";
					addAllImages();
				});
		</script>
 	<head>
	<body style="height:100%;">
		<jsp:include page="/topbar.jsp"/>
	
	<div id="content" style="height:100%;">
	
	<div class="page-header">
    	<h1>Images <small><a href="/upload.jsp">Upload Images</a></small></h1>
 	</div>
	<ul class="tabs" data-tabs="tabs">
		<li class="active"><a href="#imageTableDiv">List</a></li>
		<li><a href="#imageThumbs">Thumbnails</a></li>
	</ul>
	<div id="main-content" class="tab-content">

        <div id="editModal" class="modal hide fade">
            <div class="modal-header">
              <a href="#" class="close">&times;</a>
              <h3>Edit</h3>
            </div>
            <div class="modal-body">
			
			<form id="image" onSubmit="doEdit();" action="" method="post" enctype="multipart/form-data">
				<input type="hidden" name="content" value="image" />
				<div class="clearfix">
					<label for="">Title</label>
					<div class="input">
						<input type="text" id="title" name="title" class="xlarge" placeholder="Unchanged"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Comment</label>
					<div class="input">
						<textarea class="xlarge" id="comment" name="comment" placeholder="Unchanged"></textarea>
					</div>
				</div>
				<div class="clearfix">
					<label for="">Tags (Use ";" to seperate)</label>
					<div class="input">
						<input type="text" id="tags" name="tags" class="xlarge" placeholder="Unchanged"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Rating</label>
					<div class="input">
						<select name="rating" id="rating">
							<option value="0"> Unchanged </option>
							<option value="1"> 1 </option>
							<option value="2"> 2 </option>
							<option value="3"> 3 </option>
							<option value="4"> 4 </option>
							<option value="5"> 5 </option>
							<option value="6"> 6 </option>
							<option value="7"> 7 </option>
							<option value="8"> 8 </option>
							<option value="9"> 9 </option>
							<option value="10"> 10 </option>
						</select> 
					</div>
				</div>
            </div>
            <div class="modal-footer">
				<button class="btn primary">Commit</button>
			</div>
			</form>
		</div>

		<button class="btn danger" style="float:right; margin-top:-55px;" onclick="doDelete();">Delete</button>
		<button class="btn danger" style="float:right; margin-top:-55px; margin-right:80px;" data-controls-modal="editModal" data-backdrop="true" data-keyboard="true">Edit</button>
			
		<div class="active" id="imageTableDiv"> 
			<table class='zebra-striped' id='imageTable'>
				<thead>
					<tr> 
						<th class='header'>Title</th>
						<th class='red header'>Rating</th>
						<th class='green header'>Date</th>
						<th class='yellow header'>Comment</th>
						<th class='green header'>Tags</th>
						<th class='blue header'>View</th>
						<th class='blue header'>Download</th>
						<th class='red header'>Select</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			
			
		</div>
		<div id="imageThumbs">
			<ul id="imageThumbsList" class="media-grid">
			</ul>
		</div>
	</div>
	</div>
	</body>
</html>
