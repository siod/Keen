<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.googlecode.objectify.*" %>
<%@ page import="keen.shared.Video" %>
<%@ page import="keen.shared.DAO" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.images.ImagesService" %>
<%@ page import="com.google.appengine.api.images.ImagesServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.datastore.Text" %>
<%
	BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();
	UserService us = UserServiceFactory.getUserService();
	ImagesService is = ImagesServiceFactory.getImagesService();
	User fred = us.getCurrentUser();
	DAO dao = new DAO();
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Video</title>
    <meta name="description" content="">
    <meta name="author" content="">

    <jsp:include page="/includes.jsp"/>
	<link type="text/css" href="css/skins/jplayer.blue.monday.css" rel="stylesheet" />
	<script type="text/javascript" src="/js/jquery.jplayer.min.js"> </script>
	<script type="text/javascript" src="/js/jplayer.playlist.min.js"></script>
	<%
    	Query<Video> query = dao.ofy().query(Video.class).filter("owner",fred.getUserId());
    %>
    
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
				for (Video vid : query) {
					String temp = "", temp1="";
				for (String tag : vid.tags)
					temp += "<span class=\"label success\">" + tag + "</span> ";
				for (String actor : vid.actors)
					temp1 += "<span class=\"label success\">" + actor + "</span> ";
				%>
						{
					id: '<%= vid.id%>',
					title: '<%= vid.title %>',
					data: '<%= vid.data.getKeyString() %>',
					director: "<%= vid.director %>",
					tags: '<%= temp %>',
					actors: '<%= temp1 %>'
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
			function addAllVideos() {
				for (x in List) {
					addNewTableRow(List[x]);
					trClickAdd(List[x]);
				}
				$("#videoTable").tablesorter();

			}

			function trClickAdd(data) {
				$('#'+data.id).click(function() {
						myPlaylist.add( {
							title:data.title,
							artist:data.director,
							m4v:"/serve?blob-key=" + data.data,
						});
				});
			}
			
			function addNewTableRow(video) {
				
				$('#videoTable tbody').append('<tr id="' + video.id + 
											  '"> <td>' +video.title + 
											  '</td> <td>' + video.director + 
											  '</td> <td>' + video.actors +
											  '</td> <td> ' + video.tags +
											  '</td> <td> <a class="btn info" href="/serve?blob-key=' + video.data + 
											  '">Download</a> </td> '+ 
											  '<td> <input type="checkbox" name="' + video.id + '"/> </td> </tr>');
			}
			
			function searchComparer(searchStr,data) {
				return (searchStr == "" 
						|| data.title.match(searchStr) 
						|| data.director.match(searchStr) 
						|| data.actors.match(searchStr) 
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
				var director = document.getElementById("director").value;
				var title = document.getElementById("title").value;
				var actors = document.getElementById("actors").value;
				var tags = document.getElementById("tags").value;
				
				$.post('/'+ page,{action: "edit", id: ids, "director":director,"title":title,"actors":actors,"tags":tags });
				console.log("after post");
				return false;
			}
	</script>
	<script type="text/javascript">
	var myPlaylist;
	$(document).ready(function(){
			document.getElementById('searchBox').addEventListener('keyup', search ,false);
					page = "video";
					addAllVideos();
			

	myPlaylist = new jPlayerPlaylist({
		jPlayer: "#jquery_jplayer_1",
		cssSelectorAncestor: "#jp_container_1"
		},[],
		{
		playlistOptions: {
			enableRemoveControls: true
		},
		supplied: "webmv, ogv, m4v",
		size: {
			width: "640px",
			height: "360px",
			cssClass: "jp-video-360p"
		}
	});

	$("#videoTable").tablesorter();

	});
	</script>

	</head>

	<body>
		<jsp:include page="/topbar.jsp"/>
	
		<div class="container-fluid">
		
		<div class="sidebar" style="width:645px;padding-right:30px">
		<div id="jp_container_1" class="jp-video jp-video-360p">
			<div class="jp-type-playlist">
				<div id="jquery_jplayer_1" class="jp-jplayer"></div>
				<div class="jp-gui">
					<div class="jp-video-play">
						<a href="javascript:;" class="jp-video-play-icon" tabindex="1">play</a>
					</div>
					<div class="jp-interface">
						<div class="jp-progress">
							<div class="jp-seek-bar">
								<div class="jp-play-bar"></div>
							</div>
						</div>
						<div class="jp-current-time"></div>
						<div class="jp-duration"></div>
						<div class="jp-controls-holder">
							<ul class="jp-controls">
								<li><a href="javascript:;" class="jp-previous" tabindex="1">previous</a></li>
								<li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
								<li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
								<li><a href="javascript:;" class="jp-next" tabindex="1">next</a></li>
								<li><a href="javascript:;" class="jp-stop" tabindex="1">stop</a></li>
								<li><a href="javascript:;" class="jp-mute" tabindex="1" title="mute">mute</a></li>
								<li><a href="javascript:;" class="jp-unmute" tabindex="1" title="unmute">unmute</a></li>
								<li><a href="javascript:;" class="jp-volume-max" tabindex="1" title="max volume">max volume</a></li>
							</ul>
							<div class="jp-volume-bar">
								<div class="jp-volume-bar-value"></div>
							</div>
							<ul class="jp-toggles">
								<li><a href="javascript:;" class="jp-full-screen" tabindex="1" title="full screen">full screen</a></li>
								<li><a href="javascript:;" class="jp-restore-screen" tabindex="1" title="restore screen">restore screen</a></li>
								<li><a href="javascript:;" class="jp-shuffle" tabindex="1" title="shuffle">shuffle</a></li>
								<li><a href="javascript:;" class="jp-shuffle-off" tabindex="1" title="shuffle off">shuffle off</a></li>
								<li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
								<li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
							</ul>
						</div>
						<div class="jp-title">
							<ul>
								<li></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="jp-playlist">
					<ul>
						<!-- The method Playlist.displayPlaylist() uses this unordered list -->
						<li></li>
					</ul>
				</div>
				<div class="jp-no-solution">
					<span>Update Required</span>
					To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
				</div>
			</div>
		</div>
		</div>
			<!-- info -->
			<div class="content" style="margin-left:680px">
				<div class="page-header">
				<h1>Video <small><a href="/upload.jsp">Upload Video</a></small></h1>
				</div>
				
			<div id="editModal" class="modal hide fade">
				<div class="modal-header">
					<a href="#" class="close">&times;</a>
					<h3>Edit</h3>
				</div>
				
				<!-- Modal -->
				<div class="modal-body">
					<form id="video" onSubmit="doEdit();" action="" method="post" enctype="multipart/form-data">
						<div class="clearfix">
							<label for="">Title</label>
							<div class="input">
								<input type="text" id="title" name="title" class="xlarge" placeholder="Unchanged"> 
							</div>
						</div>
						<div class="clearfix">
							<label for="">Director</label>
							<div class="input">
								<input type="text" id="director" name="director" class="xlarge" placeholder="Unchanged"> 
							</div>
						</div>
						<div class="clearfix">
							<label for="">Actors (Use ";" to seperate)</label>
							<div class="input">
								<input type="text" id="actors" name="actors" class="xlarge" placeholder="Unchanged"> 
							</div>
						</div>
						<div class="clearfix">
							<label for="">Tags (Use ";" to seperate)</label>
							<div class="input">
								<input type="text" id="tags" name="tags" class="xlarge" placeholder="Unchanged"> 
							</div>
						</div>
					</div>
						<div class="modal-footer">
							<button class="btn primary">Commit</button>
						</div>

					</form>
				</div>
				<!-- End Modal -->

				<button class="btn danger" style="float:right; margin-top:-55px;" onClick="doDelete();">Delete</button>
				<button class="btn danger" style="float:right; margin-top:-55px; margin-right:80px;" data-controls-modal="editModal" data-backdrop="true" data-keyboard="true">Edit</button>

				<table class="zebra-striped" id="videoTable">
					<thead>
						<tr> 
							<th class="header">Title</th>
							<th class="blue header">Director</th>
							<th class="green header">Actors</th>
							<th class="red header">Tags</th>
							<th class="blue header">Download</th>
							<th class="red header">Select</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
			</table>
		</div>
		</div>
	</body>
<html>

