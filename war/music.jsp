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
	<script type="text/javascript">
	var myPlaylist;
	$(document).ready(function(){

	myPlaylist = new jPlayerPlaylist({
		jPlayer: "#jquery_jplayer_1",
		cssSelectorAncestor: "#jp_container_1"
	},[] , {
		playlistOptions: {
			enableRemoveControls: true
		},
		swfPath: "js",
		supplied: "webmv, ogv, m4v, oga, mp3"
	});

	$("#musicTable").tablesorter({ sortList: [[1.0]] });

	});
	</script>

	</head>

	<body>
		<jsp:include page="/topbar.jsp"/>
	
		<div class="container-fluid">
		
		<div class="sidebar" style="width:480px;padding-right:30px">
			<%
	 		UserService us = UserServiceFactory.getUserService();
			ImagesService is = ImagesServiceFactory.getImagesService();
			User fred = us.getCurrentUser();
			if (fred != null) {
			DAO dao = new DAO();
			
			Query<Music> query = dao.ofy().query(Music.class).filter("owner",fred.getUserId());
			if (!(query.count() > 0)) {
		%>
		<p>No Music to view</p>
		<%
			} else {
			%>
		<div id="jp_container_1" class="jp-video jp-video-270p">
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
			<div class="content" style="margin-left:550px">
				<div class="page-header">
				<h1>Music <small>Yay!</small></h1>
				</div>
				<a href="/upload.jsp?music=1">Upload Music</a>
				<table class="zebra-striped" id="musicTable">
					<thead>
						<tr> 
							<th class="header">SongName</th>
							<th class="blue header">Artist</th>
							<th class="green header">Genre</th>
							<th class="red header">Track Number</th>
							<th class="red header">Disc Number</th>
							<th class="blue header">Tags</th>
							<th class="header">Download</th>
						</tr>
					</thead>
					<tbody>
			<%
				int i = 0;
				for (Music music : query) {
				if (music.toDelete == true)
					continue;
				String temp = "";
				for (String tag : music.tags)
					temp += tag + ";";
				%>
				<tr id="<%=i%>">
					<td> <%=music.songName%></td>
					<td> <%=music.artist%> </td>
					<td> <%=music.genre%> </td>
					<td> <%=music.trackNum%> </td>
					<td> <%=music.discNum%> </td>
					<td> <%=temp%> </td>
					<td> <a href="/serve?blob-key=<%=music.data.getKeyString()%>">Download</a> </td>
				</tr>
				<script type="text/javascript">
					$("#<%=i%>").click(function() {
						myPlaylist.add( {
							title:"<%=music.songName%>",
							artist:"<%=music.artist%>",
							mp3:"<%= "/serve?blob-key=" + music.data.getKeyString() %>"
							<%
							if (music.albumArt != null) {
							%>,
							poster: "<%= is.getServingUrl(music.albumArt,640,false) %>",
							<%
							}
							%>
						});
					});
				</script>
				<%
				i++;
				}
				%>
			</tbody>
			</table>
			<%
			}
		}
		%>
	

		</div>
		</div>
	</body>
<html>
