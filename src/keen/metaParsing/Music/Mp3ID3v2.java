package keen.metaParsing.Music;
import keen.metaParsing.ChainedBlobstoreInputStream;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.lang.NullPointerException;

public class Mp3ID3v2 implements MusicParser {

	private boolean err;
	private int paddingValue;
	private int remAttribNum;
	private int version;
	private Map<String,byte[]> attribs;


	public Mp3ID3v2(ChainedBlobstoreInputStream tag) throws IOException {
		// check if id3
		byte[] header = new byte[10];
		if (tag.read(header) != 10) {
			err = true;
			return;
		}

		// check for "ID3" and version number
		if (!((int)(header[0] & 0xFF) == (int)((0x49) & 0xFF)
			&& (int)(header[1] & 0xFF) == (int)((0x44) & 0xFF)
			&& (int)(header[2] & 0xFF) == (int)((0x33) & 0xFF))) {
			err = true;
			return;
		}

		version = checkVersion(header,3);

		switch (version) {

			case 3:
				paddingValue = 8;
				break;
			case 4:
				paddingValue = 7;

			default:
				// error unsupported version
				err = true;
				return;
		}

		checkHeaderFlags(header,5);
		int tagSize = getSize(header,6);
		// attributes to parse
		attribs = new HashMap<String,byte[]>();
		// Album
		attribs.put("TALB",null);
		// Title
		attribs.put("TIT2",null);
		// Genre
		attribs.put("TCON",null);
		// Artist
		attribs.put("TPE1",null);
		// Track Number
		attribs.put("TRCK",null);
		// Disc Number
		attribs.put("TPA",null);
		// Rating
		attribs.put("POPM",null);
		// Length
		//attribs.put("TLEN",null);
		remAttribNum = attribs.size();

		while (remAttribNum != 0 && tag.getOffset() < tagSize) {
			parseFrame(tag);
		}
		tag.close();
		err = false;

	}

	private int checkVersion(byte[] header, int offset) {
		int Majorversion = (int)(header[offset] & 0xFF);
		// minor version isn't currently used
		//int MinorVersion = (int)(header[offset + 1] & 0xFF);
		return Majorversion;
	}
	private void checkHeaderFlags(byte[] header, int offset) {

	}
	private void checkFrameFlags(byte[] header) {

	}

	private int getSize(byte[] header, int offset) {
		return (((int)(header[offset] & 0xFF) << 21) 
				| ((int)(header[offset + 1] & 0xFF) << 14) 
				| ((int)(header[offset + 2] & 0xFF) << 7) 
				| (int)(header[offset+3] & 0xFF));
	}

	private void parseFrame(ChainedBlobstoreInputStream tag) throws IOException {
		//----- Begin frame header reading
		byte[] buffer = new byte[4];
		tag.read(buffer);
		
		String frameID = new String(buffer);
		// Reading padding bytes,
		// spec requires that there is no padding between frames so it is safe to exit.
		if (frameID == "") {
			remAttribNum = 0;
			return;
		}

		tag.read(buffer);

		//HACK
		// this is unsigned. and java has no unsigned int
		long size = (((int)(buffer[0] & 0xFF) << paddingValue*3) 
					| ((int)(buffer[1] & 0xFF) << paddingValue*2) 
					| ((int)(buffer[2] & 0xFF) << paddingValue) 
					| ((int)(buffer[3] & 0xFF)));

		// may not use
		byte[] flags = new byte[2];
		tag.read(flags);
		checkFrameFlags(flags);
		//----- End frame header reading

		if (attribs.containsKey(frameID)) {
			--remAttribNum;
			buffer = new byte[(int)(size)];
			tag.read(buffer);
			attribs.put(frameID,buffer);
			// do something
		} else {
			// not needed
			tag.skip(size);
		}
	}


	public boolean success() {
		return !err;
	}

	// Note all text incoded strings start with a byte to indicate charset
	// 0x00 = ISO-8859-1
	// 0x0.1 = Unicode 2.0 these strings also start with a BOM to indicate byte order 
	// LE? 0xFF 0xFE
	// BE? 0xFE 0xFF

	public String getAlbum() {
		try {
			return new String(attribs.get("TALB"),1,attribs.get("TALB").length -1);
		} catch (NullPointerException e) {
			return "";
		}
	}

	public int getRating(){
		try {
			// POPM consits of a null terminated <email string> a rating byte and a optional 
			// counter bytes
			int i = 0;
			byte[] data;
			data = attribs.get("POPM");
			// don't care about the email string
			while (data[i++] != 0x00);
			// return the unsigned counter byte
			return (int)(data[i] & 0xFF);
		} catch (NullPointerException e) {
			return 0;
		}

	}

	public String getSongName() {
		try {
			return new String(attribs.get("TIT2"),1,attribs.get("TIT2").length -1);
		} catch (NullPointerException e) {
			return "";
		}

	}

	public String getGenre() {
		try {
			return new String(attribs.get("TCON"),1,attribs.get("TCON").length -1);
		} catch (NullPointerException e) {
			return "";
		}

	}


	public String getArtist() {
		try {
			return new String(attribs.get("TPE1"),1,attribs.get("TPE1").length -1);
		} catch (NullPointerException e) {
			return "";
		}
	}

	public String getTrackNum() {
		try {
			return new String(attribs.get("TRCK"),1,attribs.get("TRCK").length -1);
		} catch (NullPointerException e) {
			return "";
		}

	}

	public String getDiscNum() {
		try {
			return new String(attribs.get("TPA"),1,attribs.get("TPA").length -1);
		} catch (NullPointerException e) {
			return "";
		}
	}


	/*
	public String getLength() {
		return new String(attribs.get("TLEN"));
	}
	*/

}
