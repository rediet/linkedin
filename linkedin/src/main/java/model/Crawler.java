package model;

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

	public Crawler(Request requester) {
		this.requester = requester;

	}

	// METHODS
	public void run() {
		this.firstDegrees = firstDegreeConnections();
		this.firstDegreeUpdates = firstDegreeConnectionUpdates();
		this.groupMemberships = groupMemberships();
		this.companiesFollowing = companiesFollowing();
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
				.GET("~/group-memberships:(group:(id,name,counts-by-category))?membership-state=member",
						ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extract(element, ElementType.GROUP);
	}

	private List<Element> companiesFollowing() {
		Response response = requester.GET("~/following/companies",
				ApiType.People);
		Element element = Elements.fromResponse(response);
		return Elements.extract(element, ElementType.COMPANY);
	}
}
