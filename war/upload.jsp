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
    <title>Upload</title>
    <meta name="description" content="">
    <meta name="author" content="">

    <jsp:include page="/includes.jsp"/>

	</head>

	<body>
		<jsp:include page="/topbar.jsp"/>
	
		<div class="container">
			<%
			UserService us = UserServiceFactory.getUserService();
			User fred = us.getCurrentUser();
			if (fred != null) {
			%>
			<div id="image"><br /><br /></div>
			<div class="page-header">
    			<h1>Upload Images <small>Input the details then hit Upload!</small></h1>
 			 </div>

			<form action="<%= blobServ.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
				<input type="hidden" name="content" value="image" />
				<div class="clearfix">
					<label for="">Artist</label>
					<div class="input">
						<input type="text" name="artist" class="xlarge" size="30"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Title</label>
					<div class="input">
						<input type="text" name="title" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Comment</label>
					<div class="input">
						<input type="text" name="comment" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Tags (Use ";" to seperate)</label>
					<div class="input">
						<input type="text" name="tags" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Filetype</label>
					<div class="input">
						<select name="filetype">
							<option value="JPG"> JPG </option>
							<option value="PNG"> PNG </option>
							<option value="GIF"> GIF </option>
						</select> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">File to Store</label>
					<div class="input">
						<input type="file" name="myFile" class="input-file"> 
					</div>
				</div>
				
				<div class="actions">
					<button class="btn primary" type="submit" > Upload</button><button type="reset" class="btn">Cancel</button>
				</div>
			</form>
			<div id="music"><br /><br /></div>
			<div class="page-header">
    			<h1>Upload Music <small>Input the details then hit Upload!</small></h1>
 			 </div>
			<form action="<%= blobServ.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
				<input type="hidden" name="content" value="music" />
				<div class="clearfix">
					<label for="">Artist</label>
					<div class="input">
						<input type="text" name="artist" class="xlarge" size="30"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Title</label>
					<div class="input">
						<input type="text" name="songName" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Comment</label>
					<div class="input">
						<input type="text" name="comment" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Tags (Use ";" to seperate)</label>
					<div class="input">
						<input type="text" name="tags" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">File to Store</label>
					<div class="input">
						<input type="file" name="myFile" class="input-file"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Album Art</label>
					<div class="input">
						<input type="file" name="art" class="input-file"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Track Number</label>
					<div class="input">
						<input type="text" name="trackNum" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Disc Number</label>
					<div class="input">
						<input type="text" name="discNum" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Length</label>
					<div class="input">
						<input type="text" name="length" class="xlarge"> 
					</div>
				</div>
				<div class="actions">
					<button class="btn primary" type="submit" > Upload</button><button type="reset" class="btn">Cancel</button>
				</div>
			</form>
			<div id="video"><br /><br /></div>
			<div class="page-header">
    			<h1>Upload Video <small>Input the details then hit Upload!</small></h1>
 			 </div>
			<form action="<%= blobServ.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
				<input type="hidden" name="content" value="music" />
				<div class="clearfix">
					<label for="">Artist</label>
					<div class="input">
						<input type="text" name="artist" class="xlarge" size="30"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Title</label>
					<div class="input">
						<input type="text" name="title" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Comment</label>
					<div class="input">
						<input type="text" name="comment" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Tags (Use ";" to seperate)</label>
					<div class="input">
						<input type="text" name="tags" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">File to Store</label>
					<div class="input">
						<input type="file" name="myFile" class="input-file"> 
					</div>
				</div>
				<div class="actions">
					<button class="btn primary" type="submit" > Upload</button><button type="reset" class="btn">Cancel</button>
				</div>
			</form>

			<%
			} else {
			%>
			<p> Login to upload </p>
			<%
			}
			%>
		</div>
	</body>
<html>
