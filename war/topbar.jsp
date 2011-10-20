<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<div class="topbar-wrapper" style="z-index: 5;">
    <div class="topbar">
		<div class="fill">
			<div class="container">
				<h3><a href="/Keen.jsp">Keen Media Vault</a></h3>
				<ul class="nav">
					<li><a href="/Keen.jsp">Home</a></li>
					<li><a href="/music.jsp">Music</a></li>
					<li><a href="/images.jsp">Images</a></li>
					<li><a href="/videos.jsp">Videos</a></li>
				</ul>
				<form class="pull-left" action="" onSubmit="return false;">
					<input type="text" id="searchBox" placeholder="Search" />
				</form>
				<ul class="nav secondary-nav">
					<%
						UserService us = UserServiceFactory.getUserService();
						User fred = us.getCurrentUser();
		
						if (fred != null) {
					%>
					<script> alert = true; </script>
					<li class="dropdown" data-dropdown="dropdown">
					<a href="#" class="dropdown-toggle"><%=fred.getNickname() %></a>
					<ul class="dropdown-menu">
						<li> <a href="<%= us.createLogoutURL(request.getRequestURI())%>">Sign Out</a> </li>
					</ul>
					</li>
					<% } else { %>
					<li id="signIn" data-content="To access all of Keen Media Vault's awesome features you need to sign in.<br/><br/>Click 'Sign In' above to login with your google account and get started!" rel="popover" href="#" data-original-title="Your not signed in!"><a href="<%= us.createLoginURL(request.getRequestURI())%>">Sign In</a><li>
					<script>
						$("#signIn").popover({trigger:'manual', placement:'below', offset:5, html:true});
						$("#signIn").popover('show');
						alert = false;
					</script>
					<% } %>
					<!-- End Log On -->
				</ul>
			</div>
		</div> <!-- /fill -->
	</div> <!-- /topbar -->
</div> <!-- topbar-wrapper -->
<div id="masthead">
	<div class="inner">
		<div class="container">
			<br />
			<br />
			<br />
			<br />
		</div>
	</div>
</div>
