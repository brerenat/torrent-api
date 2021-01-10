package brere.nat.torrent.api.utils;

public class TorrentAPIException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7350655110653418940L;
	
	public TorrentAPIException(final String message) {
		super(message);
	}
	
	public TorrentAPIException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
