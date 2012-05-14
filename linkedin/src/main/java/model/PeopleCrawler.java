package model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.LInPerson;

public class PeopleCrawler extends Crawler {

	public static final String PERSON_FIELDS = "(id,first-name,last-name,distance,three-current-positions,three-past-positions,location:(country:(code)))";
	// relation-to-viewer:(distance,related-connections)

	/*
	 * PEOPLE-API ----------------------------------
	 * http://api.linkedin.com/v1/people/~
	 * http://api.linkedin.com/v1/people/id=abcdefg
	 * http://api.linkedin.com/v1/people/url=<public-profile-url>
	 */
	public static final int DEFAULT_PEOPLE_PAGE_SIZE = 10;
	public static final int MAX_PEOPLE_PAGE_SIZE = 25;

	/*
	 * CONNECTIONS-API -----------------------------
	 * http://api.linkedin.com/v1/people/~/connections
	 * http://api.linkedin.com/v1/people/id=12345/connections
	 * http://api.linkedin.com/v1/people/url=<public-profile-url>/connections
	 */
	public static final int DEFAULT_CONNECTION_PAGE_SIZE = 500;
	public static final int MAX_CONNECTION_PAGE_SIZE = 500;

	public PeopleCrawler(Request requester) {
		super(requester);
	}

	public List<LInPerson> getFirstDegreeConnections() {
		Response response = requester.GET("people/~/connections:"
				+ PERSON_FIELDS);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extract(element, ElementType.PERSON));
	}

	public List<LInPerson> getSharedConnections(String userId) {
		Response response = requester.GET("people/" + userId
				+ "/relation-to-viewer:(num-results)?count="
				+ MAX_PEOPLE_PAGE_SIZE);
		Element element = Elements.fromResponse(response);
		System.out.println(response.getBody());
		return convertPerson(Elements.extract(element, ElementType.PERSON));
	}

	/**
	 * Performs a people search using the given queryParameters. Returns all
	 * pages available. Note that each page is one API call (one page = max 25
	 * elements)
	 * 
	 * @param queryParameters
	 *            keywords=[space delimited keywords], first-name=[first name],
	 *            last-name=[last name], company-name=[company name],
	 *            current-company=[true|false], title=[title],
	 *            current-title=[true|false], school-name=[school name],
	 *            current-school=[true|false], country-code=[country code],
	 *            postal-code=[postal code], distance=[miles], facet=[facet
	 *            code, values], facets=[facet codes]
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
			exception.printStackTrace();
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
				query.append(new URI(parameter).toString());
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
}
