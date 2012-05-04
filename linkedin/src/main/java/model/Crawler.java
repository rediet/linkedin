package model;

import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
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

	public List<Element> firstDegrees;
	public List<Element> firstDegreeUpdates;
	public List<Element> groupMemberships;
	public List<Element> companiesFollowing;
	public List<Element> peopleSearch;

	public Crawler(Request requester) {
		this.requester = requester;

	}

	public void run() {
		// this.firstDegrees = firstDegreeConnections();
		// this.firstDegreeUpdates = firstDegreeConnectionUpdates();

		// this is useful for a keyboard search or company-search
		this.groupMemberships = groupMemberships();
		// this.companiesFollowing = companiesFollowing();

		// this.secondDegreeSearch = peopleSearch(); //network members (1st,2nd
		// and Group members)
		// this.peopleSearch = peopleSearch("keywords=microsoft");
		// this.peopleSearch = peopleSearch();
	}

	// HELPERS
	public List<Element> firstDegreeConnections() {
		Response response = requester.GET("~/connections:(id,distance)",
				ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extract(element, ElementType.PERSON);
	}

	public List<Element> firstDegreeConnectionUpdates() {
		Response response = requester.GET("~/network/updates", ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extractAll(element, ElementType.PERSON);
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

	public List<Element> peopleSearch(String... keywords) {
		int start = 0;
		// do a first request
		Element e = peopleSearch(start, MAX_COUNT, keywords);
		List<Element> personList = Elements.extract(e, ElementType.PERSON);
		int total, count;
		try { // search through further pages
			total = e.getChild("people").getAttribute("total").getIntValue();
			count = e.getChild("people").getAttribute("count").getIntValue();
			start = count;
			while (start < total) {
				e = peopleSearch(start, count, keywords);
				personList.addAll(Elements.extract(e, ElementType.PERSON));
				start += count;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		System.out.println(personList.size());
		return personList;
	}

	private Element peopleSearch(int start, int count,
			String... queryParameters) {
		StringBuffer query = new StringBuffer();
		query.append("people-search:(people:(id,first-name,last-name,distance),num-results)?");
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
}
