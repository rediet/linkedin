package model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import model.Request.ApiType;

public class Crawler {

	private Request requester;

	public List<Element> firstDegrees;
	public List<Element> firstDegreeUpdates;
	public List<Element> groupMemberships;
	public List<Element> companiesFollowing;
	public List<Element> secondDegreeSearch;

	public Crawler(Request requester) {
		this.requester = requester;

	}

	public void run() {
		this.firstDegrees = firstDegreeConnections();
		this.firstDegreeUpdates = firstDegreeConnectionUpdates();
		this.groupMemberships = groupMemberships();
		this.companiesFollowing = companiesFollowing();

		// order important:
		this.secondDegreeSearch = secondDegreeSearch();
	}

	// HELPERS
	private List<Element> firstDegreeConnections() {
		Response response = requester.GET("~/connections", ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extract(element, ElementType.PERSON);
	}

	private List<Element> firstDegreeConnectionUpdates() {
		Response response = requester.GET("~/network/updates", ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extractAll(element, ElementType.PERSON);
	}

	private List<Element> groupMemberships() {
		Response response = requester
				.GET("~/group-memberships", ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extract(element, ElementType.GROUP);
	}

	private List<Element> companiesFollowing() {
		Response response = requester.GET(
				"~/following/companies:(id,name,universal-name)",
				ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extract(element, ElementType.COMPANY);
	}

	private List<Element> secondDegreeSearch() {
		Response response;
		Element responseElement;
		List<Element> people = new LinkedList<Element>();
		for (Element el : companiesFollowing) {
			try {
				response = requester.GET(
						"company-name="
								+ URLEncoder.encode(el.getChildText("name"),
										"utf-8"), ApiType.PeopleSearch);
				// ----
				System.out.println(response.getBody());
				// ----
				responseElement = Elements.fromResponse(response);
				people.addAll(Elements.extract(responseElement,
						ElementType.PERSON));
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}
}
