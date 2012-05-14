package model;

import java.io.File;
import java.io.IOException;

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

		crawlNetwork(requester);
	}

	public static void crawlNetwork(Request requester) {
		PeopleCrawler crawler = new PeopleCrawler(requester);
		for (LInPerson p : crawler.getSharedConnections("Rv7HgPO5ou")) {
			System.out.println(p.getElement().getValue());
		}
	}
}
