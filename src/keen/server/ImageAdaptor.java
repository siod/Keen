package keen.server;

//import java.util.List;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Date;

import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


import com.google.appengine.api.blobstore.BlobKey;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

public class ImageAdaptor {
	static enum FILETYPE { JPG, GIF, PNG }
	static int THUMBNAIL_LENGTH = 200;
	static int THUMBNAIL_WIDTH = 200;
	Entity _image;


	public ImageAdaptor(Entity image) {
		_image = image;
	}

	public String getUser() {
		if (_image.getProperty("user") == null) return "unkown";
		return ((User) _image.getProperty("user")).getNickname();
	}

	public String GetAuthor() {
		return (String) _image.getProperty("author");
	}

	public String GetSubject() {
		return (String)_image.getProperty("subject");
	}

	public String GetComment() {
		return ((Text)_image.getProperty("comment")).getValue();
	}

	public Date GetDate() {
		return (Date)_image.getProperty("date");
	}

	public String GetTag() {
		return (String)_image.getProperty("tag");
	}

	public ImageCreator.FILETYPE GetFileType() {
		return (ImageCreator.FILETYPE) _image.getProperty("filetype");
	}

	public Rating GetRating() {
		return (Rating) _image.getProperty("rating");
	}

	public Entity getImageEntity() {
		return _image;
	}


	public BlobKey GetBlobKey() {
		return (BlobKey)_image.getProperty("blobkey");
	}

	public byte[] getThumbnail() {
		ImagesService is = ImagesServiceFactory.getImagesService();
		Image oldImage = ImagesServiceFactory.makeImageFromBlob(GetBlobKey());
		Transform resize = ImagesServiceFactory.makeResize(THUMBNAIL_WIDTH,THUMBNAIL_LENGTH);
		Image newImage = is.applyTransform(resize, oldImage);
		return newImage.getImageData();

	}
}
