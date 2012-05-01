package model;

import java.util.Collection;

import org.jdom2.Element;
import org.scribe.model.Response;

import structure.ElementType;
import structure.Elements;
import structure.ResponseNode;
import model.Request.ApiType;

public class Crawler {

	private Request requester;
	private Collection<Element> firstDegrees;

	public Crawler(Request requester) {
		this.requester = requester;

	}

	public Collection<Element> getFirstDegreeConnections() {
		return firstDegrees;
	}

	public void run() {
		this.firstDegrees = firstDegreeConnections();
	}

	private Collection<Element> firstDegreeConnections() {
		Response response = requester.GET("~/connections", ApiType.Profile);
		ResponseNode node = new ResponseNode(response);
		return Elements.extract(node.getRootElement(), ElementType.PERSON);
	}

}
