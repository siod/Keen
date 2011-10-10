package keen.server;

import java.util.Map;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Rating;

import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import keen.shared.*;
import keen.metaParsing.Music.*;
import keen.metaParsing.*;

public class Upload extends HttpServlet {
	public static final Logger log = Logger.getLogger(Upload.class.getName());
	private BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();

	public final int DATA = 0;
	public final int ART = 1;


	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		if ( req.getParameter("content").equals("image")) {
			uploadImage(req,res);

		} else if ( req.getParameter("content").equals("music")) {
			uploadMusic(req,res);

		} else if ( req.getParameter("content").equals("video")) {
			uploadVideo(req,res);

		} else {
			log.info("invalid content type");
			res.sendRedirect("/");
		}
		/*
		Map<String,BlobKey> blobs = blobServ.getUploadedBlobs(req);
		BlobKey[] blobKeys = new BlobKey[2];
		blobKeys[DATA] = blobs.get("myfile");
		blobKeys[ART]  = blobs.get("art");
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		if (blobKey == null || fred == null) {
			res.sendRedirect("/");
			return;
		} 
		log.info("User : " + fred);
		DAO oby = new DAO();
		Image image = createImage(req,blobKey,fred);
		oby.ofy().put(image);
		
		res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());
		*/

	}

	private void uploadVideo(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		Map<String,BlobKey> blobs = blobServ.getUploadedBlobs(req);
		BlobKey[] blobKeys = new BlobKey[2];
		blobKeys[DATA] = blobs.get("myFile");
		blobKeys[ART]  = blobs.get("art");
		if (blobKeys[ART] == null)
			log.info("art is null");
		blobKeys[ART] = validateBlob(blobKeys[ART]);

		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		if (blobKeys[DATA] == null || fred == null) {
			res.sendRedirect("/");
			return;
		}
		log.info("User : " + fred);
		DAO oby = new DAO();
		Video video = createVideo(req,blobKeys,fred);
		oby.ofy().put(video);
		
		res.sendRedirect("/videos.jsp");
	}
	// blobstore has being returning incorrect (IE 0 byte) blobs
	private BlobKey validateBlob(BlobKey blob) {
		if (blob == null)
			return null;
		BlobInfoFactory blobFact = new BlobInfoFactory();
		BlobInfo info = blobFact.loadBlobInfo(blob);
		if (info.getSize() != 0)
			return blob;

		log.info("invalid blob, datastore bug?");
		blobServ.delete(blob);
		return null;
	}



	private void uploadMusic(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		Map<String,BlobKey> blobs = blobServ.getUploadedBlobs(req);
		BlobKey[] blobKeys = new BlobKey[2];
		blobKeys[DATA] = blobs.get("myFile");
		blobKeys[ART]  = blobs.get("art");
		if (blobKeys[ART] == null)
			log.info("art is null");
		blobKeys[ART] = validateBlob(blobKeys[ART]);

		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		if (blobKeys[DATA] == null || fred == null) {
			res.sendRedirect("/");
			return;
		}
		log.info("User : " + fred);
		DAO oby = new DAO();
		Music music = createMusic(req,blobKeys,fred);
		oby.ofy().put(music);
		
		res.sendRedirect("/music.jsp");
		//res.sendRedirect("/serve?blob-key=" + blobKeys[DATA].getKeyString());

	}

	private void uploadImage(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		Map<String,BlobKey> blobs = blobServ.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("myFile");
		UserService us = UserServiceFactory.getUserService();
		User fred = us.getCurrentUser();
		log.info("User : " + fred);
		if (blobKey == null || fred == null) {
			res.sendRedirect("/");
			return;
		} 
		log.info("User : " + fred);
		DAO oby = new DAO();
		Image image = createImage(req,blobKey,fred);
		oby.ofy().put(image);
		
		res.sendRedirect("/images.jsp");
		//res.sendRedirect("/serve?blob-key=" + blobKey.getKeyString());

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

	public int tryParseInt(String srating) {
		try {
			int r = Integer.parseInt(srating);
			if ( r > 0)
				return r;

		} catch(NumberFormatException e) {
			log.info("Invalid rating passed, setting to 0");
		}
			log.info("invalid length");
			return 0;

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

	public Music createMusic(HttpServletRequest req,BlobKey[] blobKey,User fred) {

		//TODO
		//Testing only
		TestMetaParsing(blobKey[DATA]);
		// ------------

		String owner = fred.getUserId();
		
		int length = tryParseInt(req.getParameter("length"));
		
		String songName = req.getParameter("songName");

		String artist = req.getParameter("artist");
		log.info("Artist : " + artist);

		String genre = req.getParameter("genre");
		
		log.info("BlobKey DATA: " + blobKey[DATA].toString() + "\n BlobKey Art: " + blobKey[ART].toString());
		
		Rating rating = parseRating(req.getParameter("rating"));

		String[] tags = parseTags(req.getParameter("tags"));

		for ( String t : tags) {
			log.info("Tag : " + t);
		}
		Text comment = null;
		if (req.getParameter("comment")!=null) {
			comment = new Text(req.getParameter("comment"));
		}
		
		int trackNum = tryParseInt(req.getParameter("trackNum"));
		int discNum = tryParseInt(req.getParameter("discNum"));
		
		Music music = new Music(owner,length,songName,artist,genre,blobKey[DATA],blobKey[ART],rating,tags,trackNum,discNum,comment);

		return music;
	}

	public Video createVideo(HttpServletRequest req,BlobKey[] blobKey,User fred) {

		String owner = fred.getUserId();
		
		int length = tryParseInt(req.getParameter("length"));
		
		String title = req.getParameter("title");
		String director = req.getParameter("director");

		log.info("director : " + director);

		log.info("BlobKey DATA: " + blobKey[DATA].toString() + "\n BlobKey Art: " + blobKey[ART].toString());
		
		Rating rating = parseRating(req.getParameter("rating"));

		String[] tags = parseTags(req.getParameter("tags"));
		String[] actors = parseTags(req.getParameter("actors"));

		for ( String t : tags) {
			log.info("Tag : " + t);
		}
		Text comment = null;
		if (req.getParameter("comment")!=null) {
			comment = new Text(req.getParameter("comment"));
		}
		
		Video video = new Video(owner,length,title,director,actors,tags,blobKey[DATA],blobKey[ART],rating,comment);

		return video;
	}

	//TODO
	// This method must be disabled before deployment
	private void TestMetaParsing(BlobKey blobKey) {

		try {
			Mp3ID3v2 test = new Mp3ID3v2(new ChainedBlobstoreInputStream(blobKey));
			try {
				log.info("Album :" + test.getAlbum());
			} catch (Exception e) {
				log.info("Album is null");
			}
			try {
				log.info("SongName :" + test.getSongName());
			} catch (Exception e) {
				log.info("SongName is null");
			}
			try {
				log.info("Artist :" + test.getArtist());
			} catch (Exception e) {
				log.info("Artist is null");
			}
			try {
				log.info("Track Number :" + test.getTrackNum());
			} catch (Exception e) {
				log.info("Track is null");
			}
			try {
				log.info("Disc Number :" + test.getDiscNum());
			} catch (Exception e) {
				log.info("Disc is null");
			}
			try {
				log.info("Rating :" + test.getRating());
			} catch (Exception e) {
				log.info("Rating is null");
			}
			try {
				log.info("Genre :" + test.getGenre());
			} catch (Exception e) {
				log.info("Rating is null");
			}
		} catch (Exception e) {
			log.info(":( " + e.toString());
		}

	}

}

