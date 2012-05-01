package model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import model.Request.ApiType;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.INode;
import structure.Node;

public class Main {

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

		// Do a request
		/*
		 * Response response = requester.GET( //
		 * "http://api.linkedin.com/v1/people/id=mL8t-bd_We");
		 * "http://api.linkedin.com/v1/people-search?first-name=Pierre"); //
		 * "http://api.linkedin.com/v1/groups/2218477"); //
		 * "http://api.linkedin.com/v1/companies/1035");
		 */

		Response response = requester.GET("first-name=Pierre",
				ApiType.ProfileSearch);
		System.out.println("Response successful: " + response.isSuccessful());
		System.out.println(response.getBody());

		attemptFromRemo(response);
	}

	public static void attemptFromRemo(Response resp) {
		INode node = new Node(resp.getBody());
		Collection<Element> elements = Elements.extract(node.getRootElement(),
				ElementType.PERSON);
		System.out.println("root element: "+node.getRootElement().getName());
		System.out.println("extract persons:");
		for (Element e : elements) {
			System.out.println(e.toString());
		}
	}
}
