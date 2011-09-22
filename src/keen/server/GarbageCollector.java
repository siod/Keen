package keen.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.appengine.api.datastore.QueryResultIterable;

import com.googlecode.objectify.*;

import keen.shared.*;



public class GarbageCollector {

	List<BlobKey> markedBlobs;
	BlobstoreService blobServ;
	DAO dao;
	// singleton
	private static GarbageCollector gc;



	public static GarbageCollector getGarbageCollector() {
		if (gc == null)
			gc = new GarbageCollector();
		return gc;
	}

	//---------------

	private GarbageCollector() {
		blobServ = BlobstoreServiceFactory.getBlobstoreService();
		dao = new DAO();
		markedBlobs = new ArrayList<BlobKey>();

	}


	private void SweepMusic() {
		QueryResultIterable<Music> marked = dao.ofy().query(Music.class).filter("toDelete =",true).fetch();

		for (Music music : marked) {
			if (music.data != null)
				markedBlobs.add(music.data);
			if (music.albumArt != null)
				markedBlobs.add(music.albumArt);
		}
		dao.ofy().delete(marked);

	}

	private void SweepVideo() {
		QueryResultIterable<Video> marked = dao.ofy().query(Video.class).filter("toDelete =",true).fetch();

		for (Video video : marked) {
			if (video.data != null)
				markedBlobs.add(video.data);
			if (video.boxArt != null)
				markedBlobs.add(video.boxArt);
		}
		dao.ofy().delete(marked);

	}



	private void SweepImage() {
		QueryResultIterable<Image> marked = dao.ofy().query(Image.class).filter("toDelete =",true).fetch();

		for (Image image : marked) {
			if (image.data != null)
				markedBlobs.add(image.data);
		}
		dao.ofy().delete(marked);

	}
	// deletes all items that have been marked for deletion
	public void Sweep() {
		
		SweepMusic();
		SweepVideo();
		SweepImage();
		blobServ.delete(markedBlobs.toArray(new BlobKey[0]));


	}


}
