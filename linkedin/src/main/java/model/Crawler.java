package model;

import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.PersonElement;
import model.Request.ApiType;

public class Crawler {

	// query parameters that can be specified:
	// keywords=[space delimited keywords]& first-name=[first name]&
	// last-name=[last name]& company-name=[company name]&
	// current-company=[true|false]& title=[title]& current-title=[true|false]&
	// school-name=[school name]& current-school=[true|false]&
	// country-code=[country code]& postal-code=[postal code]& distance=[miles]&
	// start=[number]& count=[1-25]& facet=[facet code, values]& facets=[facet
	// codes]& sort=[connections|recommenders|distance|relevance]";

	private Request requester;

	public List<PersonElement> firstDegrees;
	public List<PersonElement> firstDegreeUpdates;
	public List<Element> groupMemberships;
	public List<Element> companiesFollowing;
	public List<PersonElement> peopleSearch;

	public Crawler(Request requester) {
		this.requester = requester;

	}

	public void run() {
		this.firstDegrees = firstDegreeConnections();
		this.firstDegreeUpdates = firstDegreeConnectionUpdates();

		// this is useful for a keyboard search or company-search
		this.groupMemberships = groupMemberships();
		this.companiesFollowing = companiesFollowing();

		// network members (1st,2nd and Group members)
		this.peopleSearch = peopleSearch("keywords=microsoft");
		this.peopleSearch = peopleSearch();
	}

	// HELPERS
	public List<PersonElement> firstDegreeConnections() {
		Response response = requester.GET("~/connections:(id,distance)",
				ApiType.People);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extract(element, ElementType.PERSON));
	}

	public List<PersonElement> firstDegreeConnectionUpdates() {
		Response response = requester.GET("~/network/updates", ApiType.People);
		Element element = Elements.fromResponse(response);
		return convertPerson(Elements.extractAll(element, ElementType.PERSON));
	}

	public List<Element> groupMemberships() {
		Response response = requester
				.GET("~/group-memberships", ApiType.People);
		Element element = Elements.fromResponse(response);
		System.out.println(response.getBody());
		return Elements.extract(element, ElementType.GROUP);
	}

	public List<Element> groupInformation(String... groupIDs) {
		List<Element> list = new LinkedList<Element>();
		Response response;
		for (String id : groupIDs) {
			response = requester.GET(id + "", ApiType.Group);
			list.add(Elements.fromResponse(response));
		}
		return list;
	}

	public List<Element> companiesFollowing() {
		Response response = requester.GET(
				"~/following/companies:(id,name,universal-name)",
				ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extract(element, ElementType.COMPANY);
	}

	public static final int MAX_COUNT = 25; // number of a search results

	public List<PersonElement> peopleSearch(String... keywords) {
		int start = 0;
		// do a first request
		Element result = peopleSearch(start, MAX_COUNT, keywords);
		List<Element> personList = Elements.extract(result, ElementType.PERSON);
		int total, count;
		try { // search through further pages
			total = result.getChild("people").getAttribute("total")
					.getIntValue();
			count = result.getChild("people").getAttribute("count")
					.getIntValue();
			start = count;
			while (start < total) {
				result = peopleSearch(start, count, keywords);
				personList.addAll(Elements.extract(result, ElementType.PERSON));
				start += count;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		System.out.println(personList.size());
		return convertPerson(personList);
	}

	private Element peopleSearch(int start, int count,
			String... queryParameters) {
		StringBuffer query = new StringBuffer();
		query.append("people-search:(people:(id,first-name,last-name,distance),num-results)?");
		// query.append("people-search:(facets:(code,buckets:(code,name)))?facets=location");
		// //seems not to work
		query.append("start=").append(start);
		query.append("&count=").append(count);
		query.append("&sort=distance");
		for (String parameter : queryParameters) {
			query.append("&").append(parameter);
		}
		System.out.println(query.toString());
		Response response = requester.GET(query.toString(), ApiType.Preamble);
		System.out.println(response.getBody());
		return Elements.fromResponse(response);
	}

	private List<PersonElement> convertPerson(List<Element> list) {
		List<PersonElement> persons = new LinkedList<PersonElement>();
		for (Element e : list) {
			persons.add(new PersonElement(e));
		}
		return persons;
	}
}
