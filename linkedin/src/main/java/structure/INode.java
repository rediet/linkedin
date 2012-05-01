package structure;

import java.util.Collection;

import org.jdom2.Element;

public interface INode {

	public boolean isKindOf(ElementType type);

	public Collection<Element> getChildren();

	public Element getRootElement();
}
