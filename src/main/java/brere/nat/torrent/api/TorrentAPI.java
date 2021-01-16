package brere.nat.torrent.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import brere.nat.torrent.api.model.TorrentResult;
import brere.nat.torrent.api.model.TorrentResults;
import brere.nat.torrent.api.utils.TorrentAPIException;
import brere.nat.torrent.api.utils.TorrentAPIUtils;

public class TorrentAPI extends TorrentAPIUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(TorrentAPI.class);
	private int minSeeders = 0;

	/**
	 * 
	 */
	public TorrentAPI() {
		super();
	}

	/**
	 * 
	 * @param httpClientBuilder
	 */
	public TorrentAPI(final HttpClientBuilder httpClientBuilder) {
		super(httpClientBuilder);
	}

	/**
	 * 
	 * @param appID
	 */
	public TorrentAPI(final String appID) {
		super(appID);
	}
	
	public TorrentAPI(final String appID, final int maxRetries) {
		super(appID, maxRetries);
	}

	/**
	 * 
	 * @param appID
	 * @param httpClientBuilder
	 */
	public TorrentAPI(final String appID, final HttpClientBuilder httpClientBuilder) {
		super(appID, httpClientBuilder);
	}

	public int getMinSeeders() {
		return minSeeders;
	}

	public void setMinSeeders(int minSeeders) {
		this.minSeeders = minSeeders;
	}

	/**
	 * 
	 * @param params
	 * @return
	 * @throws TorrentAPIException
	 */
	private List<TorrentResult> getTorrentResults(final List<NameValuePair> params) throws TorrentAPIException {
		try {
			final URI uri = getURI(params);
			
			LOG.info("Getting Torrent Results");
			final TorrentResults results = getObject(uri, TorrentResults.class);
			LOG.info("Got Torrent Results");
			
			return results.getTorrentResults();
			
		} catch (URISyntaxException e) {
			throw new TorrentAPIException("Error when creating URI to search for Torrents", e);
		}
	}
	
	/**
	 * 
	 * @param imdbID
	 * @return
	 * @throws TorrentAPIException
	 */
	public List<TorrentResult> getTorrentByIMDBID(final String imdbID) throws TorrentAPIException {
		LOG.info("Getting Token");
		checkToken();
		LOG.info("Got Token");
		
		final List<NameValuePair> params = getRequestParams();
		params.add(new BasicNameValuePair("search_imdb", imdbID));
		setMinSeeders(params);
		return getTorrentResults(params);
	}
	
	/**
	 * 
	 * @param imdbID
	 * @param search
	 * @return
	 * @throws TorrentAPIException
	 */
	public List<TorrentResult> getTorrentByIMDBIDAndSearch(final String imdbID, final String search) throws TorrentAPIException {
		LOG.info("Getting Token");
		checkToken();
		LOG.info("Got Token");
		
		final List<NameValuePair> params = getRequestParams();
		params.add(new BasicNameValuePair("search_imdb", imdbID));
		params.add(new BasicNameValuePair("search_string", search));
		setMinSeeders(params);
		return getTorrentResults(params);
	}
	
	/**
	 * 
	 * @param search
	 * @return
	 * @throws TorrentAPIException
	 */
	public List<TorrentResult> getTorrentBySearch(final String search) throws TorrentAPIException {
		LOG.info("Getting Token");
		checkToken();
		LOG.info("Got Token");
		
		final List<NameValuePair> params = getRequestParams();
		params.add(new BasicNameValuePair("search_string", search));
		setMinSeeders(params);
		return getTorrentResults(params);
	}
	
	private void setMinSeeders(final List<NameValuePair> params) {
		if (minSeeders > 0) {
			params.add(new BasicNameValuePair("min_seeders", Integer.toString(minSeeders)));
		}
	}

}
