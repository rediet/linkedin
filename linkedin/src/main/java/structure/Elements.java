package structure;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.scribe.model.Response;

/**
 * Utility class to provide operations on and to (JDOM) XML Elements
 */
public class Elements {

	/**
	 * Parses an OAuht Response body to a JDOM Document and returns its root
	 * Element
	 * @param response the OAuth Response object
	 * @return the root element
	 */
	public static Element fromResponse(Response response) {
		return fromResponse(response.getBody());
	}

	/**
	 * Parses an OAuth Response body to a JDOM Document and returns its root
	 * Element
	 * @param body the OAuth Response body as string
	 * @return the root element
	 */
	public static Element fromResponse(String body) {
		try {
			Document doc = (new SAXBuilder()).build(new StringReader(body));
			return doc.getRootElement();
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Extracts top occurrences of the specified ElementType within the XML
	 * tree-structure and returns the result as a List. Ignores nested elements
	 * of the same ElementType within an occurrence.
	 * @param element the root element
	 * @param subType the ElementType of the elements that should be extracted
	 * @return a List containing the top occurrences of the specified type.
	 */
	public static List<Element> extract(Element element, ElementType subType) {
		List<Element> subElements = new LinkedList<Element>();
		extract(element, subType, subElements);
		return subElements;
	}

	/**
	 * Extracts all occurrences of the specified ElementType within the XML
	 * tree-structure and returns the result as a List.
	 * @param element the root element
	 * @param subType the ElementType of the elements that should be extracted
	 * @return a List containing all occurrences of the specified type.
	 */
	public static List<Element> extractAll(Element element, ElementType subType) {
		List<Element> subElements = new LinkedList<Element>();
		extract(element, subType, subElements, true);
		return subElements;
	}

	/**
	 * Recursive lookup function to extract top occurrences of elements of the
	 * specified ElementType
	 **/
	private static void extract(Element element, ElementType type,
			List<Element> collection) {
		extract(element, type, collection, false);
	}

	/**
	 * Recursive lookup function to extract all occurrences (sub-elements) of
	 * elements of the specified ElementType
	 **/
	private static void extract(Element element, ElementType type,
			List<Element> collection, boolean nestedEntries) {
		if (element == null) {
			return;
		} else if (ElementType.isKindOf(type, element)) {
			collection.add(element);
			if (!nestedEntries)
				return;
		}

		for (Element child : element.getChildren()) {
			extract(child, type, collection, nestedEntries);
		}
	}
}
