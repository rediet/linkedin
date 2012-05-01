package structure;

import org.jdom2.Element;

/**
 * Enum object that represents XML type-names of a LinkedIn request
 */
public enum ElementType {
	PERSON("person"), GROUP("group"), COMPANY("company"), PEOPLE_SEARCH(
			"people-search"), PEOPLE("people");

	private final String name;

	ElementType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public boolean hasSameTypeAs(Element e) {
		return this.name.equals(e.getName());
	}

	public boolean hasSameTypeAs(String name) {
		return this.name.equals(name);
	}
}