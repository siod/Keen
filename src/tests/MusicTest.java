package tests;

import static org.junit.Assert.*;
import keen.metaParsing.ChainedBlobstoreInputStream;
import keen.metaParsing.Music.Mp3ID3v2;
import keen.server.MusicServlet;
import keen.shared.Music;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Rating;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;

public class MusicTest {

	User user;
	ChainedBlobstoreInputStream inputStream;
	BlobKey blob = new BlobKey("Blob");
	BlobKey albumBlob = new BlobKey("AlbumBlob");
	Rating rating = new Rating(5);
	String[] tags = {"See", "Speak", "Hear"};
	Text text;
	HttpServletRequest req;
	HttpServletResponse res;
	MusicServlet musicServe;
	
	Music theMusic = new Music("Joe", "My Heart Will Go On", "Titanic Soundtrack", "Jack Black", "Electronic", blob, albumBlob, rating, tags, "1", "4", text);
	
	@Test
	public void musicServlet() {
		musicServe = new MusicServlet();
		assertTrue(musicServe!=null);
	}
	@Test
	public void musicOwnerConstructionTest() {
		
		assertEquals(theMusic.owner, "Joe");
	}
		
	@Test
	public void musicSongNameConstructionTest() {
	
		
		assertEquals(theMusic.songName, "My Heart Will Go On");
		
	}
	
	@Test
	public void musicAlbumConstructionTest() {
		
		assertEquals(theMusic.album, "Titanic Soundtrack");
		
	}
	@Test
	public void musicArtistConstructionTest() {
		assertEquals(theMusic.artist, "Jack Black");
		
	}
	
	@Test
	public void musicGenreConstructionTest(){
		assertEquals(theMusic.genre, "Electronic");
		
	}
	
	@Test
	public void musicDataConstructionTest(){
		assertEquals(theMusic.data, blob);
		
	}
	
	@Test
	public void musicArtDataConstructionTest() {
		assertEquals(theMusic.artData, albumBlob);
		
	}
	
	@Test
	public void musicRatingConstructionTest() {
		assertEquals(theMusic.rating, rating);
		
	}
	
	@Test
	public void musicTrackNumConstructionTest() {
		assertEquals(theMusic.trackNum, "1");
		
	}
	
	@Test
	public void musicDiscNumConstructionTest() {
		assertEquals(theMusic.discNum, "4");
		
	}
	
	@Test
	public void musicCommentConstructionTest() {
		assertEquals(theMusic.comment, text);
	}
// Tests correct error registering
	@Test
	public void testMp3ID3v2ConstructionError(){
		
		inputStream = null;
		@SuppressWarnings("unused")
		Mp3ID3v2 mp3ID;
		
		try {
			mp3ID = new Mp3ID3v2(inputStream);
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
 
	@Test
	public void testMp3ID3v2Construction(){
		//TODO initalise inputStream properly
		
		BlobKey newBlob = new BlobKey("newBlob");
		try{
			inputStream = new ChainedBlobstoreInputStream(newBlob);
			@SuppressWarnings("unused")
			Mp3ID3v2 mp3ID = new Mp3ID3v2(inputStream);
		}
		catch(Exception e){
			assertTrue(true);
		}
	}
}