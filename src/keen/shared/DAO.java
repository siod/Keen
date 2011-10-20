package keen.shared;
import com.googlecode.objectify.util.DAOBase;
import com.googlecode.objectify.ObjectifyService;

public class DAO extends DAOBase {
	static {
		ObjectifyService.register(Image.class);
		ObjectifyService.register(Music.class);
		ObjectifyService.register(Video.class);
	}

}
