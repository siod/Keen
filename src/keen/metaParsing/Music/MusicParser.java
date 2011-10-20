package keen.metaParsing.Music;



public interface MusicParser {

	public boolean success();

	public String getAlbum();

	public String getSongName();

	public String getGenre();

	public String getArtist();

	public String getTrackNum();

	public String getDiscNum();

	public int getRating();

	/*
	public String getLength();
	*/

}
