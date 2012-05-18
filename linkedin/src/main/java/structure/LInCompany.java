package structure;

import org.jdom2.Element;

public class LInCompany extends LInAbstractNode {

	public static ElementType TYPE = ElementType.COMPANY;

	public LInCompany(Element element) {
		super(element, TYPE);
	}

	public String getName() {
		return this.element.getChildText("name");
	}

	public String getUniversalName() {
		return this.element.getChildText("universal-name");
	}

	public int getNumFollowers() {
		try {
			return Integer.parseInt(this.element.getChildText("num-followers"));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}
