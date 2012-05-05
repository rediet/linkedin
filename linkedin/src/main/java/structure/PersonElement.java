package structure;

import org.jdom2.Element;

@SuppressWarnings("serial")
public class PersonElement extends Element implements LInNode {

	public PersonElement(Element e) {
		super(e.getName(), e.getNamespace());
		if (!ElementType.isKindOf(ElementType.PERSON, e)) {
			throw (new IllegalArgumentException("ElementType missmatch"));
		}
		if (e.getChildText("id") == null) {
			throw (new IllegalArgumentException(
					"Element does not contain attribute <id/>"));
		}
	}

	public void setDistance(int distance) {
		if (this.getChild("distance") != null)
			this.removeChild("distance");
		this.addContent(new Element("distance").setText(String
				.valueOf(distance)));
	}

	public String getDistance() {
		return this.getChildText("distance");
	}

	public String getId() {
		return this.getChildText("id");
	}

	@Override
	public boolean isKindOf(ElementType type) {
		return ElementType.PERSON == type;
	}

	@Override
	public ElementType getType() {
		return ElementType.PERSON;
	}
}
