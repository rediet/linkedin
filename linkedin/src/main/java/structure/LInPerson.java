package structure;

import org.jdom2.Element;

/**
 * Wrapper for (JDOM) XML Elements of the type Person. Provides access to fields
 * like id and implements equality checks for persons.
 */
public class LInPerson extends LInAbstractNode {

	public static ElementType TYPE = ElementType.PERSON;

	public LInPerson(Element element) {
		super(element, TYPE);
	}
	
	public String getDistance() {
		return this.element.getChildText("distance");
	}

	public boolean isPrivate() {
		return "private".equals(this.id);
	}

	// Note: the profile API can return id as 'private' (id is not unique)
	@Override
	public boolean equals(Object obj) {
		if (this == obj) // self-comparison
			return true;
		if (!(obj instanceof LInPerson))
			return false;
		LInPerson person = (LInPerson) obj;

		if (this.id != null)
			return this.id.equals(person.getId());
		else
			return this.id == person.getId();

	}

	@Override
	public int hashCode() {
		if (this.id == null)
			return 0;
		else
			return this.id.hashCode();
	}
}
