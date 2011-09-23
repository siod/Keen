package keen.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.*;

import keen.shared.*;

public class GCMark extends HttpServlet {

	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException {
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		if (fred == null) {
			res.setStatus(400);
			return;
		}
		DAO dao = new DAO();
		Long id;
		if ((id = parseId(req.getParameter("id"))) == 0L) {
			res.setStatus(400);
			return;
		}

		Key<Media> key = new Key<Media>(Media.class,id);
		Media media = dao.ofy().get(key);
		if (!fred.getUserId().equals(media.owner)) {
			res.setStatus(401);
			return;
		}

		media.toDelete = true;
		dao.ofy().put(media);
	}

	private Long parseId(String sid) {
		try {
			return Long.parseLong(sid);
		} catch(Exception e) {

		}
		return new Long(0);

	}
}
