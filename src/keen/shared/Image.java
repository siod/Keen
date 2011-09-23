package keen.shared;


import java.util.Date;
import java.util.List;
import java.util.ArrayList;


import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.blobstore.BlobKey;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.*;

@Subclass
public class Image extends Media {

	public String title;
	public String artist;
	@Unindexed public BlobKey data;
	@NotSaved(IfDefault.class) public Rating rating = null;
	public List<String> tags = new ArrayList<String>();
	@NotSaved(IfDefault.class) public Text comment = null;
	public Date date;


	private Image() {

	}

	public Image(String owner, String title,
			String artist,BlobKey data,
			Rating rating,String[] tags,
			Text comment,Date date) {
		this.owner = owner;
		this.title = title;
		this.artist = artist;
		this.data = data;
		this.rating = rating;
		if (tags != null) {
			for( String s : tags) {
				this.tags.add(s);
			}
		}
		this.comment = comment;
		this.date = date;
		toDelete = false;


	}

}
