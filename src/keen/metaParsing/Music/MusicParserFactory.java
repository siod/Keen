package keen.metaParsing.Music;

import java.util.Set;
import java.util.HashSet;
import com.google.appengine.api.blobstore.BlobKey;
import keen.metaParsing.ChainedBlobstoreInputStream;

import java.io.IOException;
public class MusicParserFactory {
	private Set<String> mp3Mimes;
	private static MusicParserFactory instance;

	protected MusicParserFactory() {
		mp3Mimes = new HashSet<String>();
		mp3Mimes.add("audio/mpeg");
		mp3Mimes.add("audio/x-mpeg");
		mp3Mimes.add("audio/mp3");
		mp3Mimes.add("audio/x-mp3");
		mp3Mimes.add("audio/mpeg3");
		mp3Mimes.add("audio/x-mpeg3");
		mp3Mimes.add("audio/mpg");
		mp3Mimes.add("audio/x-mpg");
		mp3Mimes.add("audio/x-mpegaudio");

	}

	public static MusicParserFactory getInstance() {
		if (instance == null)
			instance = new MusicParserFactory();
		return instance;
	}

	public MusicParser getMusicParser(BlobKey blobkey,String mimeType) throws IOException {
		if (mp3Mimes.contains(mimeType))
			return new Mp3ID3v2(new ChainedBlobstoreInputStream(blobkey));
		else {
			return null;
		}
	}


}
