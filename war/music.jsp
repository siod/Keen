<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.googlecode.objectify.*" %>
<%@ page import="keen.shared.Music" %>
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
	final int ART = 1;
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Music</title>
    <meta name="description" content="">
    <meta name="author" content="">

    <jsp:include page="/includes.jsp"/>
	<link type="text/css" href="css/skins/jplayer.blue.monday.css" rel="stylesheet" />
	<script type="text/javascript" src="/js/jquery.jplayer.min.js"> </script>
	<script type="text/javascript" src="/js/jplayer.playlist.min.js"></script>
	<%
			Query<Music> query = dao.ofy().query(Music.class).filter("owner",fred.getUserId());
		%>
	<script type="text/javascript">
	<%
			if (query.count() == 0) {
			%>
			var musicList = [];
			<% } else {
			%>
				var musicList = [
				<%
				int i = 0;
				for (Music msc : query) {
					String temp = "";
				for (String tag : msc.tags)
					temp += "<span class=\"label success\">" + tag + "</span> ";
				%>
						{
					id: '<%= msc.id%>',
					songName: '<%= msc.songName %>',
					data: '<%= msc.data.getKeyString() %>',
					album: "<%= msc.album %>",
					artist: "<%= msc.artist %>",
					genre: "<%= msc.genre %>",
					rating: "<%= (msc.comment != null) ? msc.rating.getRating() : "" %>",
					track: "<%= msc.trackNum %>",
					disk: "<%= msc.discNum %>",
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
			function addAllMusics() {
				for (x in musicList) {

						addNewTableRow(musicList[x]);
						
					$('#'+musicList[x].id).click(function() {
						myPlaylist.add( {
							title:musicList[x].songName,
							artist:musicList[x].artist,
							mp3:"/serve?blob-key=" + musicList[x].data 
						});
					});
					
				}
				$("#musicTable").tablesorter({ sortList: [[1.0]] });

			}
			
			function addNewTableRow(music) {
				
				$('#musicTable tbody').append('<tr id="' + music.id + 
											  '"> <td>' +music.songName + 
											  '</td> <td>' + music.album + 
											  '</td> <td>' + music.artist +
											  '</td> <td> ' + music.genre +
											  '</td> <td> ' + music.rating +
											  '</td> <td> ' + music.track +
											  '</td> <td> ' + music.disk +
											  '</td> <td> ' + music.tags +
											  '</td> <td> <a class="btn info" href="/serve?blob-key=' + music.data + 
											  '">Download</a> </td> '+ 
											  '<td> <input type="checkbox" name="' + music.id + '"/> </td> </tr>');
			}
			
			function search(){
				
				searchStr = document.getElementById('searchBox').value;
				console.log("in search");
				for (x in musicList) {
					if(searchStr == "" || 
					   musicList[x].songName.match(searchStr) ||
					   musicList[x].tags.match(searchStr)){
						document.getElementById(musicList[x].id).style.display = '';
					} else{
						document.getElementById(musicList[x].id).style.display = 'none';
					}
				}
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
				var songName = document.getElementById("songName").value;
				var album = document.getElementById("album").value;
				var artist = document.getElementById("artist").value;
				var genre = document.getElementById("genre").value;
				var comment = document.getElementById("comment").value;
				var tags = document.getElementById("tags").value;
				var rating = document.getElementById("rating").value;
				
				$.post('/'+ page,{action: "edit", id: ids, "songName":songName, "album":album,"artist":artist,"genre":genre,"comment":comment, "tags":tags, "rating":rating });
					console.log("after post");
					return false;
			}
	</script>
	<script type="text/javascript">
	var myPlaylist;
	$(document).ready(function(){
	document.getElementById('searchBox').addEventListener('keyup', search ,false);
					page = "music";
					addAllMusics();

	myPlaylist = new jPlayerPlaylist({
		jPlayer: "#jquery_jplayer_1",
		cssSelectorAncestor: "#jp_container_1"
	},[] , {
		playlistOptions: {
			enableRemoveControls: true
		},
		supplied: "mp3"
	});

	$("table#musicTable").tablesorter();

	});
	</script>

	</head>

	<body>
		<jsp:include page="/topbar.jsp"/>
	
		<div class="container-fluid">
			<div class="sidebar" style="width:422px;padding-right:30px">
				<div id="jquery_jplayer_1" class="jp-jplayer"></div>
					<div id="jp_container_1" class="jp-audio">
						<div class="jp-type-playlist">
							<div class="jp-gui jp-interface">
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
								<div class="jp-progress">
									<div class="jp-seek-bar">
										<div class="jp-play-bar"></div>

									</div>
								</div>
								<div class="jp-volume-bar">
									<div class="jp-volume-bar-value"></div>
								</div>
								<div class="jp-current-time"></div>
								<div class="jp-duration"></div>
								<ul class="jp-toggles">
									<li><a href="javascript:;" class="jp-shuffle" tabindex="1" title="shuffle">shuffle</a></li>
									<li><a href="javascript:;" class="jp-shuffle-off" tabindex="1" title="shuffle off">shuffle off</a></li>
									<li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
									<li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
								</ul>
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
						<div class="content" style="margin-left:480px">
							<div class="page-header">
								<h1>Music <small><a href="/upload.jsp">Upload Music</a></small></h1>
							</div>
							<div id="editModal" class="modal hide fade">
								<div class="modal-header">
								<a href="#" class="close">&times;</a>
							<h3>Edit</h3>
						</div>
			
            <div class="modal-body">
			
			<form id="image" onSubmit="doEdit();" action="" method="post" enctype="multipart/form-data">
				<input type="hidden" name="content" value="image" />
				<div class="clearfix">
					<label for="">Song Name</label>
					<div class="input">
						<input type="text" id="songName" name="songName" class="xlarge" placeholder="Unchanged"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Album</label>
					<div class="input">
						<input type="text" id="album" name="album" class="xlarge" placeholder="Unchanged"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Artist</label>
					<div class="input">
						<input type="text" id="artist" name="artist" class="xlarge" placeholder="Unchanged"> 
					</div>
				</div>
				<div class="clearfix">
					<label for="">Genre</label>
					<div class="input">
						<input type="text" id="genre" name="genre" class="xlarge" placeholder="Unchanged"> 
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
				<table class="zebra-striped" id="musicTable">
					<thead>
						<tr> 
							<th class="header">Song Name</th>
							<th class="red header">Album</th>
							<th class="blue header">Artist</th>
							<th class="green header">Genre</th>
							<th class="yellow header">Rating</th>
							<th class="red header">Track</th>
							<th class="red header">Disc</th>
							<th class="green header">Tags</th>
							<th class="blue header">Download</th>
							<th class='red header'>Select</th>
						</tr>
					</thead>
					<tbody>

			</tbody>
			</table>
		</div>
		</div>
	</body>
<html>
