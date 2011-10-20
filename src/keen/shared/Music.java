package keen.shared;

import javax.persistence.Id;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Id;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.blobstore.BlobKey;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.*;

public class Music {

	@Id public Long id;
	public String owner;
	public String songName;
	public String album;
	public String artist;
	public String genre;
	@Unindexed public BlobKey data;
	@Unindexed public BlobKey artData;
	@NotSaved(IfDefault.class) public Rating rating = null;
	public List<String> tags = new ArrayList<String>();
	public String trackNum;
	public String discNum;
	@NotSaved(IfDefault.class) public Text comment = null;


	public Music() {

	}

	public Music(String owner, String songName, String album,
				String artist, String genre, BlobKey data,
				BlobKey albumArt, Rating rating, String[] tags,String trackNum,
				String discNum, Text comment) {
		this.owner = owner;
		this.songName = songName;
		this.album = album;
		this.artist = artist;
		this.genre = genre;
		this.data = data;
		this.artData = albumArt;
		this.rating = rating;
		if (tags != null) {
			for( String s : tags) {
				this.tags.add(s);
			}
		}
		this.trackNum = trackNum;
		this.discNum = discNum;
		this.comment = comment;
	}
				
	
}
