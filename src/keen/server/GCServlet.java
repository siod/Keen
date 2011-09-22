package keen.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;




public class GCServlet extends HttpServlet {
	public static final Logger log = Logger.getLogger(Upload.class.getName());

	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		if (req.getHeader("X-AppEngine-Cron") == "true") {
			log.info("running Cron GC sweep");
			GarbageCollector.getGarbageCollector().Sweep();
		} else {
			log.severe("UnAuthorized GC sweep");
		}
	}

}
