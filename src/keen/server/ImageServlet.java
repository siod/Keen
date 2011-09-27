package keen.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.googlecode.objectify.*;

import keen.shared.*;

public class ImageServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(Upload.class.getName());


	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		if (fred == null) {
			res.setStatus(400);
			return;
		}
		Long id;
		if ((id = parseId(req.getParameter("id"))) == null) {
			res.setStatus(400);
			return;
		}
		DAO dao = new DAO();
		List<BlobKey> markedBlobs = new ArrayList<BlobKey>(1);
		Key<Image> key = new Key<Image>(Image.class,id);
		Image image;
		try {
			image = dao.ofy().get(key);
		} catch (NotFoundException e) {
			res.setStatus(401);
			return;
		}
		if (!fred.getUserId().equals(image.owner)) {
			res.setStatus(401);
			return;
		}
		log.info("deleted image with id = " + id.toString());
		imageBlobs(image,markedBlobs);
		dao.ofy().delete(image);
		BlobstoreServiceFactory.getBlobstoreService().delete(markedBlobs.toArray(new BlobKey[0]));

	}

	private void imageBlobs(Image image, List<BlobKey> markedBlobs) {
		if (image.data != null)
			markedBlobs.add(image.data);
	}

	private Long parseId(String sid) {
		try {
			Long id = Long.parseLong(sid);
			if (id <= 0L)
				return null;
			return id;
		} catch(Exception e) {

		}
		return null;

	}
}
