package structure;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Node implements INode {

	private Document doc;

	public Node(String response) {
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(new StringReader(response));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Document getDocument() {
		return doc;
	}

	@Override
	public boolean isPerson() {
		return Elements.isPerson(doc.getRootElement());
	}

	@Override
	public Collection<Element> getElements() {
		return doc.getRootElement().getChildren();
	}

	@Override
	public Element getRootElement() {
		return doc.getRootElement();
	}

}
