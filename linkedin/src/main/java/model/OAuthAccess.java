package model;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class OAuthAccess {

	private OAuthService service;
	private Token accessToken;

	public OAuthAccess(String apiKey, String apiSecret) {
		service = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(apiKey).apiSecret(apiSecret).build();
	}

	public OAuthService getService() {
		return service;
	}

	public Token getAccessToken() {
		if (accessToken != null) {
			return accessToken;
		} else {
			Scanner in = new Scanner(System.in);
			Token requestToken = service.getRequestToken();
			String authUrl = service.getAuthorizationUrl(requestToken);
			System.out.println("get your veryfier from this page:");
			System.out.println(authUrl);
			System.out.println("enter your verifier:");
			Verifier verifier = new Verifier(in.nextLine());
			accessToken = service.getAccessToken(requestToken, verifier);
			return accessToken;
		}
	}
}
