package structure;

import org.jdom2.Element;

/**
 * Wrapper for (JDOM) XML Elements of the type Person. Provides access to fields
 * like id and implements equality checks for persons.
 */
public class LInPerson implements LInNode {

	private static final ElementType ELEMENT_TYPE = ElementType.PERSON;

	private Element element;
	private String id;

	public LInPerson(Element element) {
		if (!ElementType.isKindOf(ELEMENT_TYPE, element)) {
			throw (new IllegalArgumentException("ElementType missmatch"));
		}

		this.element = element;
		this.id = element.getChildText("id");
	}

	public String getId() {
		return this.id;
	}

	public boolean isPrivate() {
		return "private".equals(this.id);
	}

	// TODO: profile API can return id as 'private'
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // self-comparison
			return true;
		if (!(obj instanceof LInPerson))// class of instance
			return false;

		return this.id.equals(((LInPerson) obj).getId());
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean isKindOf(ElementType type) {
		return ELEMENT_TYPE.equals(type);
	}

	@Override
	public Element getRootElement() {
		return element;
	}

	@Override
	public ElementType getType() {
		return ELEMENT_TYPE;
	}

}
