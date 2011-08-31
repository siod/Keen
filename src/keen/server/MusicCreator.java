package keen.server;

import java.util.List;
import java.lang.*;
import java.util.Date;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


import com.google.appengine.api.blobstore.BlobKey;

//TODO Implement as Creator
public class MusicCreator extends baseCreator {
	static enum FILETYPE { MP3, M4A, MP4, WMA }
	Entity musicDetails;

	public MusicCreator(User user) {
		super(user,"music");
	}

	
	public void AddTag(String tag) {
		musicDetails.setProperty("tag",tag);
	}
	public void AddArtist(String artist) {
		musicDetails.setProperty("artist",artist);

	}

	
	public void AddGenre(String genre) {
		musicDetails.setProperty("genre",genre);
	}

	public void AddAlbum(String album) {
		musicDetails.setProperty("album",album);
	}

	public void AddLength(String length) {
		musicDetails.setProperty("length",length);
	}
	
	public void AddTrackNum(int trackNum) {
		
		musicDetails.setProperty("trackNum", trackNum);
	}
	
	public void AddDiscNum(int discNum) {
		
		musicDetails.setProperty("discNum", discNum);
	}

	
	public void AddFileType(String fileType) {
		musicDetails.setProperty("filetype",fileType);
	}

	public void AddComment(Text comment) {
		musicDetails.setProperty("comment",comment);
	}

	public void AddRating(String srating) {
		int rating = Integer.parseInt(srating);
		if (rating > Rating.MAX_VALUE || rating < Rating.MIN_VALUE)
			// error
			return;

		musicDetails.setProperty("rating",new Rating(rating));
	}

	public void AddDate(Date date) {
		musicDetails.setProperty("date",date);
	}

	public void AddBlobKey(BlobKey blobKey) {
		musicDetails.setProperty("blobkey",blobKey);
	}
	
	//TODO rename to getEntity
	public Entity getMusicEntity() {
		return baseDetails;
	}



}

