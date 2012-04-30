package model;

import java.io.File;
import java.io.IOException;

import org.scribe.model.Response;

public class Main {

	public static void main(String[] args) {
		AccessGenerator generator = null;

		// Get access to the linkedin server
		try {
			generator = AccessGenerator.generateFromProperties(File.separator
					+ "linkedin.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create request factory to do API calls
		Request requester = new Request(generator.getService(),
				generator.getAccessToken());

		// Do a request
		Response response = requester.GET(
				"http://api.linkedin.com/v1/people/id=mL8t-bd_We", true);
		// "http://api.linkedin.com/v1/people-search?first-name=Pierre", true);
		System.out.println(response.getBody());
	}
}
