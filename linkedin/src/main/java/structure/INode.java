package structure;

import java.util.Collection;

import org.jdom2.Element;

public interface INode {

	public boolean isPerson();
	
	public Collection<Element> getElements();
	
	public Element getRootElement();
}
