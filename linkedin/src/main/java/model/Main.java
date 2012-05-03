package model;

import java.io.File;
import java.io.IOException;

import model.Request.ApiType;

import org.scribe.model.Response;

public class Main {

	public static final String DANIELE = "scar4b1FXy";
	public static final String REMO = "7fV1HdMLHo";

	public static void main(String[] args) {
		AccessGenerator generator = null;

		// Get access to the LinkedIn server
		try {
			generator = AccessGenerator.generateFromProperties(File.separator
					+ "linkedin.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create request factory to do API calls
		Request requester = new Request(generator.getService(),
				generator.getAccessToken());

		attemptFromRemo(requester);
		// exampleMethod(requester);
	}

	public static void attemptFromRemo(Request requester) {
		Crawler crawler = new Crawler(requester);
		crawler.run();
	}

	public static void exampleMethod(Request requester) {
		// Do a request
		Response response;
		// response =
		// requester.GET("http://api.linkedin.com/v1/people/id=7fV1HdMLHo");
		// response =
		// requester.GET("http://api.linkedin.com/v1/people-search?first-name=Remo");
		// response =
		// requester.GET("http://api.linkedin.com/v1/groups/2218477");
		// response =
		// requester.GET("http://api.linkedin.com/v1/companies/1035");
		// response = requester.GET("~/network/updates",ApiType.People);
		response = requester
				.GET("~/network/updates?scope=self", ApiType.People);
		System.out.println(response.getBody());
	}
}
