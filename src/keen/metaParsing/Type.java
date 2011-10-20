package keen.metaParsing;

import java.util.Set;
import java.util.HashSet;

public class Type {

	public enum TYPE { INVALID,MUSIC,IMAGE, VIDEO };

	private static Type instance;
	private Set<String> musicMimes;
	//private Set<String> videoMimes;
	//private Set<String> imageMimes;

	protected Type() {
		musicMimes = new HashSet<String>();
		musicMimes.add("audio/mpeg");
		musicMimes.add("audio/x-mpeg");
		musicMimes.add("audio/mp3");
		musicMimes.add("audio/x-mp3");
		musicMimes.add("audio/mpeg3");
		musicMimes.add("audio/x-mpeg3");
		musicMimes.add("audio/mpg");
		musicMimes.add("audio/x-mpg");
		musicMimes.add("audio/x-mpegaudio");

		// Currently only supporting a very limited subset of videos
		//videoMimes = new HashSet<String>();
		//videoMimes.add("video/mp4");

		//currently not tightly controlling Image types
		//imageMimes = new HashSet<String>();

	}


	public static Type getInstance() {
		if (instance == null)
			instance = new Type();
		return instance;
	}

	public TYPE parseType(String mimeType) {
		if (isMusic(mimeType))
			return TYPE.MUSIC;
		if (isVideo(mimeType))
			return TYPE.VIDEO;
		if (isImage(mimeType))
			return TYPE.IMAGE;
		return TYPE.INVALID;
	}


	boolean isVideo(String mimetype) {
		return mimetype.equals("video/mp4");
		//return videoMimes.contains(mimeType);
	}
	boolean isMusic(String mimetype) {
		return musicMimes.contains(mimetype);
	}

	boolean isImage(String mimetype) {
		return mimetype.contains("image/");
	}

}
