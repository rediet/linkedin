package model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.LInCompany;
import structure.LInGroup;
import structure.LInPerson;
import model.Request.ApiType;

public class Crawler {

	/* Query parameters that can be specified: */
	// keywords=[space delimited keywords]
	// first-name=[first name]
	// last-name=[last name]
	// company-name=[company name]
	// current-company=[true|false]
	// title=[title]
	// current-title=[true|false]
	// school-name=[school name]
	// current-school=[true|false]
	// country-code=[country code]
	// postal-code=[postal code]
	// distance=[miles]
	// facet=[facet code, values]
	// facets=[facet codes]
	// ---- ALREADY USED: ---
	// start=[number]
	// count=[1-25]
	// sort=[connections|recommenders|distance|relevance]

	public static final String PERSON_FIELDS = "(id,first-name,last-name,distance,three-current-positions,three-past-positions,location:(country:(code)))";
	public static final String COMPANY_FIELDS = "(id,name,universal-name,num-followers)";
	public static final String GROUP_FIELDS = "(group:(id,name,num-members,location:(country,postal-code)))";

	public List<LInPerson> personDatabase;
	public List<LInGroup> groupMemberships;
	public List<LInCompany> companiesFollowing;

	private Request requester;

	public Crawler(Request requester) {
		this.requester = requester;

	}

	public void run() {
		// this.personDatabase = getFirstDegreeConnections();
		// this.personDatabase.addAll(getNetworkUpdates());
		// this.personDatabase.addAll(searchPeople());
		//
		// // HashSet removes duplicates
		// for (LInPerson e : new HashSet<LInPerson>(personDatabase)) {
		// System.out.println(e.getId());
		// }

		// this.groupMemberships = getGroupMemberships();
		// for (LInGroup g : this.groupMemberships) {
		// System.out.println(g);
		// }

		this.companiesFollowing = getCompaniesFollowing();
		// for (LInCompany c : this.companiesFollowing) {
		// System.out.println(c);
		// }

		for (LInCompany c : this.companiesFollowing) {
			searchPeople(0, 25, "company=" + c.getName());
			// ,"current-company=true");
		}
	}

	// CRAWLERS ----------------------------------------------------------------

	public List<LInPerson> getFirstDegreeConnections() {
		Response response = requester.GET("~/connections:" + PERSON_FIELDS,
				ApiType.People);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extract(element, ElementType.PERSON));
	}

	// TODO: Updates have a big range of different types
	public List<LInPerson> getNetworkUpdates() {
		Response response = requester.GET("~/network/updates", ApiType.People);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extractAll(element, ElementType.PERSON));
	}

	public List<LInGroup> getGroupMemberships() {
		Response response = requester.GET(
				"~/group-memberships:" + GROUP_FIELDS, ApiType.People);
		Element element = Elements.fromResponse(response);
		return convertGroup(Elements.extract(element, ElementType.GROUP));
	}

	public List<LInCompany> getCompaniesFollowing() {
		Response response = requester.GET("~/following/companies:"
				+ COMPANY_FIELDS, ApiType.People);
		Element element = Elements.fromResponse(response);
		return convertCompany(Elements.extract(element, ElementType.COMPANY));
	}

	/**
	 * Performs a people search using the given keywords. Returns all pages
	 * available (one page = max 25 elements). Note that each page is on API
	 * call.
	 * 
	 * @param keywords
	 * @return
	 */
	public List<LInPerson> searchPeople(String... keywords) {
		int start = 0;
		// do a first request
		Element result = searchPeople(start, MAX_COUNT, keywords);
		List<Element> personList = Elements.extract(result, ElementType.PERSON);
		int total, count;
		try { // search through further pages
			total = result.getChild("people").getAttribute("total")
					.getIntValue();
			count = result.getChild("people").getAttribute("count")
					.getIntValue();
			start = count;
			while (start < total) {
				result = searchPeople(start, count, keywords);
				personList.addAll(Elements.extract(result, ElementType.PERSON));
				start += count;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return convertPerson(personList);
	}

	private static final int MAX_COUNT = 25; // number of a search results

	// HELPERS ----------------------------------------------------------------

	/**
	 * Single page people search.
	 * 
	 * @param start
	 *            start index
	 * @param count
	 *            number of elements to return (API limit: 25)
	 * @param queryParameters
	 *            additional query parameters
	 * @return an Element representing the XML search result
	 */
	private Element searchPeople(int start, int count,
			String... queryParameters) {
		StringBuffer query = new StringBuffer();
		query.append("people-search:(people:" + PERSON_FIELDS
				+ ",num-results)?");
		// query.append("people-search:(facets:(code,buckets:(code,name)))?facets=location");
		// //seems not to work

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

		Response response = requester.GET(query.toString(), ApiType.Preamble);
		System.out.println(query.toString());
		System.out.println(response.getBody());
		return Elements.fromResponse(response);
	}

	// PARSERS ----------------------------------------------------------------

	private List<LInPerson> convertPerson(List<Element> list) {
		List<LInPerson> persons = new LinkedList<LInPerson>();
		for (Element e : list) {
			persons.add(new LInPerson(e));
		}
		return persons;
	}

	private List<LInCompany> convertCompany(List<Element> list) {
		List<LInCompany> companies = new LinkedList<LInCompany>();
		for (Element e : list) {
			companies.add(new LInCompany(e));
		}
		return companies;
	}

	private List<LInGroup> convertGroup(List<Element> list) {
		List<LInGroup> groups = new LinkedList<LInGroup>();
		for (Element e : list) {
			groups.add(new LInGroup(e));
		}
		return groups;
	}
}
