<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Keen Media Vault</title>
    <meta name="description" content="">
    <meta name="author" content="">
	
	<jsp:include page="/includes.jsp"/>
	
	<script type="text/javascript" src="/js/bootstrap-twipsy.js"> </script>
	<script type="text/javascript" src="/js/bootstrap-popover.js"> </script>
	<script type="text/javascript" src="/js/bootstrap-alerts.js"> </script>
	
	<script type="text/javascript">
			$(document).ready(function(){
					$("li[rel=popover]").popover('show');
				});
		</script>
	
	</head>

	<body>
		<jsp:include page="/topbar.jsp"/>

		<div class="container">
			<div class="alert-message warning fade in" data-alert="alert" id="alert" style="margin-top:-50px">
				<a class="close" href="#">&times;</a>
				<p><strong>Welcome to Keen Media Vault!</strong> &nbsp;&nbsp;&nbsp; ...why are you still here? Click the 'Music', 'Images' or 'Video' links above to start with the cool stuff!</p>
			</div>

			<script> 
			$("#alert").hide();
			
			if(alert == true){
				$("#searchBox").hide();
				$("#alert").show('slow');
			}
			</script>
			<br/>
			<div class="page-header">
				<h2>What is Keen Media Vault? <small> Awesome, thats what!</small></h2>
			</div>
			<!-- Table structure -->
			<div class="row">
				<div class="span12">
					<p>
						Keen Media Vault is a cloaud based service to which you can upload your images, music and video files.  
						This allows you to view, play and/or download them from anywhere with an internet connection.
						<br/><br/>
						Once you begin using KMV you can upload your files to the cloud where they are secure from physical harm 
						such as hard disk failure or flood or fire damage.
					</p>
				</div>
			</div>
			
			<br/><br/>
			
			<div class="page-header">
				<h2>Features <small> Oooh man, do we have some features for you!</small></h2>
			</div>
			<!-- Table structure -->
			<div class="row">
				<div class="span12">
					<p>KMV has a tonne of cool features.  Here's a list of some of the coolest:</p>
					<ul>
						<strong>
						<li style="color:#000000;">Stream your music AND video in your browser</li>
						<li style="color:#000000;">Instant searching and regex based searching</li>
						<li style="color:#000000;">KMV will read your music's tags automatically</li>
						<li style="color:#000000;">Edit multiple files at once</li>
						<li style="color:#000000;">Cloud based</li>
						</strong>
					</ul>
					<p>Plus heaps more, try it now to find out!</p>
				</div>
			</div>
			
			<br/><br/>

			<div class="page-header">
				<h2>So this must cost heaps, right?<small> Nope.</small></h2>
			</div>
			<!-- Table structure -->
			<div class="row">
				<div class="span12">
					<p>
					KMV is free! Thats right, 100% free.  No ads, no trials.  Just log in and start uploading!
					</p>
				</div>
			</div>
			
		</div>
		
	</body>
<html>
