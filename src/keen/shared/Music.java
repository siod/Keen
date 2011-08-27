package keen.shared;

import javax.persistence.Id;

import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.blobstore.BlobKey;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.*;

public class Music {

	@Id Long id;
	String owner;
	int length;
	String songName;
	String artist;
	String genre;
	@Unindexed BlobKey data;
	@Unindexed BlobKey albumArt;
	@NotSaved(IfDefault.class) Rating rating = null;
	@Unindexed String filetype;
	int trackNum;
	int discNum;
	@NotSaved(IfDefault.class) Text comment = null;
	
}
