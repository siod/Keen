// DO NOT USE THIS CLASS
package keen.server;

//import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Date;

import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


import com.google.appengine.api.blobstore.BlobKey;

public class ImageCreator {
	static enum FILETYPE { JPG, GIF, PNG }
	Entity imageDetails;


	public ImageCreator(User user) {
		imageDetails = new Entity("image");
		imageDetails.setProperty("user",user);
	}

	public void AddAuthor(String author) {
		imageDetails.setProperty("author",author);
	}

	public void AddSubject(String subject) {
		imageDetails.setProperty("subject",subject);
	}

	public void AddComment(Text comment) {
		imageDetails.setProperty("comment",comment);
	}

	public void AddDate(Date date) {
		imageDetails.setProperty("date",date);
	}

	public void AddTag(String tag) {
		imageDetails.setProperty("tag",tag);
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

	public Entity getImageEntity() {
		return imageDetails;
	}


	public void AddBlobKey(BlobKey blobKey) {
		imageDetails.setProperty("blobkey",blobKey);
	}

}
