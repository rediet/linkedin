package structure;

import org.jdom2.Element;

public abstract class LInAbstractNode implements LInNode {

	protected Element element;
	protected String id;

	private ElementType type;

	public LInAbstractNode(Element element, ElementType type) {
		if (!ElementType.isKindOf(type, element)) {
			throw (new IllegalArgumentException("ElementType missmatch"));
		}
		if (element.getChildText("id") == null) {
			throw (new IllegalArgumentException(
					"Element does not contain attribute <id/>"));
		}

		this.element = element;
		this.id = element.getChildText("id");
		this.type = type;
	}

	@Override
	public boolean isKindOf(ElementType type) {
		return this.type == type;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public Element getElement() {
		return this.element;
	}

	@Override
	public ElementType getType() {
		return this.type;
	}

	@Override
	public String toString() {
		//return this.element.getValue();
		return this.element.getChildText("first-name") + " " + this.element.getChildText("last-name");
	}

}
