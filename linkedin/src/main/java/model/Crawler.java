package model;

import java.util.List;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.LInResponse;
import model.Request.ApiType;

public class Crawler {

	private Request requester;
	
	public List<Element> firstDegrees;
	public List<Element> firstDegreeUpdates;

	public Crawler(Request requester) {
		this.requester = requester;

	}

	// METHODS
	public void run() {
		this.firstDegrees = firstDegreeConnections();
		this.firstDegreeUpdates = firstDegreeConnectionUpdates();
	}

	// HELPERS
	private List<Element> firstDegreeConnections() {
		Response response = requester.GET("~/connections", ApiType.People);
		LInResponse node = new LInResponse(response);
		return Elements.extract(node.getRootElement(), ElementType.PERSON);
	}

	private List<Element> firstDegreeConnectionUpdates() {
		Response response = requester.GET("~/network/updates", ApiType.People);
		LInResponse node = new LInResponse(response);
		return Elements.extract(node.getRootElement(), ElementType.UPDATE);
	}

}
