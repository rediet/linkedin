package model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import structure.LInPerson;

import api_crawlers.Connection;
import api_crawlers.GraphBuilder;
import api_crawlers.PeopleCrawler;

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

		GraphBuilder graph = crawlNetwork(requester);

		for (Connection c : graph.getConnections()) {
			System.out.println(c);
		}
	}

	public static GraphBuilder crawlNetwork(Request requester) {
		PeopleCrawler crawler = new PeopleCrawler(requester);
		GraphBuilder graph = new GraphBuilder();

		// // first degree connections
		// LInPerson self = crawler.getOwnProfile();
		// List<LInPerson> people = crawler.getFirstDegreeConnections();
		// graph.addConnection(self, people);

		// // 2nd degree people-search
		// people = crawler.searchPeople("facet=network,S");
		// graph.addConnection(self, people);

		// // people-search inside one of the user's groups
		// crawler.searchPeople("facet=network,A");
		// graph.addConnection(self, people);

		// specific 2nd degree or group people-search
		crawler.searchPeople("company-name=University of Bern",
				"facet=network,S");

		// // shared connections
		// for (LInPerson p : people) {
		// graph.addConnection(p, crawler.getSharedConnections(p.getId()));
		// }

		return graph;
	}
}
