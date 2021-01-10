package brere.nat.torrent.api.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import brere.nat.torrent.api.model.utils.APIError;
import brere.nat.torrent.api.model.utils.Token;

public abstract class TorrentAPIUtils {

	private static final Logger LOG = LoggerFactory.getLogger(TorrentAPIUtils.class);
	
	public static final String DEFAULT_APPID = "default";
	public static final String DEFAULT_SORT = "seeders";
	public static final String DEFAULT_MODE = "search";
	public static final String DEFAULT_CATEGORY = "tv";
	public static final String BASE_URL = "https://torrentapi.org/pubapi_v2.php";
	protected static final String FORMAT = "json_extended";
	protected static final String GET_TOKEN = "get_token";
	protected ObjectMapper objMapper;
	protected static final long WAIT_TIMER = 2000L;
	protected int maxRetries = 5;
	
	private static final int TOKEN_EXPIRY = 600000;
	
	protected HttpClientBuilder httpClientBuilder = null;
	protected String appID = DEFAULT_APPID;
	protected String sort = DEFAULT_SORT;
	protected String mode = DEFAULT_MODE;
	protected String category = DEFAULT_CATEGORY;
	protected int requestCounter = 0;
	
	private Token token;
	private boolean error = false;
	
	public TorrentAPIUtils() {
		super();
		setUpObjectMapper();
	}
	
	public TorrentAPIUtils(int maxRetries) {
		super();
		this.maxRetries = maxRetries;
		setUpObjectMapper();
	}

	public TorrentAPIUtils(final HttpClientBuilder httpClientBuilder) {
		super();
		this.httpClientBuilder = httpClientBuilder;
		setUpObjectMapper();
	}
	
	public TorrentAPIUtils(final HttpClientBuilder httpClientBuilder, final int maxRetries) {
		super();
		this.httpClientBuilder = httpClientBuilder;
		this.maxRetries = maxRetries;
		setUpObjectMapper();
	}

	public TorrentAPIUtils(final String appID) {
		super();
		this.appID = appID;
		setUpObjectMapper();
	}
	
	public TorrentAPIUtils(final String appID, final int maxRetries) {
		super();
		this.appID = appID;
		this.maxRetries = maxRetries;
		setUpObjectMapper();
	}

	public TorrentAPIUtils(final String appID, final HttpClientBuilder httpClientBuilder) {
		super();
		this.appID = appID;
		this.httpClientBuilder = httpClientBuilder;
		setUpObjectMapper();
	}
	
	public TorrentAPIUtils(final String appID, final HttpClientBuilder httpClientBuilder, final int maxRetries) {
		super();
		this.appID = appID;
		this.httpClientBuilder = httpClientBuilder;
		this.maxRetries = maxRetries;
		setUpObjectMapper();
	}
	
	private void setUpObjectMapper() {
		objMapper = new ObjectMapper();
	}
	
	/**
	 * 
	 * @return
	 */
	protected HttpClient getHttpClient() {
		if (httpClientBuilder == null) {
			httpClientBuilder = HttpClientBuilder.create();
		}
		return httpClientBuilder.build();
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	protected URI getURI(final List<NameValuePair> params) throws URISyntaxException {
		return new URIBuilder(BASE_URL).addParameters(params).build();
	}
	
	/**
	 * 
	 * @return a {@link List} of {@link NameValuePair} objects that represent the Parameters that are required to get the Token
	 */
	private List<NameValuePair> getTokenParams() {
		final List<NameValuePair> params = getBaseParams();
		params.add(new BasicNameValuePair(GET_TOKEN, GET_TOKEN));
		return params;
	}

	/**
	 * 
	 * @return a {@link List} of {@link NameValuePair} objects that represent the Parameters that are always required by the API
	 */
	private List<NameValuePair> getBaseParams() {
		final List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("app_id", appID));
		return params;
	}
	
	/**
	 * 
	 * @return a {@link List} of {@link NameValuePair} objects that represent the Parameters once a token has been obtained
	 */
	protected List<NameValuePair> getRequestParams() {
		final List<NameValuePair> params = getBaseParams();
		params.add(new BasicNameValuePair("token", token.getToken()));
		params.add(new BasicNameValuePair("mode", mode));
		params.add(new BasicNameValuePair("category", category));
		params.add(new BasicNameValuePair("sort", sort));
		params.add(new BasicNameValuePair("format", FORMAT));
		return params;
	}
	
	private void setUpHttpRequestBase(final HttpRequestBase req) {
		req.setHeader("Accept", MediaType.APPLICATION_JSON);
		req.setHeader("Connection", "keep-alive");
		req.setHeader("User-Agent", "Apache HTTP Client");
	}

