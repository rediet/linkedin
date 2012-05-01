package structure;

import org.jdom2.Element;

public interface Node {

	public boolean isKindOf(ElementType type);

	public ElementType getType();

	public Element getRootElement();
}
