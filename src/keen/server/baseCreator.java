
package keen.server;

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

public class baseCreator {
	protected Entity baseDetails;


	protected baseCreator(User user,String type) {
		baseDetails = new Entity(type);
		baseDetails.setProperty("user",user);
	}

	protected void AddAuthor(String author) {
		baseDetails.setProperty("author",author);
	}

	protected void AddComment(Text comment) {
		baseDetails.setProperty("comment",comment);
	}

	protected void AddDate(Date date) {
		baseDetails.setProperty("date",date);
	}

	protected void AddTag(String tag) {
		baseDetails.setProperty("tag",tag);
	}

	protected void AddFileType(String fileType) {
		baseDetails.setProperty("filetype",fileType);
	}

	protected void AddRating(String srating) {
		int rating = Integer.parseInt(srating);
		if (rating > Rating.MAX_VALUE || rating < Rating.MIN_VALUE)
			// error
			return;

		baseDetails.setProperty("rating",new Rating(rating));
	}

	protected Entity getbaseEntity() {
		return baseDetails;
	}


	protected void AddBlobKey(BlobKey blobKey) {
		baseDetails.setProperty("blobkey",blobKey);
	}

}
