package keen.shared;

import javax.persistence.Id;
import java.util.List;
import java.util.ArrayList;

import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.blobstore.BlobKey;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.*;

public class Music {

	@Id Long id;
	public String owner;
	public int length;
	public String songName;
	public String artist;
	public String genre;
	@Unindexed public BlobKey data;
	@Unindexed public BlobKey albumArt;
	@NotSaved(IfDefault.class) public Rating rating = null;
	public List<String> tags = new ArrayList<String>();
	public int trackNum;
	public int discNum;
	@NotSaved(IfDefault.class) public Text comment = null;
	@Unindexed(IfFalse.class) public boolean toDelete;


	public Music() {

	}

	public Music(String owner, int length, String songName, 
				String artist, String genre, BlobKey data,
				BlobKey albumArt, Rating rating, String[] tags,int trackNum,
				int discNum, Text comment) {
		this.owner = owner;
		this.length = length;
		this.songName = songName;
		this.artist = artist;
		this.genre = genre;
		this.data = data;
		this.albumArt = albumArt;
		this.rating = rating;
		if (tags != null) {
			for( String s : tags) {
				this.tags.add(s);
			}
		}
		this.trackNum = trackNum;
		this.discNum = discNum;
		this.comment = comment;
		toDelete = false;

	}
				
	
}
