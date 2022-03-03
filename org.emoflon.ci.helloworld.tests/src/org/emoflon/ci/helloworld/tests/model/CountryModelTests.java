package org.emoflon.ci.helloworld.tests.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import helloworld.Country;
import helloworld.HelloworldFactory;

public class CountryModelTests {

	@Test
	public void testHelloworldFactoryInstanceNotNull() {
		assertNotNull(HelloworldFactory.eINSTANCE);
	}

	@Test
	public void testGetterSetter() {
		final Country c = HelloworldFactory.eINSTANCE.createCountry();
		c.setPopulation(42);
		assertEquals(42, c.getPopulation());
		c.setName("germany");
		assertEquals("germany", c.getName());
	}

}
