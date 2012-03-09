package model;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.oauth.OAuthService;

public class Main {

	// TODO: replace with current API-key and secret
	private static String API_KEY = "xxxxxxxxxxxx";
	private static String API_SECRET = "xxxxxxxxxxxxx";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out
				.println("Hello world! This is an initialization to the LinkedIn API");

		OAuthService service = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(API_KEY).apiSecret(API_SECRET).build();

	}

}
