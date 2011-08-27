package keen.server;

import java.util.List;
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

public class MusicCreator extends baseCreator {

	public MusicCreator(User user) {
		super(user,"music");
	}

	/*
	public void AddTag(String tag) {
		musicDetails.setProperty("tag",tag);
	}
	public void AddArtist(String artist) {
		musicDetails.setProperty("artist",artist);

	}

	*/
	public void AddGenre(String genre) {
		baseDetails.setProperty("genre",genre);
	}

	public void AddAlbum(String album) {
		baseDetails.setProperty("album",album);
	}

	public void AddLength(String length) {
		baseDetails.setProperty("length",length);
	}

	/*
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
	*/
	//TODO rename to getEntity
	public Entity getMusicEntity() {
		return baseDetails;
	}



}

