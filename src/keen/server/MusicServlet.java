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
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.googlecode.objectify.*;

import keen.shared.*;

@SuppressWarnings("serial")
public class MusicServlet extends HttpServlet {
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
				
				Key<Music> key = new Key<Music>(Music.class,id);
				Music music;
				try {
					music = dao.ofy().get(key);
				} catch (NotFoundException e) {
					res.setStatus(401);
					return;
				}
				if (!fred.getUserId().equals(music.owner)) {
					res.setStatus(401);
					return;
				}
				if (music.data != null){
					markedBlobs.add(music.data);
				}
			
				log.info("deleted music with id = " + id.toString());
				dao.ofy().delete(music);
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
				Key<Music> key = new Key<Music>(Music.class,id);
				Music music;
				//read existing data
				try {
					music = dao.ofy().get(key);
				} catch (NotFoundException e) {
					res.setStatus(401);
					return;
				}
				if (!fred.getUserId().equals(music.owner)) {
					continue;
				}
	
				if (!req.getParameter("songName").equals("")) {
					music.songName = req.getParameter("songName");
				}
				
				if (!req.getParameter("album").equals("")) {
					music.album = req.getParameter("album");
				}
				
				if (!req.getParameter("artist").equals("")) {
					music.artist = req.getParameter("artist");
				}
				
				if (!req.getParameter("genre").equals("")) {
					music.genre = req.getParameter("genre");
				}
				
				if (!req.getParameter("comment").equals("")) {
					music.comment = new Text(req.getParameter("comment"));
				}
				
				if (!req.getParameter("tags").equals("")) {
					music.tags = Arrays.asList(req.getParameter("tags").split(";"));
				}
				
				if (!req.getParameter("rating").equals("0")) {
					music.rating = new Rating(Integer.parseInt(req.getParameter("rating")));
				}
				
				
	
				dao.ofy().put(music);
			}
		}
	}
}
