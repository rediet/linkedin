package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Main {

	public static void main(String[] args) {

		String[] keys = readAPIKeys();
		OAuthAccess oauth = new OAuthAccess(keys[0], keys[1]);
		Token accessToken = oauth.getAccessToken();
		OAuthService service = oauth.getService();

		String url = "http://api.linkedin.com/v1/people/~";
		OAuthRequest request = new OAuthRequest(Verb.GET, url);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println(response.getBody());
	}

	public static String[] readAPIKeys() {
		String[] keys = new String[2];
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(File.separator
					+ "linkedin.properties"));
			keys[0] = properties.getProperty("API_KEY");
			keys[1] = properties.getProperty("API_SECRET");
		} catch (FileNotFoundException e) {
			System.err.println("Properties file not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read API-key properly!");
			e.printStackTrace();
		}

		return keys;
	}

}
