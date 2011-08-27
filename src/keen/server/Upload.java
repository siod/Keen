package keen.server;

import java.util.Map;
import java.util.ArrayList;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
//import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Rating;
//import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import keen.shared.*;

public class Upload extends HttpServlet {
	public static final Logger log = Logger.getLogger(Upload.class.getName());
	private BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		Map<String,BlobKey> blobs = blobServ.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("myFile");
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		if (blobKey == null || fred == null) {
			res.sendRedirect("/");
		} 
		log.info("User : " + fred);
		DAO oby = new DAO();
		Image image = createImage(req,blobKey,fred);
		oby.ofy().put(image);
		
		res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());

	}


	private String[] parseTags(String stags) {
		if (stags == null)
			return null;
		String[] tags = stags.split(";");
		return tags;
	}

	private Rating parseRating(String srating) {
		if (srating == null)
			return null;
		try {
			int r = Integer.parseInt(srating);
			return new Rating(r);
		} catch(NumberFormatException e) {
			log.info("Invalid rating passed, setting to null");
		}
		return null;
	}

	//Assumed that all paramters are not null
	public Image createImage(HttpServletRequest req,BlobKey blobKey,User fred) {

		String owner = fred.getUserId();

		String title = req.getParameter("title");

		String artist = req.getParameter("artist");
		log.info("Artist : " + artist);

		Rating rating = parseRating(req.getParameter("rating"));

		String[] tags = parseTags(req.getParameter("tags"));

		log.info("Tag : " + tags);
		Text comment = null;
		if (req.getParameter("comment")!=null) {
			comment = new Text(req.getParameter("comment"));
		}
		log.info("BlobKey : " + blobKey.toString());
		Image image = new Image(owner,title,artist,blobKey,rating,tags,comment,new Date());

		return image;
	}
}

