package keen.shared;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Id;

import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.blobstore.BlobKey;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.*;

public class Video {

	@Id public Long id;
	public String owner;
	@Unindexed public int length;
	public String title;
	public String director;
	public List<String> actors = new ArrayList<String>();
	public List<String> tags = new ArrayList<String>();
	@Unindexed public BlobKey data;
	@Unindexed public BlobKey artData;
	@NotSaved(IfDefault.class) public Rating rating = null;
	@NotSaved(IfDefault.class) public Text comment = null;


	private Video() {

	}

	public Video(String owner,int length,
			String title, String director,
			String[] actors,String[] tags,
			BlobKey data, BlobKey boxArt,
			Rating rating, Text comment) {
		this.owner = owner;
		this.title = title;
		this.length = length;
		this.director = director;
		if (tags != null)
			for( String s : tags)
				this.tags.add(s);
		if (actors != null)
			for( String s : actors)
			this.actors.add(s);
		this.data = data;
		this.artData = boxArt;
		this.rating = rating;
		this.comment = comment;
	}


}
