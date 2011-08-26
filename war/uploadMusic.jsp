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
    <title>Upload Music</title>
    <meta name="description" content="">
    <meta name="author" content="">

    <jsp:include page="/includes.jsp"/>

	</head>

	<body onLoad="prettyPrint();">
		<jsp:include page="/topbar.jsp"/>
	
		<div class="container">
		
			<div class="page-header">
    			<h1>Upload Music <small>Don't use this! It don't work yet fool!</small></h1>
 			 </div>
			 
			<form action="<%= blobServ.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
				<div class="clearfix">
					<label for="">Author</label>
					<div class="input">
						<input type="text" name="author" class="xlarge" size="30"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Subject</label>
					<div class="input">
						<input type="text" name="subject" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Comment</label>
					<div class="input">
						<input type="text" name="comment" class="xlarge"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Tags</label>
					<div class="input">
						<input type="text" name="tag" class="xlarge"> 
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
					<button type="submit" class="btn primary">Upload</button>&nbsp;<button type="reset" class="btn">Cancel</button>
				</div>
			</form>
		</div>
	</body>
<html>
