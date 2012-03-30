package model;

import java.io.File;
import java.io.IOException;

import javax.management.AttributeNotFoundException;

import org.scribe.model.Response;

public class Main {

	public static void main(String[] args) {
		AccessGenerator generator = null;

		try {
			generator = AccessGenerator.generateFromProperties(File.separator
					+ "linkedin.properties");
		} catch (AttributeNotFoundException | IOException e) {
			e.printStackTrace();
		}

		RESTRequester requester = new RESTRequester(generator.getService(),
				generator.getAccessToken());

		Response response = requester
				.GET("http://api.linkedin.com/v1/people/id=7fV1HdMLHo", true);
		System.out.println(response.getBody());
	}
}
