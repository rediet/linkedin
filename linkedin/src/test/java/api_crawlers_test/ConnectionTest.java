package api_crawlers_test;

import static org.junit.Assert.*;

import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import structure.LInPerson;

import api_crawlers.Connection;

public class ConnectionTest {

	LInPerson person1, person2;

	@Before
	public void setUp() {
		Element first = new Element("person");
		Element second = new Element("person");
		Element id1 = new Element("id");
		Element id2 = new Element("id");
		id1.setText("1234");
		id2.setText("5678");
		first.addContent(id1);
		second.addContent(id2);
		person1 = new LInPerson(first);
		person2 = new LInPerson(second);
	}

	@Test
	public void setupTest() {
		assertEquals("1234", person1.getId());
		assertEquals("5678", person2.getId());
		assertEquals(person1.hashCode(), "1234".hashCode());
		assertEquals(person2.hashCode(), "5678".hashCode());
	}

	@Test
	public void hashCodeTest() {
		Connection connection1 = new Connection(person1, person2);
		Connection connection2 = new Connection(person1, person2);
		Connection connection3 = new Connection(person2, person1);
		assertEquals(connection1.hashCode(), connection2.hashCode());
		assertEquals(connection1.hashCode(), connection3.hashCode());
	}

	@Test
	public void equalsTest() {
		Connection connection1 = new Connection(person1, person2);
		Connection connection2 = new Connection(person1, person2);
		Connection connection3 = new Connection(person2, person1);
		assertEquals(connection1, connection2);
		assertEquals(connection1, connection3);
	}
}
