package keen.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import java.util.logging.Logger;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.googlecode.objectify.*;

import keen.shared.*;

@SuppressWarnings("serial")
public class VideoServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(Upload.class.getName());



	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		if (fred == null) {
			res.setStatus(400);
			return;
		}
		
		if(req.getParameter("action").equals("delete")){
			String[] ids;
			String str = req.getParameter("id");
			ids = str.split("\\|");
			if ( ids == null) {
				res.setStatus(400);
				return;
			}
			List<BlobKey> markedBlobs = new ArrayList<BlobKey>();
			
			for(String strId:ids){
				Long id;
				try{
				id = Long.parseLong(strId);
				}
				catch(Exception e){
					continue;
				}
				
				DAO dao = new DAO();
				
				Key<Video> key = new Key<Video>(Video.class,id);
				Video video;
				try {
					video = dao.ofy().get(key);
				} catch (NotFoundException e) {
					res.setStatus(401);
					return;
				}
				if (!fred.getUserId().equals(video.owner)) {
					res.setStatus(401);
					return;
				}
				if (video.data != null){
					markedBlobs.add(video.data);
				}
			
				log.info("deleted video with id = " + id.toString());
				dao.ofy().delete(video);
			}
			System.out.println(markedBlobs.toArray(new BlobKey[1]));
			
			try{
				BlobstoreServiceFactory.getBlobstoreService().delete(markedBlobs.toArray(new BlobKey[0]));
			}
			catch(Exception e){
			}
			
			
		}else if(req.getParameter("action").equals("edit")){
			String[] ids;
			String str = req.getParameter("id");
			ids = str.split("\\|");
			if ( ids == null) {
				res.setStatus(400);
				return;
			}
			
			System.out.println(ids);
			for(String strId:ids){
			
				Long id;
				try{
				id = Long.parseLong(strId);
				}
				catch(Exception e){
					continue;
				}
				
				DAO dao = new DAO();
				Key<Video> key = new Key<Video>(Video.class,id);
				Video video;
				//read existing data
				try {
					video = dao.ofy().get(key);
				} catch (NotFoundException e) {
					res.setStatus(401);
					return;
				}
				if (!fred.getUserId().equals(video.owner)) {
					continue;
				}
				
				if (!req.getParameter("title").equals("")) {
					video.title = req.getParameter("title");
				}
				
				if (!req.getParameter("director").equals("")) {
					video.director = req.getParameter("director");
				}
				
				if (!req.getParameter("tags").equals("")) {
					video.tags = Arrays.asList(req.getParameter("tags").split(";"));
				}
				
				if (!req.getParameter("actors").equals("")) {
					video.actors = Arrays.asList(req.getParameter("actors").split(";"));
				}
				
				
	
				dao.ofy().put(video);
			}
		}
	}
}
