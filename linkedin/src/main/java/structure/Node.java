package structure;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * XML document wrapper for LinkedIn response body's
 */
public class Node implements INode {

	private Document doc;

	/**
	 * Parses a String to an XML Document containing Elements
	 * @param response
	 */
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
	public Collection<Element> getChildren() {
		return doc.getRootElement().getChildren();
	}

	@Override
	public Element getRootElement() {
		return doc.getRootElement();
	}

	@Override
	public boolean isKindOf(ElementType type) {
		return type.equals(doc.getRootElement().getName());
	}

}
