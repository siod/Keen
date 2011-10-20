package tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import keen.server.ImageServlet;
import keen.shared.Image;

import org.junit.*;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;

public class ImageTest {
	
	private Text text;
	private Date date;
	private Rating rating = new Rating(5);
	private BlobKey blob = new BlobKey("Blob");
	private String[] array = {"tag1", "tag2"};
	private Image theImage = new Image("Joe", "CoolPic", blob, rating, array, text, date);
	private ImageServlet imageServe;

	@Test
	public void testImageServlet(){
		imageServe = new ImageServlet();
		assertTrue(imageServe!=null);
	}
	
	
	@Test
	public void imageOwnerConstructionTest() {
		
		assertEquals(theImage.owner, "Joe");
	}
	
	@Test
	public void imageTitleConstructionTest() {
		assertEquals(theImage.title, "CoolPic");
		
	}
	
	@Test
	public void imageCommentConstructionTest() {
		assertEquals(theImage.comment, text);
		
	}
	
	@Test
	public void imageDateConstructionTest() {
		assertEquals(theImage.date, date);
	
	}
	
	@Test
	public void imageTagConstructionTest() {
		assertEquals(theImage.tags, Arrays.asList(array));	
	}
	
	@Test
	public void imageRatingConstructionTest() {
		assertEquals(theImage.rating, rating);
		
	}
	
	@Test
	public void imageDataConstructionTest() {
		assertEquals(theImage.data, blob);
	
	}
	
}



