package structure;

import java.util.Collection;
import java.util.LinkedList;

import org.jdom2.Element;

/**
 * Utility class to provide operations on XML Elements
 */
public class Elements {

	public static boolean isKindOf(ElementType type, Element element) {
		return type.hasSameTypeAs(element);
	}

	/**
	 * Extracts all elements of the specified type and returns the result as a
	 * Collection. Ignores sub-elements of an Element whose ElementType equals
	 * the specified type.
	 * @param element the root element
	 * @param subType the ElementType of the elements that should be extracted
	 * @return a Collection containing the elements of the specified type.
	 */
	public static Collection<Element> extract(Element element,
			ElementType subType) {
		Collection<Element> subElements = new LinkedList<Element>();
		extract(element, subType, subElements);
		return subElements;
	}

	/**
	 * Recursive lookup function to extract elements of the specified
	 * ElementType
	 **/
	private static void extract(Element element, ElementType type,
			Collection<Element> collection) {
		if (element == null) {
			return;
		} else if (isKindOf(type, element)) {
			collection.add(element);
			return;
		} else {
			for (Element child : element.getChildren()) {
				extract(child, type, collection);
			}
		}
	}
}
