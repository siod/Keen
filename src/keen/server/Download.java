package keen.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Download extends HttpServlet {
	private BlobstoreService blobServ = BlobstoreServiceFactory.getBlobstoreService();

	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
		try {
			BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
			String filename = req.getParameter("filename");
			res.setHeader("Content-Disposition","attachment;filename="+ filename);
			blobServ.serve(blobKey, res);
		} catch (Exception e) {
			//Don't do anything
		}

	}
}


