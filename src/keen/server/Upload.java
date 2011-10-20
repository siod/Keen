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
import java.io.PrintWriter;

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


	// return new fileupload url
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("application/json");
		String jsonData = "{ \"url\": \"" + blobServ.createUploadUrl("/upload") + "\" }";
		PrintWriter out = res.getWriter();
		out.print(jsonData);
		out.flush();
	}

	public void createJSON(BlobKey blob,HttpServletResponse res) throws IOException {
		BlobInfoFactory blobFact = new BlobInfoFactory();
		BlobInfo info = blobFact.loadBlobInfo(blob);
		String jsonData = "[{ \"name\": \"" + info.getFilename() + "\", \"size\": \"" + info.getSize() + "\" }]";
		PrintWriter out = res.getWriter();
		out.print(jsonData);
		out.flush();
	}

	private void freeBlob(BlobKey blob) {
		if (blob != null)
			blobServ.delete(blob);
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



	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		Map<String,BlobKey> blobs = blobServ.getUploadedBlobs(req);
		BlobKey blob = blobs.get("myFile");
		blob = validateBlob(blob);
		UserService us = UserServiceFactory.getUserService();
		User user = us.getCurrentUser();

		if (blob == null || user == null) {
			freeBlob(blob);
			res.setStatus(400);
			return;
		}

		BlobInfoFactory blobFact = new BlobInfoFactory();
		BlobInfo info = blobFact.loadBlobInfo(blob);

		switch (Type.getInstance().parseType(info.getContentType())) {
			case MUSIC:
				uploadMusic(req,res,user,blob);
				break;
			case IMAGE:
				uploadImage(req,res,user,blob);
				break;
			case VIDEO:
				uploadVideo(req,res,user,blob);
				break;
			default:
				freeBlob(blob);
				res.setStatus(400);
				break;
		}
	}

	private void uploadVideo(HttpServletRequest req,
								HttpServletResponse res,
								User user,BlobKey blobKey) throws IOException, ServletException {
		log.info("User : " + user);
		DAO oby = new DAO();
		Video video = createVideo(req,blobKey,user);
		oby.ofy().put(video);
		createJSON(blobKey,res);
	}

	private void uploadMusic(HttpServletRequest req,
								HttpServletResponse res,
								User user,BlobKey blobKey) throws IOException, ServletException {
		log.info("User : " + user);
		DAO oby = new DAO();
		Music music = createMusic(req,blobKey,user);
		oby.ofy().put(music);
		
		createJSON(blobKey,res);

	}

	private void uploadImage(HttpServletRequest req,
								HttpServletResponse res,
								User user,BlobKey blobKey) throws IOException, ServletException {
		log.info("User : " + user);
		DAO oby = new DAO();
		Image image = createImage(req,blobKey,user);
		oby.ofy().put(image);
		
		createJSON(blobKey,res);

	}


	public String getFileName(BlobKey blob) {
		BlobInfoFactory blobFact = new BlobInfoFactory();
		BlobInfo info = blobFact.loadBlobInfo(blob);
		String filename = info.getFilename();
		int ext = filename.lastIndexOf(".");
		if (ext == -1)
			return filename;
		return filename.substring(0,ext);
	}

	//Assumed that all paramters are not null
	public Image createImage(HttpServletRequest req,BlobKey blobKey,User user) {

		String owner = user.getUserId();
		String title = getFileName(blobKey);
		log.info("BlobKey : " + blobKey.toString());
		Image image = new Image(owner,title,blobKey,null,null,null,new Date());

		return image;
	}

	public Music createMusic(HttpServletRequest req,BlobKey blobKey,User user) {

		//TODO
		//Testing only
		//TestMetaParsing(blobKey);
		// ------------
		String songName,album, artist,genre,trackNum,discNum;
		Rating rating;

		BlobInfoFactory blobFact = new BlobInfoFactory();
		BlobInfo info = blobFact.loadBlobInfo(blobKey);
		log.info("Content type = " + info.getContentType());
		try {
			MusicParser metaInfo;
			if ((metaInfo = MusicParserFactory.getInstance().getMusicParser(blobKey,info.getContentType())) != null) {
				songName = metaInfo.getSongName();
				album = metaInfo.getAlbum();
				artist = metaInfo.getArtist();
				genre = metaInfo.getGenre();
				trackNum = metaInfo.getTrackNum();
				discNum = metaInfo.getDiscNum();
				rating = new Rating(metaInfo.getRating()/255 * 10);

			} else {
				throw new Exception();

			}

		} catch (Exception e) {
			log.info("Parser exception");
			songName = getFileName(blobKey);
			album = "";
			artist = "";
			genre = "";
			rating = null;
			trackNum = "";
			discNum = "";
		}

		String owner = user.getUserId();
		log.info("Artist : " + artist);
		log.info("BlobKey DATA: " + blobKey.toString());
		Music music = new Music(owner,songName,album,artist,genre,blobKey,null,rating,null,trackNum,discNum,null);

		return music;
	}

	public Video createVideo(HttpServletRequest req,BlobKey blobKey,User user) {
		String owner = user.getUserId();
		String title = getFileName(blobKey);
		log.info("BlobKey " + blobKey.toString());
		Video video = new Video(owner,title,"",null,null,blobKey,null,null);

		return video;
	}

	/*
	//TODO
	// This method must be disabled before deployment
	private void TestMetaParsing(BlobKey blob) {

		BlobInfoFactory blobFact = new BlobInfoFactory();
		BlobInfo info = blobFact.loadBlobInfo(blob);
		log.info("Content type = " + info.getContentType());

		try {
			MusicParser test = MusicParserFactory.getInstance().getMusicParser(blob,info.getContentType());
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
	*/

}

