package structure;

import org.jdom2.Element;

public class LInGroup extends LInAbstractNode {

	public static ElementType TYPE = ElementType.GROUP;

	public LInGroup(Element element) {
		super(element, TYPE);
	}

	public String getName() {
		return this.element.getChildText("name");
	}

	public int getNumMembers() {
		try {
			return Integer.parseInt(this.element.getChildText("num-members"));
		} catch (NumberFormatException e) {
			return -1;
		}

	}
	public String getCountryCode() {
		Element e = this.element.getChild("location");
		if (e != null) {
			e = this.element.getChild("country");
			if (e != null) {
				return e.getChildText("code");
			}
		}

		return null;
	}

	public String getPostalCode() {
		Element e = this.element.getChild("location");
		if (e != null) {
			return this.element.getChildText("postal-code");
		}
		return null;
	}

}
