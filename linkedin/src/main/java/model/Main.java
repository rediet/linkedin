package model;

import java.io.File;
import java.io.IOException;

import org.scribe.model.Response;

public class Main {

	public static void main(String[] args) {
		AccessGenerator generator = null;

		try {
			generator = AccessGenerator.generateFromProperties(File.separator
					+ "linkedin.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Request requester = new Request(generator.getService(),
				generator.getAccessToken());

		Response response = requester.GET(
				"http://api.linkedin.com/v1/people/~", true);
		System.out.println(response.getBody());
	}
}
