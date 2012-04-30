package structure;

import org.jdom2.Element;

public class Elements {

	public static final String PERSON = "person";
	public static final String GROUP = "group";
	public static final String COMPANY = "company";

	public static boolean isPerson(Element e) {
		return PERSON.equals(e.getName());
	}

	public static boolean isGroup(Element e) {
		return GROUP.equals(e.getName());
	}

	public static boolean isCompany(Element e) {
		return COMPANY.equals(e.getName());
	}
}
