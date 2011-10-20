package keen.server;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class Serve extends HttpServlet {
	private BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
		try {
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		blobServ.serve(blobKey, res);
		} catch (Exception e) {
			// don't do anything
		}
	}
}

