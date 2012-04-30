package model;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Request {

	Token accessToken;
	OAuthService service;

	public Request(OAuthService service, Token accessToken) {
		this.service = service;
		this.accessToken = accessToken;
	}

	public Response GET(String url) {
		return this.GET(url, false);
	}

	public Response GET(String url, boolean JSON) {
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		if (JSON) {
			request.addHeader("x-li-format", "json");
		}
		service.signRequest(accessToken, request);
		return request.send();
	}
}
