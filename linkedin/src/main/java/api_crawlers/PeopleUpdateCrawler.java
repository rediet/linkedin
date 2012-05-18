package api_crawlers;

import java.util.List;

import model.Request;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.LInPerson;

public class PeopleUpdateCrawler extends Crawler {

	/*
	 * SHARE-API ------------------------------ THROTTLE LIMIT:
	 * http://api.linkedin.com/v1/people/~/shares
	 * 
	 * UPDATEs - API -------------------------- THROTTLE LIMIT:
	 * http://api.linkedin.com/v1/people/~/network/updates?scope=self
	 * http://api.linkedin.com/v1/people/~/network/updates
	 */

	public PeopleUpdateCrawler(Request requester) {
		super(requester);
	}

	// TODO: Updates have a big range of different types
	public List<LInPerson> getNetworkUpdates() {
		Response response = requester.GET("people/~/network/updates");
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extractAll(element, ElementType.PERSON));
	}
}
