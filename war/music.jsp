<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.googlecode.objectify.*" %>
<%@ page import="keen.shared.Music" %>
<%@ page import="keen.shared.DAO" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobKey" %>
<%@ page import="com.google.appengine.api.datastore.Text" %>
<%
	BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();
	String HOPETHISWORKS = "Has too";
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
	<script type="text/javascript">
	$(document).ready(function(){
	var fileLoc = hi();
        $("#jquery_jplayer_1").jPlayer({
                ready: function () {
                        $(this).jPlayer("setMedia", {
							mp3: fileLoc
                        });
                },
                swfPath: "/js",
                supplied: "mp3"
        });
	});
	</script>

	</head>

	<body>
		<jsp:include page="/topbar.jsp"/>
	
		<div class="container-fluid">
		
		<div class="sidebar" style="width:420px;padding-right:30px">
			<%
	 		UserService us = UserServiceFactory.getUserService();
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
			<div id="jquery_jplayer_1" class="jp-jplayer"></div>
                <div id="jp_container_1" class="jp-audio">
                        <div class="jp-type-single">
                                <div class="jp-gui jp-interface">
                                        <ul class="jp-controls">
                                                <li><a href="javascript:;" class="jp-play" tabindex="1">play</a></li>
                                                <li><a href="javascript:;" class="jp-pause" tabindex="1">pause</a></li>
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
                                        <div class="jp-time-holder">
                                                <div class="jp-current-time"></div>
                                                <div class="jp-duration"></div>
                                                <ul class="jp-toggles">
                                                        <li><a href="javascript:;" class="jp-repeat" tabindex="1" title="repeat">repeat</a></li>
                                                        <li><a href="javascript:;" class="jp-repeat-off" tabindex="1" title="repeat off">repeat off</a></li>
                                                </ul>
                                        </div>
                                </div>
                                <div class="jp-title">
                                        <ul>
                                                <li>Cro Magnon Man</li>
                                        </ul>
                                </div>
                                <div class="jp-no-solution">
                                        <span>Update Required</span>
                                        To play the media you will need to either update your browser to a recent version or update your <a href="http://get.adobe.com/flashplayer/" target="_blank">Flash plugin</a>.
                                </div>
                        </div>
                </div>	
			</div>
			<div class="content" style="margin-left:440px">
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
			<%
				for (Music music : query) {
				String temp = "";
				for (String tag : music.tags)
					temp += tag + ";";
				%>
				<tr>
					<td> <%=music.songName%></td>
					<td> <%=music.artist%> </td>
					<td> <%=music.genre%> </td>
					<td> <%=music.trackNum%> </td>
					<td> <%=music.discNum%> </td>
					<td> <%=temp%> </td>
					<td> <a href="/serve?blob-key=<%=music.data.getKeyString()%>">Download</a> </td>
				</tr>

				<script type="text/javascript">
					function hi() {
						return "<%= "/serve?blob-key=" + music.data.getKeyString() %>";
					}
				</script>
				<%
				}
				%>
			</table>
			<%
			}
		}
		%>
	

		</div>
		</div>
	</body>
<html>