	/**
	 * 
	 * @param <T>
	 * @param bytes
	 * @param clazz
	 * @return
	 * @throws TorrentAPIException 
	 * @throws IOException
	 */
	private <T> T getObjectFromResult(final byte[] bytes, final Class<T> clazz) throws TorrentAPIException {
		T item;
		try {
			String jsonString = new String(bytes);
			LOG.debug("Json :" + jsonString);
			item = objMapper.readValue(jsonString, clazz);
		} catch (JsonParseException | JsonMappingException e) {
			if (error) {
				error = false;
				throw new TorrentAPIException("Response came back with an unknown Object", e);
			} else  {
				LOG.warn("Json Parse/Mapping Exception when parsing object, probably Error returned");
				error = true;
				final APIError err = getObjectFromResult(bytes, APIError.class);
				if (err != null) {
					LOG.warn("Error Result from API");
					error = false;
					item = null;
				} else {
					throw new TorrentAPIException("Response came back with an unknown Object", e);
				}
			}
		} catch (UnsupportedOperationException e) {
			throw new TorrentAPIException("UnsupportedOperationException when parsing HTTP Request", e);
		} catch (IOException e) {
			throw new TorrentAPIException("IOException when parsing HTTP Request", e);
		}
		return item;
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	private boolean checkResponse(final HttpResponse response) {
		LOG.debug("Checking Response Status :" + response.getStatusLine().getStatusCode());
		LOG.debug(response.getStatusLine().getReasonPhrase());
		return response.getStatusLine().getStatusCode() < 210;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 * @throws TorrentAPIException
	 */
	private HttpResponse getResponse(final HttpRequestBase uri) throws TorrentAPIException {
		final HttpClient client = getHttpClient();

		HttpResponse response;
		try {
			response = client.execute(uri);
			return response;
		} catch (IOException e) {
			throw new TorrentAPIException("IOException when executing HTTP Request", e);
		}
	}
	
	/**
	 * 
	 * @param <T>
	 * @param uri
	 * @param clazz
	 * @return
	 * @throws TorrentAPIException
	 */
	protected <T> T getObject(final URI uri, final Class<T> clazz) throws TorrentAPIException {
		final HttpGet get = new HttpGet(uri);
		setUpHttpRequestBase(get);
		T item;
		
		LOG.debug("HTTP Get :" + uri.toString());
		final HttpResponse response = getResponse(get);
		
		try {
			if (checkResponse(response)) {
				final ByteArrayOutputStream bos = new ByteArrayOutputStream();
				
				IOUtils.copy(response.getEntity().getContent(), bos);
				
				final byte[] bytes = bos.toByteArray();
				
				item = getObjectFromResult(bytes, clazz);
				LOG.info("Item :" + item);
			} else {
				item = null;
			}
			if (item == null) {
				LOG.debug("No Item on Response");
				item = retry(uri, clazz);
			} else {
				requestCounter = 0;
			}
		} catch (InterruptedException e) {
			throw new TorrentAPIException("Exception when trying to put Thread to sleep", e);
		} catch (UnsupportedOperationException | IOException e) {
			throw new TorrentAPIException(e.getClass().getName() + " when trying to copy Response input stream to Byte array", e);
		}
		return item;
	}

	private <T> T retry(final URI uri, final Class<T> clazz)
			throws InterruptedException, TorrentAPIException {
		T item;
		if (requestCounter < maxRetries) {
			LOG.debug("Trying again in " + (WAIT_TIMER / 1000) + " seconds");
			Thread.sleep(WAIT_TIMER);
			requestCounter++;
			item = getObject(uri, clazz);
		} else {
			requestCounter = 0;
			throw new TorrentNotFoundException("Couldn't get good response from " + uri.toString() + " after "
					+ maxRetries + " attempts");
		}
		return item;
	}
	
	/**
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws InterruptedException
	 * 
	 */
	protected void checkToken() throws TorrentAPIException {
		
		if (token == null || new Date().after(token.getExpiresIn())) {
			getToken();
		}
	}

	private void getToken() throws TorrentAPIException {
		try {
			final URI uri = getURI(getTokenParams());

			final Token token = getObject(uri, Token.class);
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MILLISECOND, TOKEN_EXPIRY);
			
			token.setExpiresIn(cal.getTime());
			
			LOG.debug("Got Token :" + token.getToken());
			LOG.debug("Token Expires :" + token.getExpiresIn());
			
			this.token = token;
			
		} catch (URISyntaxException e) {
			throw new TorrentAPIException("Error when creating URI to get Token", e);
		}
	}
	
}
