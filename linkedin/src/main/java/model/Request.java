package model;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * The Request class provides methods to parse and execute HTTP requests and to
 * return Response objects containing either an XML or JSON representation of
 * the server response.
 */
public class Request {

	public static final String API_PREAMBLE = "http://api.linkedin.com/v1/";
	public static final String PROFILE = "people/";
	public static final String PROFILE_SEARCH = "people-search?";
	public static final String GROUPS = "groups/";
	public static final String COMPANIES = "companies/";

	public enum ApiType {
		People, ProfileSearch, Group, Company
	};

	Token accessToken;
	OAuthService service;

	/**
	 * Creates a Request service instance using the specified OAuthService and
	 * access token.
	 * @param service
	 * @param accessToken
	 */
	public Request(OAuthService service, Token accessToken) {
		this.service = service;
		this.accessToken = accessToken;
	}

	/**
	 * Sends an HTTP GET request to the server and returns the Response in XML
	 * format.
	 * @param url the HTTP request
	 * @return the Response
	 */
	public Response GET(String url) {
		return this.GET(url, false);
	}

	/**
	 * Sends an HTTP GET request to the server and returns the Response either
	 * as XML or JSON.
	 * @param url The HTTP request
	 * @param JSON If true, the response format is a JSON object. If false the
	 *            response format will be XML.
	 * @return the Response
	 */
	public Response GET(String url, boolean JSON) {
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		if (JSON) {
			request.addHeader("x-li-format", "json");
		}
		service.signRequest(accessToken, request);
		return request.send();
	}

	public Response GET(String identifier, ApiType type) {
		return GET(identifier, type, false);
	}

	public Response GET(String identifier, ApiType type, boolean JSON) {
		switch (type) {
		case People:
			return GET(API_PREAMBLE + PROFILE + identifier, JSON);
		case ProfileSearch:
			return GET(API_PREAMBLE + PROFILE_SEARCH + identifier, JSON);
		case Group:
			return GET(API_PREAMBLE + GROUPS + identifier, JSON);
		case Company:
			return GET(API_PREAMBLE + COMPANIES + identifier, JSON);
		default:
			return null;
		}

	}

}
