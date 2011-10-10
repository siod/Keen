package keen.metaParsing;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * ChainedBlobstoreInputStream works exactly like BlobstoreInputStream but does
 * not throw an error if more than 1mb is read.
 * 
 * @author Richard Wallis, SiOD
 */
public class ChainedBlobstoreInputStream extends InputStream {
    /**
     * The maximum number of bytes that can be read by a single request.
     */
    private static final int MAX_READSIZE = 1015800;

    /**
     * The BlobKey of the blobstore item.
     */
    private BlobKey blobKey;
    /**
     * The current byte position of the reader.
     */
    private long offset;

    /**
     * The Total Size of the blob.
     */
    private long totalSize;
    /**
     * The current Input Stream being read.
     */
    private BlobstoreInputStream currentStream;
    /**
     * The next point at which a new InputStream will need to be initialized.
     */
    private long nextReadBreak;
    /**
     * The currentStream at the time of the last mark.
     */
    private BlobstoreInputStream markedStream;

    /**
     * Creates a new ChainedBlobstoreInputStream. This stream should behave
     * exactly the same as a BlobstoreInputStream and it should be possible to
     * interchange them.
     * 
     * @param theBlobKey
     *            - The blobkey of the object to be read.
     * @throws IOException
     *             - Thrown if there is an error reading the current stream.
     */
    public ChainedBlobstoreInputStream(final BlobKey theBlobKey)
            throws IOException {
        this(theBlobKey, 0);
    }

    /**
     * Creates a new ChainedBlobstoreInputStream. This stream should behave
     * exactly the same as a BlobstoreInputStream and it should be possible to
     * interchange them.
     * 
     * @param theBlobKey
     *            - The blobkey of the object to be read.
     * @param newOffset
     *            - The offset in the blob from where to read.
     * @throws IOException
     *             - Thrown if there is an error reading the current stream.
     */
    public ChainedBlobstoreInputStream(final BlobKey theBlobKey,
            final long newOffset) throws IOException {
        this.offset = newOffset;
        this.blobKey = theBlobKey;
        final BlobInfo blobInfo = 
            new BlobInfoFactory().loadBlobInfo(this.blobKey);
        this.totalSize = blobInfo.getSize();
        this.currentStream = 
            new BlobstoreInputStream(this.blobKey, this.offset);
        this.nextReadBreak = 
            this.offset + ChainedBlobstoreInputStream.MAX_READSIZE;
    }

	public long getOffset() {
		return offset;
	}

    /*
     * (non-javadoc)
     * @see java.io.inputstream#read()
     */
    @Override
    public final int read() throws IOException {
        if (this.offset < this.totalSize) {
            if (this.offset == this.nextReadBreak) {
                this.currentStream.close();
                this.currentStream = 
                    new BlobstoreInputStream(this.blobKey, this.offset);
                this.nextReadBreak = this.offset 
                + ChainedBlobstoreInputStream.MAX_READSIZE;
            }
            this.offset += 1;
            return this.currentStream.read();
        } else {
            this.currentStream.close();
            return -1;
        }
    }

    /*
     * (non-javadoc)
     * @see java.io.inputstream#read()
     */
    @Override
    public final int read(byte[] output) throws IOException {
        if (this.offset + output.length <= this.totalSize) {
			long read = 0;
			while (read != output.length && !(this.offset >= this.totalSize)) {
				if (this.offset + (output.length - read) >= this.nextReadBreak) {
					int amountToRead = (int)(this.nextReadBreak - this.offset);
					read += this.currentStream.read(output,0,amountToRead);
					this.currentStream.close();
					this.currentStream = 
						new BlobstoreInputStream(this.blobKey, this.offset);
					this.offset += amountToRead;
				} else {
					this.offset += (output.length - read);
					read += this.currentStream.read(output,0,(int)(output.length - read));
				}

			}
            return (int)(read);
        } else {
            this.currentStream.close();
            return -1;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    @Override
    public final long skip(long n) throws IOException {
        if (this.offset + n <= this.totalSize) {
		long skipped = 0;
			while (skipped != n && !(this.offset >= this.totalSize)) {
				if (this.offset + (n - skipped) >= this.nextReadBreak) {
					long amountToRead = this.nextReadBreak - this.offset;
					skipped += this.currentStream.skip(amountToRead);
					this.currentStream.close();
					this.currentStream = 
						new BlobstoreInputStream(this.blobKey, this.offset);
					this.offset += amountToRead;
				} else {
					this.offset += (n - skipped);
					skipped += this.currentStream.skip(n - skipped);
				}

			}
			return skipped;
		} else {
            this.currentStream.close();
            return -1;
        }

    }

    /*
     * (non-Javadoc)
     * @see java.io.InputStream#close()
     */
    @Override
    public final void close() throws IOException {
        this.currentStream.close();
        super.close();
    }

    /*
     * (non-Javadoc)
     * @see java.io.InputStream#mark(int)
     */
    @Override
    public final void mark(final int readlimit) {
        this.currentStream.mark(readlimit);
        this.markedStream = this.currentStream;
    }

    /*
     * (non-Javadoc)
     * @see java.io.InputStream#markSupported()
     */
    @Override
    public final boolean markSupported() {
        return this.currentStream.markSupported();
    }

    /*
     * (non-Javadoc)
     * @see java.io.InputStream#reset()
     */
    @Override
    public final void reset() throws IOException {
        this.currentStream = this.markedStream;
        this.currentStream.reset();
    }

}


