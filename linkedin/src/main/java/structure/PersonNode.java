package structure;

import org.jdom2.Element;

public class PersonNode implements Node {

	private static final ElementType ELEMENT_TYPE = ElementType.PERSON;
	private Element element;

	public PersonNode(Element element) {
		if (!ElementType.isKindOf(ELEMENT_TYPE, element)) {
			throw (new IllegalArgumentException("ElementType missmatch"));
		}

		this.element = element;
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
