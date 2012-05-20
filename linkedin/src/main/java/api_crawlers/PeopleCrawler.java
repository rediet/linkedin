package api_crawlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import model.Request;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.LInPerson;

public class PeopleCrawler extends Crawler {

	public static final String PERSON_FIELDS = "(id,first-name,last-name,distance)";
	// "(id,first-name,last-name,distance,three-current-positions,three-past-positions,location:(country:(code)))";

	/*
	 * PEOPLE-API ------------------------------ THROTTLE LIMIT: 200
	 * http://api.linkedin.com/v1/people/~
	 * http://api.linkedin.com/v1/people/id=abcdefg
	 * http://api.linkedin.com/v1/people/url=<public-profile-url>
	 */
	public static final int DEFAULT_PEOPLE_PAGE_SIZE = 10;
	public static final int MAX_PEOPLE_PAGE_SIZE = 25;

	/*
	 * CONNECTIONS-API ------------------------- THROTTLE LIMIT: 20k
	 * http://api.linkedin.com/v1/people/~/connections
	 * http://api.linkedin.com/v1/people/id=12345/connections
	 * http://api.linkedin.com/v1/people/url=<public-profile-url>/connections
	 */
	public static final int DEFAULT_CONNECTION_PAGE_SIZE = 500;
	public static final int MAX_CONNECTION_PAGE_SIZE = 500;

	public PeopleCrawler(Request requester) {
		super(requester);
	}

	/**
	 * Gets the full-detail profile of the current user.
	 */
	public LInPerson getOwnProfile() {
		Response response = requester.GET("people/~:" + PERSON_FIELDS);
		Element element = Elements.fromResponse(response);
		return new LInPerson(element);
	}

	/**
	 * Gets the full-detail profile of all people in the current user's network.
	 * (does not allow relation-to-viewer).
	 */
	public List<LInPerson> getFirstDegreeConnections() {
		Response response = requester.GET("people/~/connections"
		/* + PERSON_FIELDS */);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extract(element, ElementType.PERSON));
	}

	/**
	 * Gets the mini-profile of all shared connections with a particular user.
	 * 
	 * @param userId
	 * @return
	 */
	public List<LInPerson> getSharedConnections(String userId) {
		Response response = requester.GET("people/" + userId
				+ "/relation-to-viewer?count=" + MAX_PEOPLE_PAGE_SIZE);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extract(element, ElementType.PERSON));
	}

	/**
	 * Performs a people search using the specified, optional queryParameters.
	 * Searches through all pages available. By default, if no keywords (see
	 * below) are specified, the result will contain a list of all people in the
	 * member's network. Therefore to search for people outside the user's
	 * network, a keyword has to be specified. Note that each page is one API
	 * call (one page = max 25 elements).
	 * 
	 * @param queryParameters
	 *            a specification of additional query parameters that can be of
	 *            the following type:
	 *            <p>
	 *            keywords=[space delimited keywords], first-name=[first name],
	 *            last-name=[last name], company-name=[company name],
	 *            current-company=[true|false], title=[title],
	 *            current-title=[true|false], school-name=[school name],
	 *            current-school=[true|false], country-code=[country code],
	 *            postal-code=[postal code], distance=[miles], facet=[facet
	 *            code, values], facets=[facet codes]
	 *            </p>
	 * @return a list of LInPerson nodes containing all the people that match
	 *         the query parameters
	 */
	public List<LInPerson> searchPeople(String... queryParameters) {
		int start = 0;
		// do a first request
		Element result = searchPeople(start, MAX_PEOPLE_PAGE_SIZE,
				queryParameters);
		List<Element> personList = Elements.extract(result, ElementType.PERSON);
		int total, count;
		try { // search through further pages
			total = result.getChild("people").getAttribute("total")
					.getIntValue();
			count = result.getChild("people").getAttribute("count")
					.getIntValue();
			start = count;
			while (start < total) {
				result = searchPeople(start, count, queryParameters);
				personList.addAll(Elements.extract(result, ElementType.PERSON));
				start += count;
			}
		} catch (Exception exception) {
			// do not progress further pages - they don't exist
		}
		return convertPerson(personList);
	}

	private Element searchPeople(int start, int count,
			String... queryParameters) {
		StringBuffer query = new StringBuffer();
		query.append("people-search:(people:" + PERSON_FIELDS
				+ ",num-results)?");

		for (String parameter : queryParameters) {
			try {
				query.append(new URI(parameter.replace(" ", "%20")).toString());
				query.append("&");
			} catch (URISyntaxException e) {
				System.err.println("invalid syntax: \"" + parameter + "\"");
			}
		}

		query.append("start=").append(start);
		query.append("&count=").append(count);
		query.append("&sort=distance");

		Response response = requester.GET(query.toString());
		return Elements.fromResponse(response);
	}

	/**
	 * Gets the connections of a particular user
	 * 
	 * @deprecated Does not work - this api call is restricted only to users
	 *             that allowed the application access to their profile
	 *             information.
	 * @param userId
	 *            the unique user id
	 * @return a list of first degree connections of that user.
	 */
	public List<LInPerson> getUserConnections(String userId) {
		Response response = requester.GET("people/id=" + userId
				+ "/connections:" + PERSON_FIELDS);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extract(element, ElementType.PERSON));
	}

	public List<Connection> getUpdatedConnections() {
		// Retrieve the member's first-degree connection updates
		Response response = requester.GET("people/~/network/updates");
		Element element = Elements.fromResponse(response);
		List<Element> connUpdates = Elements.extract(element,
				ElementType.UPDATE);
		List<Connection> newConn = new LinkedList<Connection>();
		for (Element e : connUpdates) {
			if (Elements.extractAll(e, ElementType.PERSON).size() == 2) {
				LInPerson one = new LInPerson(Elements.extractAll(e,
						ElementType.PERSON).get(0));
				LInPerson two = new LInPerson(Elements.extractAll(e,
						ElementType.PERSON).get(1));
				newConn.add(new Connection(one, two));
			}
		}

		/*
		 * for (Connection c : newConn) { System.out.println(c); }
		 */
		return newConn;
	}
}
