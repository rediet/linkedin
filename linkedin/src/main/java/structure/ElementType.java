package structure;

import org.jdom2.Element;

/**
 * Enum structure that represents XML type-names of a LinkedIn request
 */
public enum ElementType {
	OTHER("OTHER"), PERSON("person"), GROUP("group"), COMPANY("company"), UPDATE("update");

	private final String name;

	ElementType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public boolean typeEquals(Element e) {
		return this.name.equals(e.getName());
	}

	public boolean typeEquals(String name) {
		return this.name.equals(name);
	}

	public static boolean isKindOf(ElementType type, Element element) {
		return type.typeEquals(element);
	}

	public static ElementType getTypeOf(Element element) {
		for (ElementType type : ElementType.values()) {
			if (type.typeEquals(element))
				return type;
		}

		return OTHER;
	}
}