<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<div class="topbar-wrapper" style="z-index: 5;">
    <div class="topbar">
		<div class="fill">
			<div class="container">
				<h3><a href="/Keen.jsp">Keen Media Vault</a></h3>
				<ul>
					<li><a href="/Keen.jsp">Home</a></li>
					<li><a href="/music.jsp">Music</a></li>
					<li><a href="/images.jsp">Images</a></li>
					<li><a href="/videos.jsp">Videos</a></li>
				</ul>
				<form action="">
					<input type="text" placeholder="Search" />
				</form>
				<ul style="padding-left:20px; padding-top:5px; text-align:center;">
					<!-- Start Log On -->
					<%
						UserService us = UserServiceFactory.getUserService();
						User fred = us.getCurrentUser();
		
						if (fred != null) {
					%>
			
					Hello, <%= fred.getNickname() %> <br/>(
					<a href="<%= us.createLogoutURL(request.getRequestURI()) %>">Sign Out</a>.)
					<%
						} else {
					%>
			
					Hello!  <br/>(You can
					<a href="<%= us.createLoginURL(request.getRequestURI()) %>">Sign In</a>.)
					<%
						}
					%>
					<!-- End Log On -->
				</ul>
			</div>
		</div> <!-- /fill -->
	</div> <!-- /topbar -->
</div> <!-- topbar-wrapper -->
<div id="masthead">
	<div class="inner">
		<div class="container">
		</div>
	</div>
</div>
