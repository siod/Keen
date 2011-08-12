package keen.server;

import java.util.Map;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Upload extends HttpServlet {
	public static final Logger log = Logger.getLogger(Upload.class.getName());
	private BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();
	private DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		Map<String,BlobKey> blobs = blobServ.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("myFile");
		if (blobKey == null) {
			res.sendRedirect("/");
		} 
		Entity mediaFile = createImage(req,blobKey);
		dataStore.put(mediaFile);
		
		res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());

	}
	public Entity createImage(HttpServletRequest req,BlobKey blobKey) {
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		log.info("User : " + fred);
		ImageCreator newImage = new ImageCreator(fred);
		String param;
		if ((param = req.getParameter("author")) != null) {
			log.info("Author : " + param);
			newImage.AddAuthor(param);
		}
		if ((param = req.getParameter("subject")) != null) {
			log.info("Subject : " + param);
			newImage.AddSubject(param);
		}
		if ((param = req.getParameter("tag")) != null) {
			log.info("Tag : " + param);
			newImage.AddTag(param);
		}

		if ((param = req.getParameter("comment")) != null) {
			log.info("comment : " + param);
			Text comment = new Text(param);
			newImage.AddComment(comment);
		}
		if ((param = req.getParameter("filetype")) != null) {
			log.info("fileType : " + param);
			newImage.AddFileType(param);
		}
		log.info("BlobKey : " + blobKey.toString());
		newImage.AddBlobKey(blobKey);
		newImage.AddDate(new Date());
		return newImage.getImageEntity();
	}
}

