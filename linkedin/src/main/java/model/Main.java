package model;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import model.Request.ApiType;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.LInPerson;

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
		LInPerson node;
		List<LInPerson> persons = new LinkedList<LInPerson>();
		System.out.println("========== FIRST DEGREE CONNECTIONS ==========");
		List<Element> connections = crawler.firstDegrees;
		for (Element e : connections) {
			node = new LInPerson(e);
			System.out.println(node.getId());
			persons.add(node);
		}

		System.out.println("\n========== ALL PERSONS FROM UPDATES ==========");
		List<Element> updates = crawler.firstDegreeUpdates;
		for (Element e : updates) {
			for (Element p : Elements.extractAll(e, ElementType.PERSON)) {
				node = new LInPerson(p);
				System.out.println(node.getId());
				persons.add(node);
			}
		}

		System.out.println("\n========== MERGED ==========");
		for (LInPerson n : persons) {
			System.out.println(n.getId());
		}

		System.out.println("\n========== SHRINKED ==========");
		HashSet<LInPerson> hashSet = new HashSet<LInPerson>(persons);
		for (LInPerson n : hashSet) {
			System.out.println(n.getId());
		}

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
