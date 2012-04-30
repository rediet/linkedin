package model;

//test

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class AccessGenerator {

	private OAuthService service;
	private Token accessToken;

	public AccessGenerator(String apiKey, String apiSecret) {
		service = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(apiKey).apiSecret(apiSecret).build();
		accessToken = null;
	}

	public Token getAccessToken() {
		return accessToken;
	}

	public OAuthService getService() {
		return service;
	}

	public boolean isAccessTokenValid() {
		if (accessToken == null) {
			return false;
		} else {
			return false; // TODO: insert procedure to check validity
		}
	}

	public Token requestAccessToken() {
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

	public void setAccessToken(Token token) {
		this.accessToken = token;
	}

	public static AccessGenerator generateFromProperties(String fileName)
			throws IOException {
		AccessGenerator access;

		String[] keys = new String[4];
		Properties properties = new Properties();

		File propertiesFile = new File(fileName);

		if (propertiesFile.exists()) {
			properties.load(new FileInputStream(fileName));
			keys[0] = properties.getProperty("API_KEY");
			keys[1] = properties.getProperty("API_SECRET");
			keys[2] = properties.getProperty("TOKEN");
			keys[3] = properties.getProperty("TOKEN_SECRET");
		} else {
			propertiesFile.createNewFile();
		}

		if (keys[0] == null) {
			System.out.println("Please enter the API key:");
			keys[0] = (new BufferedReader(new InputStreamReader(System.in)))
					.readLine();
			properties.setProperty("API_KEY", keys[0]);
		}

		if (keys[1] == null) {
			System.out.println("Please enter the API secret:");
			keys[1] = (new BufferedReader(new InputStreamReader(System.in)))
					.readLine();
			properties.setProperty("API_SECRET", keys[0]);
		}

		access = new AccessGenerator(keys[0], keys[1]);

		if (keys[2] == null || keys[3] == null) {
			Token token = access.requestAccessToken();
			properties.setProperty("TOKEN", token.getToken());
			properties.setProperty("TOKEN_SECRET", token.getSecret());
			properties.store(new FileOutputStream(fileName), null);
		} else {
			access.setAccessToken(new Token(keys[2], keys[3]));
		}

		return access;
	}
}