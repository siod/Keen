package keen.shared;

import javax.persistence.Id;
import java.util.List;
import java.util.ArrayList;

import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.blobstore.BlobKey;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.*;

public class Video {

	@Id Long id;
	String owner;
	int length;
	String title;
	List<String> actors;
	String director;
	@Unindexed BlobKey data;
	@Unindexed BlobKey boxArt;
	@NotSaved(IfDefault.class) Rating rating = null;
	@Unindexed String filetype;
	@NotSaved(IfDefault.class) Text comment = null;


	private Video() {

	}

	public Video(String owner,String title,
			List<String>actors,String director,
			BlobKey data,BlobKey boxArt,
			Rating rating, String filetype,
			Text comment) {
		this.owner = owner;
		this.title = title;
		this.actors = new ArrayList<String>();
		for(String s: actors)
			this.actors.add(s);
		this.director = director;
		this.data = data;
		this.boxArt = boxArt;
		this.rating = rating;
		this.filetype = filetype;
		this.comment = comment;
	}


}
