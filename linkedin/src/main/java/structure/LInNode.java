package structure;

import org.jdom2.Element;

public interface LInNode {

	public boolean isKindOf(ElementType type);
	
	public String getId();

	public Element getElement();

	public ElementType getType();

}
