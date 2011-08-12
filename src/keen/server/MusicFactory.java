package keen.server;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


import com.google.appengine.api.datastore.Rating;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


import com.google.appengine.api.blobstore.BlobKey;

public class MusicFactory {
	Entity imageDetails;


	public MusicFactory(User user) {
		imageDetails = new Entity("music");
		imageDetails.setProperty("owner",user);
	}

	public void AddArtist(String artist) {
		imageDetails.setProperty("artist",artist);

	}

	public void AddGenre(String genre) {
		imageDetails.setProperty("genre",genre);
	}

	public void AddAlbum(String album) {
		imageDetails.setProperty("album",album);
	}

	public void AddLength(String length) {
		imageDetails.setProperty("length",length);
	}

	public void AddFileType(String fileType) {
		imageDetails.setProperty("filetype",fileType);
	}

	public void AddRating(int rating) {
		if (rating > Rating.MAX_VALUE || rating < Rating.MIN_VALUE)
			// error
			return;

		imageDetails.setProperty("rating",new Rating(rating));
	}


	public void AddBlobKey(BlobKey blobKey) {
		imageDetails.setProperty("blobkey",blobKey);
	}



}

