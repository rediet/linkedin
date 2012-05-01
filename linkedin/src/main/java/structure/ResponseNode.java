package structure;

import java.io.IOException;
import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.scribe.model.Response;

/**
 * XML document wrapper for LinkedIn response body's
 */
public class ResponseNode implements Node {

	private Document doc;

	/**
	 * Parses a String to an XML Document containing Elements
	 * @param response
	 */
	public ResponseNode(String response) {
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
	
	public ResponseNode(Response response) {
		this(response.getBody());
	}

	public Document getDocument() {
		return doc;
	}

	@Override
	public Element getRootElement() {
		return doc.getRootElement();
	}

	// TODO: Do the reference to root element or better to undefined type
	// (Document)?
	@Override
	public boolean isKindOf(ElementType type) {
		return type.equals(doc.getRootElement().getName());
	}

	// TODO: Do the reference to root element or better to undefined type
	// (Document)?
	@Override
	public ElementType getType() {
		return ElementType.UNDEFINED;
	}

}
