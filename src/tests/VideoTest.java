package tests;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import keen.server.VideoServlet;
import keen.shared.Video;

import org.junit.Test;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;

public class VideoTest {

	String[] actors = {"Tom Green", "Tom Woods", "Tom Tomason"};
	String[] tags = {"Funny", "Comedy", "Tiring"};
	BlobKey blob = new BlobKey("data");
	BlobKey artBlob = new BlobKey("art");
	Rating rating = new Rating(1);
	Text text;
	HttpServletRequest req;
	HttpServletResponse res;
	VideoServlet videoServe;
	Video theVideo = new Video("Joe", "Lord of the Rings", "Ronald Reegan", actors, tags, blob, rating, text);	
	
	
	@Test
	public void testVideoServlet(){
		videoServe = new VideoServlet();
		assertTrue(videoServe!=null);
	}
	
	@Test
	public void videoOwnerConstructionTest() {
		
		assertEquals(theVideo.owner, "Joe");
		
	}
		
	
	@Test
	public void videoTitleConstructionTest() {
		assertEquals(theVideo.title, "Lord of the Rings");
		
	}
	
	@Test
	public void videoDirectorConstructionTest() {
		assertEquals(theVideo.director, "Ronald Reegan");
	}
	
	@Test
	public void videoDataConstructionTest() {
		assertEquals(theVideo.data, blob);
		
	}
	
	
	@Test
	public void videoRatingConstructionTest() {
		assertEquals(theVideo.rating, rating);
	}
	
	@Test
	public void videoCommentConstructionTest() {
		assertEquals(theVideo.comment, text);
		
	}
}
