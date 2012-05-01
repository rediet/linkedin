package structure;

import org.jdom2.Element;

/**
 * Enum object that represents XML type-names of a LinkedIn request
 */
public enum ElementType {
	UNDEFINED("UNDEFINED"), PERSON("person"), GROUP("group"), COMPANY("company"), PEOPLE_SEARCH(
			"people-search"), PEOPLE_COLLECTION("people");

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

	public static ElementType getTypeOf(Element element) {
		for (ElementType type : ElementType.values()) {
			if (type.hasSameTypeAs(element))
				return type;
		}

		return UNDEFINED;
	}

	public static boolean isKindOf(ElementType type, Element element) {
		return type.hasSameTypeAs(element);
	}
}