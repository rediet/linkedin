package model;

import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
import structure.LInCompany;
import structure.LInGroup;
import structure.LInPerson;

public abstract class Crawler {

	protected Request requester;

	public Crawler(Request requester) {
		this.requester = requester;

	}

	// ELEMENT PARSERS --------------------------------------------------------
	public List<LInPerson> convertPerson(List<Element> list) {
		List<LInPerson> persons = new LinkedList<LInPerson>();
		for (Element e : list) {
			persons.add(new LInPerson(e));
		}
		return persons;
	}

	public List<LInCompany> convertCompany(List<Element> list) {
		List<LInCompany> companies = new LinkedList<LInCompany>();
		for (Element e : list) {
			companies.add(new LInCompany(e));
		}
		return companies;
	}

	public List<LInGroup> convertGroup(List<Element> list) {
		List<LInGroup> groups = new LinkedList<LInGroup>();
		for (Element e : list) {
			groups.add(new LInGroup(e));
		}
		return groups;
	}
}
