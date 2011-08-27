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
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Images</title>
		<meta name="description" content="">
		<meta name="author" content="">
		
		<jsp:include page="/includes.jsp"/>
		
		<link type="text/css" rel="stylesheet" href="/css/main.css" />
		<link rel="stylesheet" href="css/galleriffic.css" type="text/css" />
		<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
		<script type="text/javascript" src="js/jquery.galleriffic.js"></script>
		<script type="text/javascript" src="js/jquery.opacityrollover.js"></script>
		<!-- We only want the thunbnails to display when javascript is disabled -->
		<script type="text/javascript">
			document.write('<style>.noscript { display: none; }</style>');
		</script>
		
 	<head>
	<body style="height:100%;">
		<jsp:include page="/topbar.jsp"/>
	
	<div id="content" style="height:100%;">
	
	<div class="page-header">
    	<h1>Images <small>Woot!</small></h1>
 	</div>
	<a href="/uploadImages.jsp">Upload Images</a>
	<br/><br/><br/>
	
		<%
			UserService us = UserServiceFactory.getUserService();
			User fred = us.getCurrentUser();
			ImagesService is = ImagesServiceFactory.getImagesService();
			DAO dao = new DAO();
			
			Query<Image> query = dao.ofy().query(Image.class).filter("owner",fred.getUserId()).order("date");
			if (!(query.count() > 0)) {
		%>
		<p>No Images to view</p>
		<%
			} else {
		%>
		<!-- Start Advanced Gallery Html Containers -->
		<div id="gallery" class="content">
			<div id="controls" class="controls"></div>
			<div class="slideshow-container">
				<div id="loading" class="loader"></div>
				<div id="slideshow" class="slideshow"></div>
			</div>
			<div id="caption" class="caption-container"></div>
		</div>
		<div id="thumbs" class="navigation" style="height:90%; overflow:auto;">
		
	<ul class="thumbs noscript">
		
		<%
	for (Image img : query) {
		%>
		<li>
			<a class="thumb" name="<%= img.title %>" href="<%= is.getServingUrl(img.data) + "=s400" %>" title="<%= img.title %>">
				<img src="<%= is.getServingUrl(img.data) + "=s200" %>" title="<%= img.title %>" />
			</a>
			<div class="caption">
				<div class="download">
					<a href="/serve?blob-key=<%=img.data.getKeyString()%>"> Download </a>
				</div>
				<p> <%=img.comment %> </p>
			</div>
		</li>
	<%
	}
	}
	%>
	</ul>
	</div>
	</div>
	<script type="text/javascript">
			jQuery(document).ready(function($) {
				// We only want these styles applied when javascript is enabled
				$('div.navigation').css({'width' : '300px', 'float' : 'left'});
				$('div.content').css('display', 'block');

				// Initially set opacity on thumbs and add
				// additional styling for hover effect on thumbs
				var onMouseOutOpacity = 0.67;
				$('#thumbs ul.thumbs li').opacityrollover({
					mouseOutOpacity:   onMouseOutOpacity,
					mouseOverOpacity:  1.0,
					fadeSpeed:         'fast',
					exemptionSelector: '.selected'
				});
				
				// Initialize Advanced Galleriffic Gallery
				var gallery = $('#thumbs').galleriffic({
					delay:                     2500,
					numThumbs:                 15,
					preloadAhead:              10,
					enableTopPager:            true,
					enableBottomPager:         true,
					maxPagesToShow:            7,
					imageContainerSel:         '#slideshow',
					controlsContainerSel:      '#controls',
					captionContainerSel:       '#caption',
					loadingContainerSel:       '#loading',
					renderSSControls:          true,
					renderNavControls:         true,
					playLinkText:              'Play Slideshow',
					pauseLinkText:             'Pause Slideshow',
					prevLinkText:              '&lsaquo; Previous Photo',
					nextLinkText:              'Next Photo &rsaquo;',
					nextPageLinkText:          'Next &rsaquo;',
					prevPageLinkText:          '&lsaquo; Prev',
					enableHistory:             false,
					autoStart:                 false,
					syncTransitions:           true,
					defaultTransitionDuration: 900,
					onSlideChange:             function(prevIndex, nextIndex) {
						// 'this' refers to the gallery, which is an extension of $('#thumbs')
						this.find('ul.thumbs').children()
							.eq(prevIndex).fadeTo('fast', onMouseOutOpacity).end()
							.eq(nextIndex).fadeTo('fast', 1.0);
					},
					onPageTransitionOut:       function(callback) {
						this.fadeTo('fast', 0.0, callback);
					},
					onPageTransitionIn:        function() {
						this.fadeTo('fast', 1.0);
					}
				});
			});
		</script>

	</body>
</html>
