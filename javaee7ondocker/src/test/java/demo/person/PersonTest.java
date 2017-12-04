package demo.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PersonTest {

	/**
	 * Test target1.
	 */
	private Person person1;
	/**
	 * Test target1.
	 */
	private Person person2;

	@Before
	public void initiateTest() {
		person1 = new Person();
		person2 = new Person("TeSt");
	}

	@Test
	public void testGetName() {
		assertNull(person1.getName());
		assertEquals("TeSt", person2.getName());
	}

	@Test
	public void testSetName() {
		person1.setName("TeSt1");
		assertEquals("TeSt1", person1.getName());
		person2.setName("TeSt2");
		assertEquals("TeSt2", person2.getName());
	}

	@Test
	public void testToString() {
		assertNull(person1.toString());
		assertEquals("TeSt", person2.toString());
	}
}
